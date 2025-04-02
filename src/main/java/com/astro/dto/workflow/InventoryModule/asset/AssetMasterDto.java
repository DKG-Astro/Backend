package com.astro.dto.workflow.InventoryModule.asset;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AssetMasterDto {
    private Integer assetId;
    private String materialCode;
    private String materialDesc;
    private String assetDesc;
    private String makeNo;
    private String serialNo;
    private String modelNo;
    private BigDecimal initQuantity;
    private BigDecimal unitPrice;
    private String uomId;
    private BigDecimal depriciationRate;
    private LocalDate endOfLife;
    private BigDecimal stockLevels;
    private String conditionOfGoods;
    private String shelfLife;
    private String componentName;
    private Integer componentId;
    private LocalDateTime createDate;
    private Integer createdBy;
    private LocalDateTime updatedDate;
    private Integer updatedBy;
}