package com.astro.dto.workflow.SreviceOrderDto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceOrderRequestDTO {

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
    private List<ServiceOrderMaterialRequestDTO> materials;
    private String createdBy;
    private String updatedBy;

}
