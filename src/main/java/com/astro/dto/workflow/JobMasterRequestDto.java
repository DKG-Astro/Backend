package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobMasterRequestDto {

    private String jobCode;
    private String category;
    private String jobDescription;
    private String assetId;
    private String uom;
    private BigDecimal value;
    private String updatedBy;
    private String createdBy;
}
