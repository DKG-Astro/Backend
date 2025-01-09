package com.astro.dto.workflow.purchaseOrder;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponseDTO {

    private Long poId;
    private String tenderId;
    private String indentId;
    private BigDecimal warranty;
    private String consignesAddress;
    private String billingAddress;
    private BigDecimal deliveryPeriod;
    private Boolean ifLdClauseApplicable;
    private String incoterms;
    private String paymentterms;
    private String vendorName;
    private String vendorAddress;
    private String applicablePbgToBeSubmitted;
    private String transposterAndFreightForWarderDetails;
    private String vendorAccountNumber;
    private String vendorsZfscCode;
    private String vendorAccountName;
    private List<PurchaseOrderAttributesResponseDTO> purchaseOrderAttributes;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
