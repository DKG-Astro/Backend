package com.astro.service.impl;


import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnMaterialsResponseDto;

import com.astro.dto.workflow.InventoryModule.GprnDto.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;

import com.astro.entity.InventoryModule.Gprn;
import com.astro.entity.InventoryModule.GprnMaterials;


import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialsRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnRepository;
import com.astro.service.GprnService;
import com.astro.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class GprnServiceImpl implements GprnService {

    @Autowired
    private GprnRepository gprnRepository;

    @Autowired
    private GprnMaterialsRepository gprnMaterialsRepository;
    private static final Logger log = LoggerFactory.getLogger(GprnServiceImpl.class);

    @Transactional
    public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto) {

        // Map GprnRequestDto to Gprn entity
        Gprn gprn = new Gprn();
        gprn.setPoNo(gprnRequestDto.getPoNo());
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

        // Initialize quantities and provisionalReceiptCertificate
        gprn.setReceivedQty(gprnRequestDto.getReceivedQty());
        gprn.setPendingQty(gprnRequestDto.getPendingQty());
        gprn.setAcceptedQty(gprnRequestDto.getAcceptedQty());
        gprn.setProvisionalReceiptCertificate(gprnRequestDto.getProvisionalReceiptCertificate());
        gprnRepository.save(gprn);
        // Save MaterialDetails entities and link them to the Gprn
        List<GprnMaterials> gprnMaterials = gprnRequestDto.getGprnMaterials().stream().map(materialRequest -> {
            GprnMaterials gprnMaterial = new GprnMaterials();
            gprnMaterial.setDescription(materialRequest.getDescription());
            gprnMaterial.setUom(materialRequest.getUom());
            gprnMaterial.setOrderedQuantity(materialRequest.getOrderedQuantity());
            gprnMaterial.setQuantityDelivered(materialRequest.getQuantityDelivered());
            gprnMaterial.setReceivedQuantity(materialRequest.getReceivedQuantity());
            gprnMaterial.setUnitPrice(materialRequest.getUnitPrice());

            // Compute netPrice based on receivedQuantity and unitPrice
            BigDecimal receivedQuantity = materialRequest.getReceivedQuantity() != null
                    ? BigDecimal.valueOf(materialRequest.getReceivedQuantity())
                    : BigDecimal.ZERO;
            BigDecimal unitPrice = materialRequest.getUnitPrice() != null
                    ? BigDecimal.valueOf(materialRequest.getUnitPrice())
                    : BigDecimal.ZERO;
            gprnMaterial.setNetPrice(receivedQuantity.multiply(unitPrice));

            gprnMaterial.setMakeNo(materialRequest.getMakeNo());
            gprnMaterial.setModelNo(materialRequest.getModelNo());
            gprnMaterial.setSerialNo(materialRequest.getSerialNo());
            gprnMaterial.setWarranty(materialRequest.getWarranty());
            gprnMaterial.setNote(materialRequest.getNote());
            gprnMaterial.setPhotographPath(materialRequest.getPhotographPath());
            gprnMaterial.setGprn(gprn);
            return gprnMaterial;
        }).collect(Collectors.toList());




        // Save all material details
        gprnMaterialsRepository.saveAll(gprnMaterials);


        gprn.setGprnMaterials(gprnMaterials);
        // Link the materials to the GPRN entity

        // Set created and updated information
       gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
        gprn.setCreatedBy(gprnRequestDto.getCreatedBy());




        return mapToResponseDTO(gprn);
    }

    @Transactional
    public GprnResponseDto updateGprn(Long gprnId, GprnRequestDto gprnRequestDto) {

        Gprn gprn = gprnRepository.findById(gprnId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "GPRN not found for the provided asset ID.")
                ));

        // Update Gprn details
        gprn.setPoNo(gprnRequestDto.getPoNo());
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

        // Initialize quantities and provisionalReceiptCertificate
        gprn.setReceivedQty(gprnRequestDto.getReceivedQty());
        gprn.setPendingQty(gprnRequestDto.getPendingQty());
        gprn.setAcceptedQty(gprnRequestDto.getAcceptedQty());
        gprn.setProvisionalReceiptCertificate(gprnRequestDto.getProvisionalReceiptCertificate());
        gprnRepository.save(gprn);

        // Get existing material details
        List<GprnMaterials> existingMaterialDetails = gprn.getGprnMaterials();

        // Clear out the existing materials (so they can be replaced)
        existingMaterialDetails.clear();

        // Map the new materials and add them to the Gprn entity
        List<GprnMaterials> updatedMaterialDetails = gprnRequestDto.getGprnMaterials().stream()
                .map(materialRequestDto -> {
                    GprnMaterials gprnMaterial = new GprnMaterials();

                    // Manually map fields from GprnMaterialsRequestDto to GprnMaterials entity
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
                    gprnMaterial.setPhotographPath(materialRequestDto.getPhotographPath());
                    gprnMaterial.setGprn(gprn); // Ensure the Gprn reference is set

                    return gprnMaterial;
                })
                .collect(Collectors.toList());

        // Add new materials to the Gprn entity
        existingMaterialDetails.addAll(updatedMaterialDetails);

        // Save all material details
        gprnMaterialsRepository.saveAll(updatedMaterialDetails);  // Save all material details

        // Save the Gprn entity with the updated materials
        gprn.setGprnMaterials(existingMaterialDetails);
        gprnRepository.save(gprn);

        return mapToResponseDTO(gprn);
    }

    @Override
    public List<GprnResponseDto> getAllGprn() {
        List<Gprn> gprn = gprnRepository.findAll();
        return gprn.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public GprnResponseDto getGprnById(Long gprnId) {
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
    public void deleteGprn(Long gprnId) {

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
        gprnResponseDto.setPoNo(gprn.getPoNo());
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

        // Ensure quantities are mapped correctly
        gprnResponseDto.setReceivedQty(gprn.getReceivedQty());
        gprnResponseDto.setPendingQty(gprn.getPendingQty());
        gprnResponseDto.setAcceptedQty(gprn.getAcceptedQty());
        gprnResponseDto.setProvisionalReceiptCertificate(gprn.getProvisionalReceiptCertificate());
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
            gprnMaterialsResponsetDto.setPhotographPath(material.getPhotographPath());
            return gprnMaterialsResponsetDto;
        }).collect(Collectors.toList());

        gprnResponseDto.setGprnMaterialsResponsetDtos(gprnMaterialsResponsetDtos);
        return gprnResponseDto;
    }
/*
    // Get all GPRNs
    public List<GprnResponseDto> getAllGprn() {
        List<Gprn> gprns = gprnRepository.findAll();
        return gprns.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // Get GPRN by ID
  c {
        Optional<Gprn> gprnOptional = gprnRepository.findById(gprnId);
        if (!gprnOptional.isPresent()) {
            throw new BusinessException("GPRN with id " + gprnId + " not found.");
        }
        return mapToResponseDTO(gprnOptional.get());
    }

 */
}

