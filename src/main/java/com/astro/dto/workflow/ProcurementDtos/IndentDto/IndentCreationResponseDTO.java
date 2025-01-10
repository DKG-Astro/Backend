package com.astro.dto.workflow.ProcurementDtos.IndentDto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class IndentCreationResponseDTO {


    private Long id;
    private String indentorName;
    private String indentorId;
    private String indentorMobileNo;
    private String indentorEmailAddress;
    private String consignesLocation;
    private String uploadingPriorApprovals;
    private String projectName;
    private Boolean isPreBidMeetingRequired;
    private LocalDate preBidMeetingDate;
    private String preBidMeetingVenue;
    private Boolean isItARateContractIndent;
    private BigDecimal estimatedRate;
    private BigDecimal periodOfContract;
    private String singleAndMultipleJob;
    private List<MaterialDetailsResponseDTO> materialDetails;
    private String createdBy;
    private String updatedBy;

}
