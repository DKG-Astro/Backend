package com.astro.dto.workflow.purchaseOrder;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderAttributesResponseDTO {


    private Long id;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal rate;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal gst;
    private BigDecimal duties;
    private BigDecimal freightCharge;
    private String budgetCode;
}