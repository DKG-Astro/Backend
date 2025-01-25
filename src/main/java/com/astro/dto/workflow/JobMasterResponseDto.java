package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobMasterResponseDto {

    private String jobCode;
    private String category;
    private String jobDescription;
    private String assetId;
    private String uom;
    private BigDecimal value;
    private String updatedBy;
    private String createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


}
