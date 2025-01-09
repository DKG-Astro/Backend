package com.astro.service;



import com.astro.dto.workflow.purchaseOrder.PurchaseOrderRequestDTO;
import com.astro.dto.workflow.purchaseOrder.PurchaseOrderResponseDTO;
import com.astro.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO);
    public PurchaseOrderResponseDTO updatePurchaseOrder(Long poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO);



   public List<PurchaseOrderResponseDTO > getAllPurchaseOrders();

    public PurchaseOrderResponseDTO getPurchaseOrderById(Long poId);
    public void deletePurchaseOrder(Long poId);

}
