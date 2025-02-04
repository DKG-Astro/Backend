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

    // newly added fields to hold filenames
    private String uploadingPriorApprovalsFileName;
    private String uploadTenderDocumentsFileName;
    private String uploadGOIOrRFPFileName;
    private String uploadPACOrBrandPACFileName;

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
    private List<MaterialDetailsResponseDTO> materialDetails;
    private String createdBy;
    private String updatedBy;

}
