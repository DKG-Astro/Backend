package com.astro.service.impl;

//import com.astro.dto.purchaseOrder.PurchaseOrderDto;
import com.astro.dto.workflow.purchaseOrder.PurchaseOrderDto;
import com.astro.entity.PurchaseOrder;
import com.astro.repository.PurchaseOrderRepo;
import com.astro.service.PurchaseOrderService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderImpl implements PurchaseOrderService {


    @Autowired
    private PurchaseOrderRepo poRepository;
    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto poDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setTenderRequests(poDto.getTenderRequests());
        purchaseOrder.setCorrespondingIndents(poDto.getCorrespondingIndents());
        purchaseOrder.setMaterialDescription(poDto.getMaterialDescription());
        purchaseOrder.setQuantity(poDto.getQuantity());
        purchaseOrder.setUnitRate(poDto.getUnitRate());
        purchaseOrder.setCurrency(poDto.getCurrency());
        purchaseOrder.setExchangeRate(poDto.getExchangeRate());
        purchaseOrder.setGstPercentage(poDto.getGstPercentage());
        purchaseOrder.setDutiesPercentage(poDto.getDutiesPercentage());
        purchaseOrder.setFreightCharges(poDto.getFreightCharges());
        // Convert the delivery period string to a date
        String deliveryPeriod = poDto.getDeliveryPeriod();  // Assuming it's a string like "01/02/2025"
        purchaseOrder.setDeliveryPeriod(CommonUtils.convertStringToDateObject(deliveryPeriod));
        purchaseOrder.setWarranty(poDto.getWarranty());
        purchaseOrder.setConsigneeAddress(poDto.getConsigneeAddress());
        purchaseOrder.setAdditionalTermsAndConditions(poDto.getAdditionalTermsAndConditions());
        purchaseOrder.setUpdatedBy(poDto.getUpdatedBy());
        return poRepository.save(purchaseOrder);
    }

    @Override
    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrderDto poDto) {
        PurchaseOrder existingPO = poRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));

        // Update the fields
        existingPO.setTenderRequests(poDto.getTenderRequests());
        existingPO.setCorrespondingIndents(poDto.getCorrespondingIndents());
        existingPO.setMaterialDescription(poDto.getMaterialDescription());
        existingPO.setQuantity(poDto.getQuantity());
        existingPO.setUnitRate(poDto.getUnitRate());
        existingPO.setCurrency(poDto.getCurrency());
        existingPO.setExchangeRate(poDto.getExchangeRate());
        existingPO.setGstPercentage(poDto.getGstPercentage());
        existingPO.setDutiesPercentage(poDto.getDutiesPercentage());
        existingPO.setFreightCharges(poDto.getFreightCharges());
        String deliveryPeriod = poDto.getDeliveryPeriod();
        existingPO.setDeliveryPeriod(CommonUtils.convertStringToDateObject(deliveryPeriod));
        existingPO.setWarranty(poDto.getWarranty());
        existingPO.setConsigneeAddress(poDto.getConsigneeAddress());
        existingPO.setAdditionalTermsAndConditions(poDto.getAdditionalTermsAndConditions());
        existingPO.setUpdatedBy(poDto.getUpdatedBy());
        return poRepository.save(existingPO);
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrders() {

        return poRepository.findAll();
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(Long poId) {
        return poRepository.findById(poId).orElseThrow(() -> new RuntimeException("PO not found!"));
    }

    @Override
    public void deletePurchaseOrder(Long poId) {

        poRepository.deleteById(poId);
    }
}
