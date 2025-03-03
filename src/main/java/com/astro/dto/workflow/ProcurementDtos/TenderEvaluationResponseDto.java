package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TenderEvaluationResponseDto {
    private String tenderId;
    private String uploadQualifiedVendorsFileName;
    private String uploadTechnicallyQualifiedVendorsFileName;
    private String uploadCommeriallyQualifiedVendorsFileName;
    private String formationOfTechnoCommerialComitee;
    private String responseFileName;
    private String responseForTechnicallyQualifiedVendorsFileName;
    private String responseForCommeriallyQualifiedVendorsFileName;
    private String fileType;
    private String updatedBy;
    private Integer createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
