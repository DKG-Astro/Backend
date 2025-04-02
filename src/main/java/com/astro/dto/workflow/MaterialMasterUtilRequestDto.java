package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialMasterUtilRequestDto {

    private String category;
    private String subCategory;
    private String description;
    private String uom;
    private BigDecimal estimatedPriceWithCcy;
    private String uploadImageFileName;
    private Boolean indigenousOrImported;
    private BigDecimal unitPrice;
    private String currency;
    private Integer createdBy;
    private String updatedBy;
}
