package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class QueueResponse {


    private String indentorName;
    private String projectName;
    private BigDecimal amount;
    private String budgetName;
    private String indentTitle;
    private String modeOfProcurement;
    private String consignee;


    private Integer workflowTransitionId;
    private Integer workflowId;
    private String workflowName;
    private Integer transitionId;
    private String requestId;
    private Integer createdBy;
   // private String createdRole;
    private Integer modifiedBy;
   // private String modifiedRole;
    private String status;
    private String nextAction;
    private String action;
    private String remarks;
   // private Integer nextActionId;
   // private String nextActionRole;
    private Integer transitionOrder;
    private Integer transitionSubOrder;
    private String currentRole;
    private String nextRole;
    private Integer workflowSequence;
    private Date modificationDate;
    private Date createdDate;


}
