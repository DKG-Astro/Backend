package com.astro.dto.workflow.ProcurementDtos.IndentDto.SreviceOrderDto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceOrderResponseDTO {

    private Long id;
    private String tenderId;
    private String consignesAddress;
    private String billingAddress;
    private BigDecimal jobCompletionPeriod;
    private Boolean ifLdClauseApplicable;
    private String incoTerms;
    private String paymentTerms;
    private String vendorName;
    private String vendorAddress;
    private String applicablePBGToBeSubmitted;
    private String vendorsAccountNo;
    private String vendorsZRSCCode;
    private String vendorsAccountName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ServiceOrderMaterialResponseDTO> materials;
    private String createdBy;
    private String updatedBy;

}
