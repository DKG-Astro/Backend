package com.astro.dto.workflow.ProcurementDtos.IndentDto;


import lombok.Data;

import java.math.BigDecimal;

import java.util.List;

@Data
public class IndentCreationResponseDTO {

    private String indentorName;
    private String indentId;
    private String indentorMobileNo;
    private String indentorEmailAddress;
    private String consignesLocation;
    private String uploadingPriorApprovalsFileName;
    private String technicalSpecificationsFileName;
    private String draftEOIOrRFPFileName;
    private String uploadPACOrBrandPACFileName;
    private String fileType;
   // private String uploadingPriorApprovalsFile;
   // private String uploadTenderDocumentsFile;
   // private String uploadGOIOrRFPFile;
   // private String uploadPACOrBrandPACFile;

    private String projectName;
    private Boolean isPreBidMeetingRequired;
    private String preBidMeetingDate;
    private String preBidMeetingVenue;
    private Boolean isItARateContractIndent;
    private BigDecimal estimatedRate;
    private BigDecimal periodOfContract;
    private String singleAndMultipleJob;
    private String materialCategory;
    private BigDecimal totalPriceOfAllMaterials;
    private BigDecimal projectLimit;
    private List<MaterialDetailsResponseDTO> materialDetails;
    private String brandAndModel;
    private String justification;
    private Boolean brandPac;
    private String quarter;
    private String purpose;
    private String reason;
    private String modelNumber;
    private String serialNumber;
    private String dateOfPurchase;
    private String proprietaryJustification;
    private String proprietaryAndLimitedDeclaration;
    private Boolean buyBack;
    private String uploadBuyBackFileNames;
    private Integer createdBy;
    private String updatedBy;

}
