package com.astro.service;


import com.astro.dto.workflow.purchaseOrder.PurchaseOrderDto;
import com.astro.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto poDto);

    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrderDto poDto);

    public List<PurchaseOrder> getAllPurchaseOrders();

    public PurchaseOrder getPurchaseOrderById(Long poId);

    public void deletePurchaseOrder(Long poId);

}
