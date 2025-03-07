package com.astro.service.impl;



import com.astro.dto.workflow.InventoryModule.GprnDto.getGprnDtlsDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.saveGprnDto;
import com.astro.repository.InventoryModule.GprnRepository.GprnMasterRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialDetailsRepository;

import com.astro.service.ProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private GprnMasterRepository gprnMasterRepository;

    @Autowired
    private GprnMaterialDetailsRepository gprnMaterialDetailsRepository;
    private static final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);

    @Override
    public saveGprnDto saveGprn(saveGprnDto saveGprnDto) {
        return null;
    }

    @Override
    public getGprnDtlsDto getSubProcessDtls(String processStage, String processId) {
        return null;
    }

    /*    @Transactional
        public getGprnDtlsDto createGprnWithMaterialDetails(saveGprnDto gprnRequestDto){
                //, String provisionalReceiptCertificateFileName, String photoFileName) {
            // Check if the GPRN ID already exists
            if (gprnRepository.existsById(gprnRequestDto.getGprnNo())) {
                throw new InvalidInputException(new ErrorDetails(400, 1, "Duplicate GPRN ID", "GPRN ID " + gprnRequestDto.getGprnNo() + " already exists."));
            }

            // Map GprnRequestDto to Gprn entity
            Gprn gprn = new Gprn();
            gprn.setGprnNo(gprnRequestDto.getGprnNo());
            gprn.setPoId(gprnRequestDto.getPoId());
            gprn.setDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getDate()));
            gprn.setDeliveryChallanNo(gprnRequestDto.getDeliveryChallanNo());
            gprn.setDeliveryChallanDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getDeliveryChallanDate()));
            gprn.setVendorId(gprnRequestDto.getVendorId());
            gprn.setVendorName(gprnRequestDto.getVendorName());
            gprn.setVendorEmail(gprnRequestDto.getVendorEmail());
            gprn.setVendorContactNo(gprnRequestDto.getVendorContactNo());
            gprn.setFieldStation(gprnRequestDto.getFieldStation());
            gprn.setIndentorName(gprnRequestDto.getIndentorName());
            gprn.setExpectedSupplyDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getExpectedSupplyDate()));
            gprn.setConsigneeDetail(gprnRequestDto.getConsigneeDetail());
            gprn.setReceivedBy(gprnRequestDto.getReceivedBy());
            gprn.setProject(gprnRequestDto.getProject());
            gprn.setWarrantyYears(gprnRequestDto.getWarrantyYears());
            gprn.setReceivedQty(gprnRequestDto.getReceivedQty());
            gprn.setPendingQty(gprnRequestDto.getPendingQty());
            gprn.setAcceptedQty(gprnRequestDto.getAcceptedQty());
            handleFileUpload(gprn, gprnRequestDto.getProvisionalReceiptCertificate(), gprn::setProvisionalReceiptCertificate);
            gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
            gprn.setCreatedBy(gprnRequestDto.getCreatedBy());
           // gprn.setProvisionalReceiptCertificateFileName(provisionalReceiptCertificateFileName);

             gprn.setProvisionalReceiptCertificateFileName(gprnRequestDto.getProvisionalReceiptCertificate().getOriginalFilename());
         //   gprnRepository.save(gprn);

            // Process Material Details
            List<GprnMaterials> gprnMaterials = gprnRequestDto.getGprnMaterials().stream().map(materialRequest -> {
                GprnMaterials gprnMaterial = new GprnMaterials();
                gprnMaterial.setGprnNo(gprnRequestDto.getGprnNo());
                gprnMaterial.setMaterialCode(materialRequest.getMaterialCode());
                gprnMaterial.setDescription(materialRequest.getDescription());
                gprnMaterial.setUom(materialRequest.getUom());
                gprnMaterial.setOrderedQuantity(materialRequest.getOrderedQuantity());
                gprnMaterial.setQuantityDelivered(materialRequest.getQuantityDelivered());
                gprnMaterial.setReceivedQuantity(materialRequest.getReceivedQuantity());
                gprnMaterial.setUnitPrice(materialRequest.getUnitPrice());

                BigDecimal receivedQuantity = materialRequest.getReceivedQuantity() != null ? BigDecimal.valueOf(materialRequest.getReceivedQuantity()) : BigDecimal.ZERO;
                BigDecimal unitPrice = materialRequest.getUnitPrice() != null ? BigDecimal.valueOf(materialRequest.getUnitPrice()) : BigDecimal.ZERO;
                gprnMaterial.setNetPrice(receivedQuantity.multiply(unitPrice));

                gprnMaterial.setMakeNo(materialRequest.getMakeNo());
                gprnMaterial.setModelNo(materialRequest.getModelNo());
                gprnMaterial.setSerialNo(materialRequest.getSerialNo());
                gprnMaterial.setWarranty(materialRequest.getWarranty());
                gprnMaterial.setNote(materialRequest.getNote());

                gprnMaterial.setGprn(gprn);
                return gprnMaterial;
            }).collect(Collectors.toList());

            gprn.setGprnMaterials(gprnMaterials);

            gprnRepository.save(gprn);

            return mapToResponseDTO(gprn);

        }


    @Transactional
    public getGprnDtlsDto updateGprn(String gprnId, saveGprnDto gprnRequestDto, String provisionalReceiptCertificateFileName, String photoFileName) {

        Gprn gprn = gprnRepository.findById(gprnId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "GPRN not found for the provided asset ID.")
                ));

        // Update Gprn details
        gprn.setPoId(gprnRequestDto.getPoId());
        gprn.setDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getDate()));
        gprn.setDeliveryChallanNo(gprnRequestDto.getDeliveryChallanNo());
        gprn.setDeliveryChallanDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getDeliveryChallanDate()));
        gprn.setVendorId(gprnRequestDto.getVendorId());
        gprn.setVendorName(gprnRequestDto.getVendorName());
        gprn.setVendorEmail(gprnRequestDto.getVendorEmail());
        gprn.setVendorContactNo(gprnRequestDto.getVendorContactNo());
        gprn.setFieldStation(gprnRequestDto.getFieldStation());
        gprn.setIndentorName(gprnRequestDto.getIndentorName());
        gprn.setExpectedSupplyDate(CommonUtils.convertStringToDateObject(gprnRequestDto.getExpectedSupplyDate()));
        gprn.setConsigneeDetail(gprnRequestDto.getConsigneeDetail());
        gprn.setReceivedBy(gprnRequestDto.getReceivedBy());
        gprn.setProject(gprnRequestDto.getProject());
        gprn.setWarrantyYears(gprnRequestDto.getWarrantyYears());
        gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
        gprn.setCreatedBy(gprnRequestDto.getCreatedBy());

        // Initialize quantities and provisionalReceiptCertificate
        gprn.setReceivedQty(gprnRequestDto.getReceivedQty());
        gprn.setPendingQty(gprnRequestDto.getPendingQty());
        gprn.setAcceptedQty(gprnRequestDto.getAcceptedQty());
        handleFileUpload(gprn, gprnRequestDto.getProvisionalReceiptCertificate(),
                gprn::setProvisionalReceiptCertificate);
        //gprn.setProvisionalReceiptCertificate(gprnRequestDto.getProvisionalReceiptCertificate());
        gprn.setProvisionalReceiptCertificateFileName(provisionalReceiptCertificateFileName);
        gprnRepository.save(gprn);


       // List<GprnMaterials> existingMaterialDetails = gprn.getGprnMaterials();
        //existingMaterialDetails.clear();
        gprn.getGprnMaterials().clear();

        // Map the new materials and add them to the Gprn entity
        List<GprnMaterials> updatedMaterialDetails = gprnRequestDto.getGprnMaterials().stream()
                .map(materialRequestDto -> {
                    GprnMaterials gprnMaterial = new GprnMaterials();

                    gprnMaterial.setGprnNo(gprnRequestDto.getGprnNo());
                    gprnMaterial.setMaterialCode(materialRequestDto.getMaterialCode());
                    gprnMaterial.setDescription(materialRequestDto.getDescription());
                    gprnMaterial.setUom(materialRequestDto.getUom());
                    gprnMaterial.setOrderedQuantity(materialRequestDto.getOrderedQuantity());
                    gprnMaterial.setQuantityDelivered(materialRequestDto.getQuantityDelivered());
                    gprnMaterial.setReceivedQuantity(materialRequestDto.getReceivedQuantity());
                    gprnMaterial.setUnitPrice(materialRequestDto.getUnitPrice());

                    // Compute netPrice based on receivedQuantity and unitPrice
                    BigDecimal receivedQuantity = materialRequestDto.getReceivedQuantity() != null
                            ? BigDecimal.valueOf(materialRequestDto.getReceivedQuantity())
                            : BigDecimal.ZERO;
                    BigDecimal unitPrice = materialRequestDto.getUnitPrice() != null
                            ? BigDecimal.valueOf(materialRequestDto.getUnitPrice())
                            : BigDecimal.ZERO;
                    gprnMaterial.setNetPrice(receivedQuantity.multiply(unitPrice));

                    gprnMaterial.setMakeNo(materialRequestDto.getMakeNo());
                    gprnMaterial.setModelNo(materialRequestDto.getModelNo());
                    gprnMaterial.setSerialNo(materialRequestDto.getSerialNo());
                    gprnMaterial.setWarranty(materialRequestDto.getWarranty());
                    gprnMaterial.setNote(materialRequestDto.getNote());
                    gprnMaterial.setPhotoFileName(photoFileName);
                   // handleFileUploadMaterial(gprnMaterial, materialRequestDto.getPhotographPath(),
                   //        gprnMaterial::setPhotographPath);
                   // handleFileUploadMaterial(gprnMaterial, materialRequestDto.getPhotographPath(), gprnMaterial::setPhotographPath);

                    gprnMaterial.setGprn(gprn); // Ensure the Gprn reference is set

                    return gprnMaterial;
                })
                .collect(Collectors.toList());

        gprn.getGprnMaterials().addAll(updatedMaterialDetails);
        gprnMaterialsRepository.saveAll(updatedMaterialDetails);
        gprnRepository.save(gprn);

        return mapToResponseDTO(gprn);
    }

    @Override
    public List<getGprnDtlsDto> getAllGprn() {
        List<Gprn> gprn = gprnRepository.findAll();
        return gprn.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public getGprnDtlsDto getGprnById(String gprnId) {
        Gprn gprn = gprnRepository.findById(gprnId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Gprn not found for the provided gprn ID.")
                ));
        return mapToResponseDTO(gprn);
    }

    @Override
    public void deleteGprn(String gprnId) {

        Gprn  gprn= gprnRepository.findById(gprnId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Gprn not found for the provided gprnId."
                        )
                ));
        try {
            gprnRepository.delete(gprn);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the gprn."
                    ),
                    ex
            );
        }
    }


    private getGprnDtlsDto mapToResponseDTO(Gprn gprn) {
        getGprnDtlsDto gprnResponseDto = new getGprnDtlsDto();
        gprnResponseDto.setGprnNo(gprn.getGprnNo());
        gprnResponseDto.setGprnNo(gprn.getGprnNo());
        gprnResponseDto.setPoId(gprn.getPoId());
        gprnResponseDto.setDate(CommonUtils.convertDateToString(gprn.getDate()));
        gprnResponseDto.setDeliveryChallanNo(gprn.getDeliveryChallanNo());
        gprnResponseDto.setDeliveryChallanDate(CommonUtils.convertDateToString(gprn.getDeliveryChallanDate()));
        gprnResponseDto.setVendorId(gprn.getVendorId());
        gprnResponseDto.setVendorName(gprn.getVendorName());
        gprnResponseDto.setFieldStation(gprn.getFieldStation());
        gprnResponseDto.setIndentorName(gprn.getIndentorName());
        gprnResponseDto.setExpectedSupplyDate(CommonUtils.convertDateToString(gprn.getExpectedSupplyDate()));
        gprnResponseDto.setConsigneeDetail(gprn.getConsigneeDetail());
        gprnResponseDto.setReceivedBy(gprn.getReceivedBy());
        gprnResponseDto.setWarrantyYears(gprn.getWarrantyYears());
        gprnResponseDto.setProject(gprn.getProject());
        gprnResponseDto.setVendorEmail(gprn.getVendorEmail());
        gprnResponseDto.setVendorContactNo(gprn.getVendorContactNo());
        gprnResponseDto.setCreatedBy(gprn.getCreatedBy());
        gprnResponseDto.setUpdatedBy(gprn.getUpdatedBy());
        // Ensure quantities are mapped correctly
        gprnResponseDto.setReceivedQty(gprn.getReceivedQty());
        gprnResponseDto.setPendingQty(gprn.getPendingQty());
        gprnResponseDto.setAcceptedQty(gprn.getAcceptedQty());
      //  gprnResponseDto.setProvisionalReceiptCertificate(gprn.getProvisionalReceiptCertificate());
        gprnResponseDto.setProvisionalReceiptCertificateFileName(gprn.getProvisionalReceiptCertificateFileName());
        gprnResponseDto.setCreatedDate(gprn.getCreatedDate());
        gprnResponseDto.setUpdatedDate(gprn.getUpdatedDate());
        // Map material details
        List<GprnMaterialsResponseDto> gprnMaterialsResponsetDtos = gprn.getGprnMaterials().stream().map(material -> {
            GprnMaterialsResponseDto gprnMaterialsResponsetDto = new GprnMaterialsResponseDto();
            gprnMaterialsResponsetDto.setMaterialCode(material.getMaterialCode());
            gprnMaterialsResponsetDto.setDescription(material.getDescription());
            gprnMaterialsResponsetDto.setUom(material.getUom());
            gprnMaterialsResponsetDto.setOrderedQuantity(material.getOrderedQuantity());
            gprnMaterialsResponsetDto.setQuantityDelivered(material.getQuantityDelivered());
            gprnMaterialsResponsetDto.setReceivedQuantity(material.getReceivedQuantity());
            gprnMaterialsResponsetDto.setUnitPrice(material.getUnitPrice());
            gprnMaterialsResponsetDto.setNetPrice(material.getNetPrice());
            gprnMaterialsResponsetDto.setMakeNo(material.getMakeNo());
            gprnMaterialsResponsetDto.setModelNo(material.getModelNo());
            gprnMaterialsResponsetDto.setSerialNo(material.getSerialNo());
            gprnMaterialsResponsetDto.setWarranty(material.getWarranty());
            gprnMaterialsResponsetDto.setNote(material.getNote());
          //  gprnMaterialsResponsetDto.setPhotoFileName(material.getPhotoFileName());
            gprnMaterialsResponsetDto.setPhotoFileName(material.getPhotoFileName() != null ? material.getPhotoFileName() : "No File Uploaded");
            return gprnMaterialsResponsetDto;
        }).collect(Collectors.toList());

        gprnResponseDto.setGprnMaterialsResponsetDtos(gprnMaterialsResponsetDtos);
        return gprnResponseDto;
    }




     */







}

