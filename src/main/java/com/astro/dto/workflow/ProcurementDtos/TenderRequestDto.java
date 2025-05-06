package com.astro.dto.workflow.ProcurementDtos;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.List;


@Data
public class TenderRequestDto {

    //  private String tenderId;
    private String titleOfTender;
    private String openingDate;
    private String closingDate;
    // private String indentId;
    private String indentMaterials;
    private String modeOfProcurement;
    private String bidType;
    private String lastDateOfSubmission;
    private String applicableTaxes;
    //  private String consignesAndBillinngAddress;
    private String incoTerms;
    private String paymentTerms;
    private String ldClause;
    // private String applicablePerformance;
    private String performanceAndWarrantySecurity;
    private Boolean bidSecurityDeclaration;
    private Boolean mllStatusDeclaration;

    //private MultipartFile uploadTenderDocuments;
    private String singleAndMultipleVendors;
    private List<String> uploadTenderDocuments;
    private List<String> uploadGeneralTermsAndConditions;
    private String billinngAddress;
    private String consignes;
    private String fileType;

    //   private MultipartFile uploadGeneralTermsAndConditions;

    private List<String> uploadSpecificTermsAndConditions;
    private String preBidDisscussions;
    private String updatedBy;
    private Integer createdBy;
    private List<String> indentIds; // Updated


}
