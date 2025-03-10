package com.astro.dto.workflow.ProcurementDtos.purchaseOrder;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponseDTO {

    private String poId;
    private String tenderId;
    private String indentId;
    private BigDecimal warranty;
    private String consignesAddress;
    private String billingAddress;
    private BigDecimal deliveryPeriod;
    private Boolean ifLdClauseApplicable;
    private String incoTerms;
    private String paymentTerms;
    private String vendorName;
    private String vendorAddress;
    private String applicablePbgToBeSubmitted;
    private String transporterAndFreightForWarderDetails;
    private String vendorAccountNumber;
    private String vendorsIfscCode;
    private String vendorAccountName;
    private BigDecimal totalValue;
    private String projectName;
    private BigDecimal projectLimit;
    private List<PurchaseOrderAttributesResponseDTO> purchaseOrderAttributes;
    private Integer createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
