package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaterialMasterResponseDto {

    private String materialCode;
    private String category;
    private String subCategory;
    private String description;
    private String uom;
    private String modeOfProcurement;
    private String endOfLife;
    private BigDecimal depreciationRate;
    private BigDecimal stockLevels;
   // private BigDecimal stockLevelsMax;
   // private BigDecimal reOrderLevel;
    private String conditionOfGoods;
    private String shelfLife;
    private String uploadImageFileName;
    private Boolean indigenousOrImported;
    private BigDecimal estimatedPriceWithCcy;
    private List<String> vendorNames;
    private Integer createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
