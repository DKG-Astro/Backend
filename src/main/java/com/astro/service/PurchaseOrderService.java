package com.astro.service;



import com.astro.dto.workflow.ProcurementDtos.ProcurementActivityReportResponse;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.*;
import com.astro.dto.workflow.VendorContractReportDTO;

import java.util.List;

public interface PurchaseOrderService {

    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO);
    public PurchaseOrderResponseDTO updatePurchaseOrder(String poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO);



   public List<PurchaseOrderResponseDTO > getAllPurchaseOrders();

    public poWithTenderAndIndentResponseDTO getPurchaseOrderById(String poId);
    public void deletePurchaseOrder(String poId);

    List<VendorContractReportDTO> getVendorContractDetails(String startDate, String endDate);

    List<ProcurementActivityReportResponse> getProcurementActivityReport(String startDate, String endDate);

    public List<ApprovedPoListReportDto> getApprovedPoReport(String startDate, String endDate);
    public List<pendingPoReportDto> getPendingPoReport(String startDate, String endDate);
}
