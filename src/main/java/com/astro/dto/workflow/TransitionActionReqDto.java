package com.astro.dto.workflow;

import lombok.Data;

@Data
public class TransitionActionReqDto {

    private Integer workflowTransitionId;
    private Integer requestId;
    private Integer actionBy;
    private String action;
    private String userRole;
    private String remarks;
}
