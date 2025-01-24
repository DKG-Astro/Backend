package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;

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
    private BigDecimal stockLevelsMin;
    private BigDecimal stockLevelsMax;
    private BigDecimal reOrderLevel;
    private String conditionOfGoods;
    private String shelfLife;
    private byte[] uploadImage;
    private Boolean indigenousOrImported;
}
