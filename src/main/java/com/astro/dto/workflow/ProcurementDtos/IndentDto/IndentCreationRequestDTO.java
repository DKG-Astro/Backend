package com.astro.dto.workflow.ProcurementDtos.IndentDto;

import com.astro.util.Base64ToByteArrayConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
public class IndentCreationRequestDTO {

    private String indentorName;

    private String indentId;

    private String indentorMobileNo;

    private String indentorEmailAddress;
    private String consignesLocation;

    private MultipartFile uploadingPriorApprovals;
    private MultipartFile uploadTenderDocuments;
    private MultipartFile uploadGOIOrRFP;
    private MultipartFile uploadPACOrBrandPAC;

    private String projectName;
    private Boolean isPreBidMeetingRequired;
    private String preBidMeetingDate;
    private String preBidMeetingVenue;
    private Boolean isItARateContractIndent;
    private BigDecimal estimatedRate;
    private BigDecimal periodOfContract;
    private String singleAndMultipleJob;
    private List<MaterialDetailsRequestDTO> materialDetails;
    private String updatedBy;
    private Integer createdBy;

}
