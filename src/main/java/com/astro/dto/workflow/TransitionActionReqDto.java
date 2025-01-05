package com.astro.dto.workflow;

import lombok.Data;

@Data
public class TransitionActionReqDto {

    private Integer workflowTransitionId;
    private Integer actionBy;
    private String action;
    private String userRole;
    private String tranConditionKey;
    private String tranConditionValue;
    private String remarks;
}
