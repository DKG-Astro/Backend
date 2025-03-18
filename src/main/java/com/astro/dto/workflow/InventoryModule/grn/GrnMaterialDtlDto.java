package com.astro.dto.workflow.InventoryModule.grn;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GrnMaterialDtlDto {
    private Integer assetId;
    private String assetDesc;
    private String uomId;
    private Integer locatorId;
    private BigDecimal bookValue;
    private BigDecimal receivedQuantity;
    private BigDecimal acceptedQuantity;
    private BigDecimal depriciationRate;
}
