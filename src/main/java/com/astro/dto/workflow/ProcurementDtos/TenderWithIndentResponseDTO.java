package com.astro.dto.workflow.ProcurementDtos;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TenderWithIndentResponseDTO {

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
    private String updatedBy;
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<IndentCreationResponseDTO> indentResponseDTO;
    private BigDecimal totalTenderValue;
 //   private List<IndentIdDto> indentIds;



}
