package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GprnDto.GetGprnDtlsDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.MaterialDtlDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.entity.InventoryModule.GprnMasterEntity;
import com.astro.entity.InventoryModule.GprnMaterialDtlEntity;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.GprnRepository.GprnMasterRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialDtlRepository;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderRepository;
import com.astro.service.ProcessService;
import com.astro.util.CommonUtils;
import com.azure.core.exception.ResourceNotFoundException;
import com.astro.exception.InvalidInputException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Value("${filePath}")
    private String basePath;

    @Autowired
    private GprnMasterRepository gmr;

    @Autowired
    private PurchaseOrderRepository por;

    @Autowired
    private GprnMaterialDtlRepository gmdr;

    private static final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);

    private void validatePoId(String poId) {
        if (!por.existsById(poId)) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Provided PO ID is not valid."));
        }
    }

    @Override
    @Transactional
    public String saveGprn(SaveGprnDto req) {
        // validatePoId(req.getPoId());

        GprnMasterEntity gme = new GprnMasterEntity();

        ModelMapper mapper = new ModelMapper();
        mapper.map(req, gme);
        gme.setDate(CommonUtils.convertStringToDateObject(req.getDate()));
        gme.setCreateDate(LocalDateTime.now());
        gme.setDeliveryDate(CommonUtils.convertStringToDateObject(req.getDeliveryDate()));
        gme.setSupplyExpectedDate(CommonUtils.convertStringToDateObject(req.getSupplyExpectedDate()));
        gme.setUpdateDate(LocalDateTime.now());
        gme.setProcessId(req.getPoId().substring(2));

        gme = gmr.save(gme);

        List<GprnMaterialDtlEntity> saveDtlEntityList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder(); // to add error messages and throw at once.
        Boolean errorFound = false;

        // fetch material details based on poId and material code
        // add the received quantities to get the previous received quantity
        for (MaterialDtlDto dtl : req.getMaterialDtlList()) {
            List<GprnMaterialDtlEntity> gmdeList = gmdr.findByPoIdAndMaterialCode(req.getPoId(), dtl.getMaterialCode());

            System.out.println("LENGTHHHH: " + gmdeList.size());

            // previously received quantity
            BigDecimal prevRecQuant = gmdeList.stream().map(gmde -> {
                return gmde.getReceivedQuantity();
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            // total received quantity
            BigDecimal totalRecQuant = prevRecQuant.add(dtl.getReceivedQuantity());
            if (totalRecQuant.compareTo(dtl.getOrderedQuantity()) > 0) {
                errorMessage.append(
                        "Total received quantity for " + dtl.getMaterialCode() + " is more than ordered quantity.");
                errorFound = true;
                continue;
            } else if (errorFound)
                continue;

            // create new entity
            GprnMaterialDtlEntity gmde = new GprnMaterialDtlEntity();
            mapper.map(dtl, gmde);
            gmde.setProcessId(gme.getProcessId());
            gmde.setPoId(gme.getPoId());
            gmde.setSubProcessId(gme.getSubProcessId());
            try {
                String imageFileName = CommonUtils.saveBase64Image(dtl.getImageBase64(), basePath + "/INV");
                gmde.setFileName(imageFileName);
            } catch (Exception e) {
                throw new InvalidInputException(new ErrorDetails(
                        AppConstant.FILE_UPLOAD_ERROR,
                        AppConstant.USER_INVALID_INPUT,
                        AppConstant.ERROR_TYPE_CORRUPTED,
                        "Error while uploading image."));
            }

            saveDtlEntityList.add(gmde);
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }
        gmdr.saveAll(saveDtlEntityList);

        // the process no. that we pass to user
        return "INV"+gme.getProcessId()+"/"+gme.getSubProcessId();
    }

    @Override
    public Object getSubProcessDtls(String processStage, String processNo) {
        if(Objects.isNull(processNo) || processNo.isEmpty() || Objects.isNull(processStage) || processStage.isEmpty()){
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Process Id or Process Stage is empty."));
        }

        switch (processStage) {
            case "GPRN":
                return getGprnDtls(processNo);
            // case "2":
            //     return getGprnDtlsDto(processId, "2");
            // case "3":
            //     return getGprnDtlsDto(processId, "3");
            default:
                throw new BusinessException(
                    new ErrorDetails(AppConstant.ERROR_TYPE_CODE_DB,
                        AppConstant.ERROR_TYPE_CODE_DB, 
                        AppConstant.ERROR_TYPE_ERROR, 
                        "Invalid process stage.")
                );
        }

    }

    private SaveGprnDto getGprnDtls(String processNo) {
        ModelMapper mapper = new ModelMapper();
        // processNo that we provide user is of the form INV[po number]/[sub_process_id]
        // fetch just subProcessId
        String[] processNoSplit = processNo.split("/");
        if(processNoSplit.length != 2){
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer subProcessId = Integer.parseInt(processNoSplit[1]);

        // fetch the processId from subProcessId
        GprnMasterEntity gme = gmr.findById(subProcessId)
            .orElseThrow(() -> 
                new InvalidInputException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Process not found for the provided process ID."))
            );
        
            // fetch material details using subprocess id

            List<GprnMaterialDtlEntity> gmdeList = gmdr.findBySubProcessId(gme.getSubProcessId()); 

            // map the entity to dto
            List<MaterialDtlDto> materialDtlListRes = gmdeList.stream()
                .map(gmde -> {
                    MaterialDtlDto mdd = mapper.map(gmde, MaterialDtlDto.class);
                    try{
                        String imageBase64 = CommonUtils.convertImageToBase64(gmde.getFileName(), basePath + "/INV");
                        mdd.setImageBase64(imageBase64);
                    }
                    catch(Exception e){
                        System.out.println("EXCEPTION: " + e.getMessage());
                    }

                    return mdd;
                    
                })
                .collect(Collectors.toList());

            SaveGprnDto gprnRes = mapper.map(gme, SaveGprnDto.class);
            gprnRes.setDate(CommonUtils.convertDateToString(gme.getDate()));
            gprnRes.setSupplyExpectedDate(CommonUtils.convertDateToString(gme.getSupplyExpectedDate()));
            gprnRes.setDeliveryDate(CommonUtils.convertDateToString(gme.getDeliveryDate()));
            gprnRes.setMaterialDtlList(materialDtlListRes);

            return gprnRes;

    }

    /*
     * @Transactional
     * public getGprnDtlsDto createGprnWithMaterialDetails(saveGprnDto
     * gprnRequestDto){
     * //, String provisionalReceiptCertificateFileName, String photoFileName) {
     * // Check if the GPRN ID already exists
     * if (gprnRepository.existsById(gprnRequestDto.getGprnNo())) {
     * throw new InvalidInputException(new ErrorDetails(400, 1, "Duplicate GPRN ID",
     * "GPRN ID " + gprnRequestDto.getGprnNo() + " already exists."));
     * }
     * 
     * // Map GprnRequestDto to Gprn entity
     * Gprn gprn = new Gprn();
     * gprn.setGprnNo(gprnRequestDto.getGprnNo());
     * gprn.setPoId(gprnRequestDto.getPoId());
     * gprn.setDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getDate()))
     * ;
     * gprn.setDeliveryChallanNo(gprnRequestDto.getDeliveryChallanNo());
     * gprn.setDeliveryChallanDate(CommonUtils.convertStringToDateObject(
     * gprnRequestDto.getDeliveryChallanDate()));
     * gprn.setVendorId(gprnRequestDto.getVendorId());
     * gprn.setVendorName(gprnRequestDto.getVendorName());
     * gprn.setVendorEmail(gprnRequestDto.getVendorEmail());
     * gprn.setVendorContactNo(gprnRequestDto.getVendorContactNo());
     * gprn.setFieldStation(gprnRequestDto.getFieldStation());
     * gprn.setIndentorName(gprnRequestDto.getIndentorName());
     * gprn.setExpectedSupplyDate(CommonUtils.convertStringToDateObject(
     * gprnRequestDto.getExpectedSupplyDate()));
     * gprn.setConsigneeDetail(gprnRequestDto.getConsigneeDetail());
     * gprn.setReceivedBy(gprnRequestDto.getReceivedBy());
     * gprn.setProject(gprnRequestDto.getProject());
     * gprn.setWarrantyYears(gprnRequestDto.getWarrantyYears());
     * gprn.setReceivedQty(gprnRequestDto.getReceivedQty());
     * gprn.setPendingQty(gprnRequestDto.getPendingQty());
     * gprn.setAcceptedQty(gprnRequestDto.getAcceptedQty());
     * handleFileUpload(gprn, gprnRequestDto.getProvisionalReceiptCertificate(),
     * gprn::setProvisionalReceiptCertificate);
     * gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
     * gprn.setCreatedBy(gprnRequestDto.getCreatedBy());
     * // gprn.setProvisionalReceiptCertificateFileName(
     * provisionalReceiptCertificateFileName);
     * 
     * gprn.setProvisionalReceiptCertificateFileName(gprnRequestDto.
     * getProvisionalReceiptCertificate().getOriginalFilename());
     * // gprnRepository.save(gprn);
     * 
     * // Process Material Details
     * List<GprnMaterials> gprnMaterials =
     * gprnRequestDto.getGprnMaterials().stream().map(materialRequest -> {
     * GprnMaterials gprnMaterial = new GprnMaterials();
     * gprnMaterial.setGprnNo(gprnRequestDto.getGprnNo());
     * gprnMaterial.setMaterialCode(materialRequest.getMaterialCode());
     * gprnMaterial.setDescription(materialRequest.getDescription());
     * gprnMaterial.setUom(materialRequest.getUom());
     * gprnMaterial.setOrderedQuantity(materialRequest.getOrderedQuantity());
     * gprnMaterial.setQuantityDelivered(materialRequest.getQuantityDelivered());
     * gprnMaterial.setReceivedQuantity(materialRequest.getReceivedQuantity());
     * gprnMaterial.setUnitPrice(materialRequest.getUnitPrice());
     * 
     * BigDecimal receivedQuantity = materialRequest.getReceivedQuantity() != null ?
     * BigDecimal.valueOf(materialRequest.getReceivedQuantity()) : BigDecimal.ZERO;
     * BigDecimal unitPrice = materialRequest.getUnitPrice() != null ?
     * BigDecimal.valueOf(materialRequest.getUnitPrice()) : BigDecimal.ZERO;
     * gprnMaterial.setNetPrice(receivedQuantity.multiply(unitPrice));
     * 
     * gprnMaterial.setMakeNo(materialRequest.getMakeNo());
     * gprnMaterial.setModelNo(materialRequest.getModelNo());
     * gprnMaterial.setSerialNo(materialRequest.getSerialNo());
     * gprnMaterial.setWarranty(materialRequest.getWarranty());
     * gprnMaterial.setNote(materialRequest.getNote());
     * 
     * gprnMaterial.setGprn(gprn);
     * return gprnMaterial;
     * }).collect(Collectors.toList());
     * 
     * gprn.setGprnMaterials(gprnMaterials);
     * 
     * gprnRepository.save(gprn);
     * 
     * return mapToResponseDTO(gprn);
     * 
     * }
     * 
     * 
     * @Transactional
     * public getGprnDtlsDto updateGprn(String gprnId, saveGprnDto gprnRequestDto,
     * String provisionalReceiptCertificateFileName, String photoFileName) {
     * 
     * Gprn gprn = gprnRepository.findById(gprnId)
     * .orElseThrow(() -> new BusinessException(
     * new ErrorDetails(
     * AppConstant.ERROR_CODE_RESOURCE,
     * AppConstant.ERROR_TYPE_CODE_RESOURCE,
     * AppConstant.ERROR_TYPE_VALIDATION,
     * "GPRN not found for the provided asset ID.")
     * ));
     * 
     * // Update Gprn details
     * gprn.setPoId(gprnRequestDto.getPoId());
     * gprn.setDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getDate()))
     * ;
     * gprn.setDeliveryChallanNo(gprnRequestDto.getDeliveryChallanNo());
     * gprn.setDeliveryChallanDate(CommonUtils.convertStringToDateObject(
     * gprnRequestDto.getDeliveryChallanDate()));
     * gprn.setVendorId(gprnRequestDto.getVendorId());
     * gprn.setVendorName(gprnRequestDto.getVendorName());
     * gprn.setVendorEmail(gprnRequestDto.getVendorEmail());
     * gprn.setVendorContactNo(gprnRequestDto.getVendorContactNo());
     * gprn.setFieldStation(gprnRequestDto.getFieldStation());
     * gprn.setIndentorName(gprnRequestDto.getIndentorName());
     * gprn.setExpectedSupplyDate(CommonUtils.convertStringToDateObject(
     * gprnRequestDto.getExpectedSupplyDate()));
     * gprn.setConsigneeDetail(gprnRequestDto.getConsigneeDetail());
     * gprn.setReceivedBy(gprnRequestDto.getReceivedBy());
     * gprn.setProject(gprnRequestDto.getProject());
     * gprn.setWarrantyYears(gprnRequestDto.getWarrantyYears());
     * gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
     * gprn.setCreatedBy(gprnRequestDto.getCreatedBy());
     * 
     * // Initialize quantities and provisionalReceiptCertificate
     * gprn.setReceivedQty(gprnRequestDto.getReceivedQty());
     * gprn.setPendingQty(gprnRequestDto.getPendingQty());
     * gprn.setAcceptedQty(gprnRequestDto.getAcceptedQty());
     * handleFileUpload(gprn, gprnRequestDto.getProvisionalReceiptCertificate(),
     * gprn::setProvisionalReceiptCertificate);
     * //gprn.setProvisionalReceiptCertificate(gprnRequestDto.
     * getProvisionalReceiptCertificate());
     * gprn.setProvisionalReceiptCertificateFileName(
     * provisionalReceiptCertificateFileName);
     * gprnRepository.save(gprn);
     * 
     * 
     * // List<GprnMaterials> existingMaterialDetails = gprn.getGprnMaterials();
     * //existingMaterialDetails.clear();
     * gprn.getGprnMaterials().clear();
     * 
     * // Map the new materials and add them to the Gprn entity
     * List<GprnMaterials> updatedMaterialDetails =
     * gprnRequestDto.getGprnMaterials().stream()
     * .map(materialRequestDto -> {
     * GprnMaterials gprnMaterial = new GprnMaterials();
     * 
     * gprnMaterial.setGprnNo(gprnRequestDto.getGprnNo());
     * gprnMaterial.setMaterialCode(materialRequestDto.getMaterialCode());
     * gprnMaterial.setDescription(materialRequestDto.getDescription());
     * gprnMaterial.setUom(materialRequestDto.getUom());
     * gprnMaterial.setOrderedQuantity(materialRequestDto.getOrderedQuantity());
     * gprnMaterial.setQuantityDelivered(materialRequestDto.getQuantityDelivered());
     * gprnMaterial.setReceivedQuantity(materialRequestDto.getReceivedQuantity());
     * gprnMaterial.setUnitPrice(materialRequestDto.getUnitPrice());
     * 
     * // Compute netPrice based on receivedQuantity and unitPrice
     * BigDecimal receivedQuantity = materialRequestDto.getReceivedQuantity() !=
     * null
     * ? BigDecimal.valueOf(materialRequestDto.getReceivedQuantity())
     * : BigDecimal.ZERO;
     * BigDecimal unitPrice = materialRequestDto.getUnitPrice() != null
     * ? BigDecimal.valueOf(materialRequestDto.getUnitPrice())
     * : BigDecimal.ZERO;
     * gprnMaterial.setNetPrice(receivedQuantity.multiply(unitPrice));
     * 
     * gprnMaterial.setMakeNo(materialRequestDto.getMakeNo());
     * gprnMaterial.setModelNo(materialRequestDto.getModelNo());
     * gprnMaterial.setSerialNo(materialRequestDto.getSerialNo());
     * gprnMaterial.setWarranty(materialRequestDto.getWarranty());
     * gprnMaterial.setNote(materialRequestDto.getNote());
     * gprnMaterial.setPhotoFileName(photoFileName);
     * // handleFileUploadMaterial(gprnMaterial,
     * materialRequestDto.getPhotographPath(),
     * // gprnMaterial::setPhotographPath);
     * // handleFileUploadMaterial(gprnMaterial,
     * materialRequestDto.getPhotographPath(), gprnMaterial::setPhotographPath);
     * 
     * gprnMaterial.setGprn(gprn); // Ensure the Gprn reference is set
     * 
     * return gprnMaterial;
     * })
     * .collect(Collectors.toList());
     * 
     * gprn.getGprnMaterials().addAll(updatedMaterialDetails);
     * gprnMaterialsRepository.saveAll(updatedMaterialDetails);
     * gprnRepository.save(gprn);
     * 
     * return mapToResponseDTO(gprn);
     * }
     * 
     * @Override
     * public List<getGprnDtlsDto> getAllGprn() {
     * List<Gprn> gprn = gprnRepository.findAll();
     * return
     * gprn.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
     * }
     * 
     * @Override
     * public getGprnDtlsDto getGprnById(String gprnId) {
     * Gprn gprn = gprnRepository.findById(gprnId)
     * .orElseThrow(() -> new BusinessException(
     * new ErrorDetails(
     * AppConstant.ERROR_CODE_RESOURCE,
     * AppConstant.ERROR_TYPE_CODE_RESOURCE,
     * AppConstant.ERROR_TYPE_RESOURCE,
     * "Gprn not found for the provided gprn ID.")
     * ));
     * return mapToResponseDTO(gprn);
     * }
     * 
     * @Override
     * public void deleteGprn(String gprnId) {
     * 
     * Gprn gprn= gprnRepository.findById(gprnId)
     * .orElseThrow(() -> new BusinessException(
     * new ErrorDetails(
     * AppConstant.ERROR_CODE_RESOURCE,
     * AppConstant.ERROR_TYPE_CODE_RESOURCE,
     * AppConstant.ERROR_TYPE_RESOURCE,
     * "Gprn not found for the provided gprnId."
     * )
     * ));
     * try {
     * gprnRepository.delete(gprn);
     * } catch (Exception ex) {
     * throw new BusinessException(
     * new ErrorDetails(
     * AppConstant.INTER_SERVER_ERROR,
     * AppConstant.ERROR_TYPE_CODE_INTERNAL,
     * AppConstant.ERROR_TYPE_ERROR,
     * "An error occurred while deleting the gprn."
     * ),
     * ex
     * );
     * }
     * }
     * 
     * 
     * private getGprnDtlsDto mapToResponseDTO(Gprn gprn) {
     * getGprnDtlsDto gprnResponseDto = new getGprnDtlsDto();
     * gprnResponseDto.setGprnNo(gprn.getGprnNo());
     * gprnResponseDto.setGprnNo(gprn.getGprnNo());
     * gprnResponseDto.setPoId(gprn.getPoId());
     * gprnResponseDto.setDate(CommonUtils.convertDateToString(gprn.getDate()));
     * gprnResponseDto.setDeliveryChallanNo(gprn.getDeliveryChallanNo());
     * gprnResponseDto.setDeliveryChallanDate(CommonUtils.convertDateToString(gprn.
     * getDeliveryChallanDate()));
     * gprnResponseDto.setVendorId(gprn.getVendorId());
     * gprnResponseDto.setVendorName(gprn.getVendorName());
     * gprnResponseDto.setFieldStation(gprn.getFieldStation());
     * gprnResponseDto.setIndentorName(gprn.getIndentorName());
     * gprnResponseDto.setExpectedSupplyDate(CommonUtils.convertDateToString(gprn.
     * getExpectedSupplyDate()));
     * gprnResponseDto.setConsigneeDetail(gprn.getConsigneeDetail());
     * gprnResponseDto.setReceivedBy(gprn.getReceivedBy());
     * gprnResponseDto.setWarrantyYears(gprn.getWarrantyYears());
     * gprnResponseDto.setProject(gprn.getProject());
     * gprnResponseDto.setVendorEmail(gprn.getVendorEmail());
     * gprnResponseDto.setVendorContactNo(gprn.getVendorContactNo());
     * gprnResponseDto.setCreatedBy(gprn.getCreatedBy());
     * gprnResponseDto.setUpdatedBy(gprn.getUpdatedBy());
     * // Ensure quantities are mapped correctly
     * gprnResponseDto.setReceivedQty(gprn.getReceivedQty());
     * gprnResponseDto.setPendingQty(gprn.getPendingQty());
     * gprnResponseDto.setAcceptedQty(gprn.getAcceptedQty());
     * // gprnResponseDto.setProvisionalReceiptCertificate(gprn.
     * getProvisionalReceiptCertificate());
     * gprnResponseDto.setProvisionalReceiptCertificateFileName(gprn.
     * getProvisionalReceiptCertificateFileName());
     * gprnResponseDto.setCreatedDate(gprn.getCreatedDate());
     * gprnResponseDto.setUpdatedDate(gprn.getUpdatedDate());
     * // Map material details
     * List<GprnMaterialsResponseDto> gprnMaterialsResponsetDtos =
     * gprn.getGprnMaterials().stream().map(material -> {
     * GprnMaterialsResponseDto gprnMaterialsResponsetDto = new
     * GprnMaterialsResponseDto();
     * gprnMaterialsResponsetDto.setMaterialCode(material.getMaterialCode());
     * gprnMaterialsResponsetDto.setDescription(material.getDescription());
     * gprnMaterialsResponsetDto.setUom(material.getUom());
     * gprnMaterialsResponsetDto.setOrderedQuantity(material.getOrderedQuantity());
     * gprnMaterialsResponsetDto.setQuantityDelivered(material.getQuantityDelivered(
     * ));
     * gprnMaterialsResponsetDto.setReceivedQuantity(material.getReceivedQuantity())
     * ;
     * gprnMaterialsResponsetDto.setUnitPrice(material.getUnitPrice());
     * gprnMaterialsResponsetDto.setNetPrice(material.getNetPrice());
     * gprnMaterialsResponsetDto.setMakeNo(material.getMakeNo());
     * gprnMaterialsResponsetDto.setModelNo(material.getModelNo());
     * gprnMaterialsResponsetDto.setSerialNo(material.getSerialNo());
     * gprnMaterialsResponsetDto.setWarranty(material.getWarranty());
     * gprnMaterialsResponsetDto.setNote(material.getNote());
     * // gprnMaterialsResponsetDto.setPhotoFileName(material.getPhotoFileName());
     * gprnMaterialsResponsetDto.setPhotoFileName(material.getPhotoFileName() !=
     * null ? material.getPhotoFileName() : "No File Uploaded");
     * return gprnMaterialsResponsetDto;
     * }).collect(Collectors.toList());
     * 
     * gprnResponseDto.setGprnMaterialsResponsetDtos(gprnMaterialsResponsetDtos);
     * return gprnResponseDto;
     * }
     * 
     * 
     * 
     * 
     */

}
