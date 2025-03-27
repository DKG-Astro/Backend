package com.astro.dto.workflow.InventoryModule;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AssetDisposalDetailDto {
    private Integer assetId;
    private String assetDesc;
    private BigDecimal disposalQuantity;
    private String disposalCategory;
    private String disposalMode;
    private String salesNoteFilename;
    private Integer locatorId;
}
