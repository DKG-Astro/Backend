package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TenderResponseDto {

    private Long id;
    private String titleOfTender;
    private String  openingDate;
    private String closingDate;
    private String indentId;
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

    private byte[] uploadTenderDocuments;
    private String singleAndMultipleVendors;

    private byte[] uploadGeneralTermsAndConditions;

    private byte[] uploadSpecificTermsAndConditions;
    private String preBidDisscussions;

    private String updatedBy;
    private String createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


}
