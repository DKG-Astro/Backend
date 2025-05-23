package com.astro.dto.workflow;

import com.astro.util.Base64ToByteArrayConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialMasterRequestDto {

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
    @JsonDeserialize(converter = Base64ToByteArrayConverter.class)
    private byte[] uploadImage;
    private Boolean indigenousOrImported;

}
