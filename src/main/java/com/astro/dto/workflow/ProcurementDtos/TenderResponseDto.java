package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TenderResponseDto {
    private String tenderId;
    private String titleOfTender;
    private String openingDate;
    private String closingDate;
   // private String indentId;
    private String indentMaterials;
    private String modeOfProcurement;
    private String bidType;
    private String lastDateOfSubmission;
    private String applicableTaxes;
    private String consignesAndBillinngAddress;
    private String incoTerms;
    private String paymentTerms;
    private String ldClause;
    private String applicablePerformance;
    private Boolean bidSecurityDeclaration;
    private Boolean mllStatusDeclaration;
    private String uploadTenderDocuments;
    private String singleAndMultipleVendors;
    private String uploadGeneralTermsAndConditions;
    private String uploadSpecificTermsAndConditions;
    private String preBidDisscussions;
    private BigDecimal totalTenderValue;
    private String updatedBy;
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

   // private List<String> indentIds;
}
