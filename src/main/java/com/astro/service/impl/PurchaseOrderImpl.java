package com.astro.service.impl;



import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderAttributesDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderAttributesResponseDTO;

import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderResponseDTO;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import com.astro.entity.ProcurementModule.PurchaseOrderAttributes;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderAttributesRepository;

import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderRepository;

import com.astro.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderAttributesRepository purchaseOrderAttributesRepository;

    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO) {

        // Check if the indentorId already exists
        if (purchaseOrderRepository.existsById(purchaseOrderRequestDTO.getPoId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Purchase Order ID", "PO ID " + purchaseOrderRequestDTO.getPoId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

        // Iterate over materialDetails and check if materialCode already exists
        for (PurchaseOrderAttributesDTO materialRequest : purchaseOrderRequestDTO.getPurchaseOrderAttributes()) {
            if (purchaseOrderAttributesRepository.existsById(materialRequest.getMaterialCode())) {
                ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Material Code",
                        "Material Code " + materialRequest.getMaterialCode() + " already exists.");
                throw new InvalidInputException(errorDetails);
            }
        }
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPoId(purchaseOrderRequestDTO.getPoId());
        purchaseOrder.setTenderId(purchaseOrderRequestDTO.getTenderId());
        purchaseOrder.setIndentId(purchaseOrderRequestDTO.getIndentId());
        purchaseOrder.setWarranty(purchaseOrderRequestDTO.getWarranty());
        purchaseOrder.setConsignesAddress(purchaseOrderRequestDTO.getConsignesAddress());
        purchaseOrder.setBillingAddress(purchaseOrderRequestDTO.getBillingAddress());
        purchaseOrder.setDeliveryPeriod(purchaseOrderRequestDTO.getDeliveryPeriod());
        purchaseOrder.setIfLdClauseApplicable(purchaseOrderRequestDTO.getIfLdClauseApplicable());
        purchaseOrder.setIncoTerms(purchaseOrderRequestDTO.getIncoTerms());
        purchaseOrder.setPaymentTerms(purchaseOrderRequestDTO.getPaymentTerms());
        purchaseOrder.setVendorName(purchaseOrderRequestDTO.getVendorName());
        purchaseOrder.setVendorAddress(purchaseOrderRequestDTO.getVendorAddress());
        purchaseOrder.setApplicablePbgToBeSubmitted(purchaseOrderRequestDTO.getApplicablePbgToBeSubmitted());
        purchaseOrder.setTransporterAndFreightForWarderDetails(purchaseOrderRequestDTO.getTransporterAndFreightForWarderDetails());
        purchaseOrder.setVendorAccountNumber(purchaseOrderRequestDTO.getVendorAccountNumber());
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsZfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        purchaseOrder.setCreatedBy(purchaseOrderRequestDTO.getCreatedBy());
        purchaseOrder.setUpdatedBy(purchaseOrderRequestDTO.getUpdatedBy());
        List<PurchaseOrderAttributes> purchaseOrderAttributes = purchaseOrderRequestDTO.getPurchaseOrderAttributes().stream()
                .map(dto -> {
                    PurchaseOrderAttributes attribute = new PurchaseOrderAttributes();
                    attribute.setMaterialCode(dto.getMaterialCode());
                    attribute.setMaterialDescription(dto.getMaterialDescription());
                    attribute.setQuantity(dto.getQuantity());
                    attribute.setRate(dto.getRate());
                    attribute.setCurrency(dto.getCurrency());
                    attribute.setExchangeRate(dto.getExchangeRate());
                    attribute.setGst(dto.getGst());
                    attribute.setDuties(dto.getDuties());
                    attribute.setFreightCharge(dto.getFreightCharge());
                    attribute.setBudgetCode(dto.getBudgetCode());
                    attribute.setPurchaseOrder(purchaseOrder);  // Associate with PurchaseOrder
                    return attribute;
                })
                .collect(Collectors.toList());

        purchaseOrder.setPurchaseOrderAttributes(purchaseOrderAttributes);
        purchaseOrderRepository.save(purchaseOrder);

        return mapToResponseDTO(purchaseOrder);
    }

    public PurchaseOrderResponseDTO updatePurchaseOrder(String poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Purchase order not found for the provided asset ID.")
                ));

        purchaseOrder.setTenderId(purchaseOrderRequestDTO.getTenderId());
        purchaseOrder.setIndentId(purchaseOrderRequestDTO.getIndentId());
        purchaseOrder.setWarranty(purchaseOrderRequestDTO.getWarranty());
        purchaseOrder.setConsignesAddress(purchaseOrderRequestDTO.getConsignesAddress());
        purchaseOrder.setBillingAddress(purchaseOrderRequestDTO.getBillingAddress());
        purchaseOrder.setDeliveryPeriod(purchaseOrderRequestDTO.getDeliveryPeriod());
        purchaseOrder.setIfLdClauseApplicable(purchaseOrderRequestDTO.getIfLdClauseApplicable());
        purchaseOrder.setIncoTerms(purchaseOrderRequestDTO.getIncoTerms());
        purchaseOrder.setPaymentTerms(purchaseOrderRequestDTO.getPaymentTerms());
        purchaseOrder.setVendorName(purchaseOrderRequestDTO.getVendorName());
        purchaseOrder.setVendorAddress(purchaseOrderRequestDTO.getVendorAddress());
        purchaseOrder.setApplicablePbgToBeSubmitted(purchaseOrderRequestDTO.getApplicablePbgToBeSubmitted());
        purchaseOrder.setTransporterAndFreightForWarderDetails(purchaseOrderRequestDTO.getTransporterAndFreightForWarderDetails());
        purchaseOrder.setVendorAccountNumber(purchaseOrderRequestDTO.getVendorAccountNumber());
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsZfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        purchaseOrder.setUpdatedBy(purchaseOrderRequestDTO.getUpdatedBy());
        purchaseOrder.setCreatedBy(purchaseOrderRequestDTO.getCreatedBy());
        List<PurchaseOrderAttributes> purchaseOrderAttributes = purchaseOrderRequestDTO.getPurchaseOrderAttributes().stream()
                .map(dto -> {
                    PurchaseOrderAttributes attribute = new PurchaseOrderAttributes();
                    attribute.setMaterialCode(dto.getMaterialCode());
                    attribute.setMaterialDescription(dto.getMaterialDescription());
                    attribute.setQuantity(dto.getQuantity());
                    attribute.setRate(dto.getRate());
                    attribute.setCurrency(dto.getCurrency());
                    attribute.setExchangeRate(dto.getExchangeRate());
                    attribute.setGst(dto.getGst());
                    attribute.setDuties(dto.getDuties());
                    attribute.setFreightCharge(dto.getFreightCharge());
                    attribute.setBudgetCode(dto.getBudgetCode());
                    attribute.setPurchaseOrder(purchaseOrder);  // Associate with PurchaseOrder
                    return attribute;
                })
                .collect(Collectors.toList());

        purchaseOrder.setPurchaseOrderAttributes(purchaseOrderAttributes);
        purchaseOrderRepository.save(purchaseOrder);

        return mapToResponseDTO(purchaseOrder);
    }

    public PurchaseOrderResponseDTO getPurchaseOrderById(String poId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Purchase order not found for the provided asset ID.")
                ));
        return mapToResponseDTO(purchaseOrder);
    }


    @Override
public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
    List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
    return purchaseOrders.stream().map(this::mapToResponseDTO).collect(Collectors.toList());  // Map each PurchaseOrder to its DTO
}


    public void deletePurchaseOrder(String poId) {

        PurchaseOrder purchaseOrder=purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Purchase order not found for the provided ID."
                        )
                ));
        try {
            purchaseOrderRepository.delete(purchaseOrder);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  po."
                    ),
                    ex
            );
        }

    }

    private PurchaseOrderResponseDTO mapToResponseDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDTO responseDTO = new PurchaseOrderResponseDTO();
        responseDTO.setPoId(purchaseOrder.getPoId());
        responseDTO.setTenderId(purchaseOrder.getTenderId());
        responseDTO.setIndentId(purchaseOrder.getIndentId());
        responseDTO.setWarranty(purchaseOrder.getWarranty());
        responseDTO.setConsignesAddress(purchaseOrder.getConsignesAddress());
        responseDTO.setBillingAddress(purchaseOrder.getBillingAddress());
        responseDTO.setDeliveryPeriod(purchaseOrder.getDeliveryPeriod());
        responseDTO.setIfLdClauseApplicable(purchaseOrder.getIfLdClauseApplicable());
        responseDTO.setIncoTerms(purchaseOrder.getIncoTerms());
        responseDTO.setPaymentTerms(purchaseOrder.getPaymentTerms());
        responseDTO.setVendorName(purchaseOrder.getVendorName());
        responseDTO.setVendorAddress(purchaseOrder.getVendorAddress());
        responseDTO.setApplicablePbgToBeSubmitted(purchaseOrder.getApplicablePbgToBeSubmitted());
        responseDTO.setTransporterAndFreightForWarderDetails(purchaseOrder.getTransporterAndFreightForWarderDetails());
        responseDTO.setVendorAccountNumber(purchaseOrder.getVendorAccountNumber());
        responseDTO.setVendorsZfscCode(purchaseOrder.getVendorsZfscCode());
        responseDTO.setVendorAccountName(purchaseOrder.getVendorAccountName());
        responseDTO.setCreatedBy(purchaseOrder.getCreatedBy());
        responseDTO.setUpdatedBy(purchaseOrder.getUpdatedBy());
        responseDTO.setCreatedDate(purchaseOrder.getCreatedDate());
        responseDTO.setUpdatedDate(purchaseOrder.getUpdatedDate());

        responseDTO.setPurchaseOrderAttributes(purchaseOrder.getPurchaseOrderAttributes().stream()
                .map(attribute -> {
                    PurchaseOrderAttributesResponseDTO attributeDTO = new PurchaseOrderAttributesResponseDTO();
                    attributeDTO.setMaterialCode(attribute.getMaterialCode());
                    attributeDTO.setMaterialDescription(attribute.getMaterialDescription());
                    attributeDTO.setQuantity(attribute.getQuantity());
                    attributeDTO.setRate(attribute.getRate());
                    attributeDTO.setCurrency(attribute.getCurrency());
                    attributeDTO.setExchangeRate(attribute.getExchangeRate());
                    attributeDTO.setGst(attribute.getGst());
                    attributeDTO.setDuties(attribute.getDuties());
                    attributeDTO.setFreightCharge(attribute.getFreightCharge());
                    attributeDTO.setBudgetCode(attribute.getBudgetCode());
                    return attributeDTO;
                })
                .collect(Collectors.toList()));

        return responseDTO;
    }


}
