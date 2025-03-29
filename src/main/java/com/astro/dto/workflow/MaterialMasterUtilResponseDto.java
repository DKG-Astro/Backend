package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialMasterUtilResponseDto {

    private String materialCode;
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
    private String approvalStatus;
    private String comments;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
