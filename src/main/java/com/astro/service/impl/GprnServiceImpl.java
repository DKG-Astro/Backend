package com.astro.service.impl;


import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnMaterialsRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnMaterialsResponseDto;

import com.astro.dto.workflow.InventoryModule.GprnDto.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;

import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderAttributesDTO;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.entity.InventoryModule.GprnMaterials;


import com.astro.entity.InventoryModule.GprnMaterialsId;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialsRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnRepository;
import com.astro.service.GprnService;
import com.astro.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GprnServiceImpl implements GprnService {

    @Autowired
    private GprnRepository gprnRepository;

    @Autowired
    private GprnMaterialsRepository gprnMaterialsRepository;
    private static final Logger log = LoggerFactory.getLogger(GprnServiceImpl.class);

        @Transactional
        public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto){
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
             //   gprnMaterial.setPhotoFileName(materialRequest.getPhotographPath());
               // handleFileUploadMaterial(gprnMaterial, materialRequest.getPhotographPath(), gprnMaterial::setPhotographPath);
                String storedFilePaths = materialRequest.getPhotographPath();
                String fileName = storedFilePaths != null && !storedFilePaths.isEmpty() ? new File(storedFilePaths).getName() : null;

                gprnMaterial.setPhotoFileName(fileName);
                String storedFilePath = materialRequest.getPhotographPath(); // Ensure this holds the correct file path
                if (storedFilePath != null && !storedFilePath.isEmpty()) {
                    gprnMaterial.setPhotographPath(storedFilePath);
                } else {
                    log.warn("Photograph path is missing for material: " + materialRequest.getMaterialCode());
                }

                gprnMaterial.setGprn(gprn);
                return gprnMaterial;
            }).collect(Collectors.toList());

            gprn.setGprnMaterials(gprnMaterials);

            gprnRepository.save(gprn);

            return mapToResponseDTO(gprn);

        }


    @Transactional
    public GprnResponseDto updateGprn(String gprnId, GprnRequestDto gprnRequestDto,String provisionalReceiptCertificateFileName, String photoFileName) {

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
      //  updatedMaterialDetails.forEach(material -> material.getGprns().add(gprn)); // Ensure bidirectional relationship
        gprnMaterialsRepository.saveAll(updatedMaterialDetails);
        gprnRepository.save(gprn);

        return mapToResponseDTO(gprn);
    }

    @Override
    public List<GprnResponseDto> getAllGprn() {
        List<Gprn> gprn = gprnRepository.findAll();
        return gprn.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public GprnResponseDto getGprnById(String gprnId) {
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


    private GprnResponseDto mapToResponseDTO(Gprn gprn) {
        GprnResponseDto gprnResponseDto = new GprnResponseDto();
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

    public void handleFileUpload(Gprn gprn, MultipartFile file, Consumer<byte[]> fileSetter) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileSetter.accept(fileBytes); // Set file content (byte[])

            } catch (IOException e) {
                throw new InvalidInputException(new ErrorDetails(500, 3, "File Processing Error",
                        "Error while processing the uploaded file. Please try again."));
            }
        } else {
            fileSetter.accept(null);  // Handle gracefully if no file is uploaded
        }
    }
    public void handleFileUploadMaterial(GprnMaterials gprnMaterials, MultipartFile file, Consumer<byte[]> fileSetter) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileSetter.accept(fileBytes); // Set file content (byte[])

            } catch (IOException e) {
                throw new InvalidInputException(new ErrorDetails(500, 3, "File Processing Error",
                        "Error while processing the uploaded file. Please try again."));
            }
        } else {
            fileSetter.accept(null);  // Handle gracefully if no file is uploaded
        }
    }








}

