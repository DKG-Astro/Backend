package com.astro.dto.workflow.InventoryModule.GiDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiMaterialDtlDto {
    private String materialCode;
    private String materialDesc;
    private String makeNo;
    private String modelNo;
    private String serialNo;
    private String uomId;
    private String installationReportBase64;
    private BigDecimal receivedQuantity;
    private BigDecimal acceptedQuantity;
    private BigDecimal rejectedQuantity;
}