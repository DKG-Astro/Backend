package com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceOrderRequestDTO {

   // private String soId;
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
  //private BigDecimal totalValueOfSo;
    private String projectName;
    private List<ServiceOrderMaterialRequestDTO> materials;
    private Integer createdBy;
    private String updatedBy;

}
