package com.astro.dto.workflow.purchaseOrder;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderDto {

    private String tenderRequests;
    private String correspondingIndents;
    private String materialDescription;
    private Integer quantity;
    private BigDecimal unitRate;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal gstPercentage;
    private BigDecimal dutiesPercentage;
    private BigDecimal freightCharges;
    private String deliveryPeriod;
    private String warranty;
    private String consigneeAddress;
    private String additionalTermsAndConditions;
    private String updatedBy;

}
