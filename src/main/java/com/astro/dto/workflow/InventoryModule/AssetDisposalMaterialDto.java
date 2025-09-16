package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetDisposalMaterialDto {
    private Long disposalDetailId;
    private Integer assetId;
    private String assetDesc;
    private BigDecimal disposalQuantity;
    private String disposalCategory;
    private String disposalMode;
    private String salesNoteFilename;
    private Integer locatorId;
    private Long ohqId;
    private BigDecimal bookValue;
    private BigDecimal depriciationRate;
    private BigDecimal unitPrice;
    private String custodianId;
    private BigDecimal poValue;
    private String reasonForDisposal;
}
