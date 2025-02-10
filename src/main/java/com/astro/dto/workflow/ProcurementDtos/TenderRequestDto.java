package com.astro.dto.workflow.ProcurementDtos;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class TenderRequestDto {

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

    private MultipartFile uploadTenderDocuments;
    private String singleAndMultipleVendors;

    private MultipartFile uploadGeneralTermsAndConditions;

    private MultipartFile uploadSpecificTermsAndConditions;
    private String preBidDisscussions;
    private String updatedBy;
    private String createdBy;
    private List<String> indentIds; // Updated




}
