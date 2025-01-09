package com.astro.service.impl;



import com.astro.dto.workflow.purchaseOrder.PurchaseOrderAttributesResponseDTO;

import com.astro.dto.workflow.purchaseOrder.PurchaseOrderRequestDTO;
import com.astro.dto.workflow.purchaseOrder.PurchaseOrderResponseDTO;
import com.astro.entity.IndentCreation;
import com.astro.entity.PurchaseOrder;
import com.astro.entity.PurchaseOrderAttributes;
import com.astro.entity.ServiceOrder;
import com.astro.repository.PurchaseOrderAttributesRepository;

import com.astro.repository.PurchaseOrderRepository;

import com.astro.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderAttributesRepository purchaseOrderAttributesRepository;

    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setTenderId(purchaseOrderRequestDTO.getTenderId());
        purchaseOrder.setIndentId(purchaseOrderRequestDTO.getIndentId());
        purchaseOrder.setWarranty(purchaseOrderRequestDTO.getWarranty());
        purchaseOrder.setConsignesAddress(purchaseOrderRequestDTO.getConsignesAddress());
        purchaseOrder.setBillingAddress(purchaseOrderRequestDTO.getBillingAddress());
        purchaseOrder.setDeliveryPeriod(purchaseOrderRequestDTO.getDeliveryPeriod());
        purchaseOrder.setIfLdClauseApplicable(purchaseOrderRequestDTO.getIfLdClauseApplicable());
        purchaseOrder.setIncoterms(purchaseOrderRequestDTO.getIncoterms());
        purchaseOrder.setPaymentterms(purchaseOrderRequestDTO.getPaymentterms());
        purchaseOrder.setVendorName(purchaseOrderRequestDTO.getVendorName());
        purchaseOrder.setVendorAddress(purchaseOrderRequestDTO.getVendorAddress());
        purchaseOrder.setApplicablePbgToBeSubmitted(purchaseOrderRequestDTO.getApplicablePbgToBeSubmitted());
        purchaseOrder.setTransposterAndFreightForWarderDetails(purchaseOrderRequestDTO.getTransposterAndFreightForWarderDetails());
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

    public PurchaseOrderResponseDTO updatePurchaseOrder(Long poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId).orElseThrow(() -> new RuntimeException("Purchase Order not found"));

        purchaseOrder.setTenderId(purchaseOrderRequestDTO.getTenderId());
        purchaseOrder.setIndentId(purchaseOrderRequestDTO.getIndentId());
        purchaseOrder.setWarranty(purchaseOrderRequestDTO.getWarranty());
        purchaseOrder.setConsignesAddress(purchaseOrderRequestDTO.getConsignesAddress());
        purchaseOrder.setBillingAddress(purchaseOrderRequestDTO.getBillingAddress());
        purchaseOrder.setDeliveryPeriod(purchaseOrderRequestDTO.getDeliveryPeriod());
        purchaseOrder.setIfLdClauseApplicable(purchaseOrderRequestDTO.getIfLdClauseApplicable());
        purchaseOrder.setIncoterms(purchaseOrderRequestDTO.getIncoterms());
        purchaseOrder.setPaymentterms(purchaseOrderRequestDTO.getPaymentterms());
        purchaseOrder.setVendorName(purchaseOrderRequestDTO.getVendorName());
        purchaseOrder.setVendorAddress(purchaseOrderRequestDTO.getVendorAddress());
        purchaseOrder.setApplicablePbgToBeSubmitted(purchaseOrderRequestDTO.getApplicablePbgToBeSubmitted());
        purchaseOrder.setTransposterAndFreightForWarderDetails(purchaseOrderRequestDTO.getTransposterAndFreightForWarderDetails());
        purchaseOrder.setVendorAccountNumber(purchaseOrderRequestDTO.getVendorAccountNumber());
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsZfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        purchaseOrder.setUpdatedBy(purchaseOrderRequestDTO.getUpdatedBy());
        purchaseOrder.setCreatedBy(purchaseOrder.getCreatedBy());
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

    public PurchaseOrderResponseDTO getPurchaseOrderById(Long poId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
        return mapToResponseDTO(purchaseOrder);  // Return the mapped response DTO
    }


    @Override
public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
    List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
    return purchaseOrders.stream().map(this::mapToResponseDTO).collect(Collectors.toList());  // Map each PurchaseOrder to its DTO
}


    public void deletePurchaseOrder(Long poId) {
      purchaseOrderRepository.deleteById(poId);
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
        responseDTO.setIncoterms(purchaseOrder.getIncoterms());
        responseDTO.setPaymentterms(purchaseOrder.getPaymentterms());
        responseDTO.setVendorName(purchaseOrder.getVendorName());
        responseDTO.setVendorAddress(purchaseOrder.getVendorAddress());
        responseDTO.setApplicablePbgToBeSubmitted(purchaseOrder.getApplicablePbgToBeSubmitted());
        responseDTO.setTransposterAndFreightForWarderDetails(purchaseOrder.getTransposterAndFreightForWarderDetails());
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
                    attributeDTO.setId(attribute.getId());
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
