package com.astro.dto.workflow;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class WorkflowTransitionDto {

    private Integer workflowTransitionId;
    private Integer workflowId;
    private String workflowName;
    private Integer transitionId;
    private Integer requestId;
    private Integer createdBy;
    private Integer modifiedBy;
    private String status;
    private String nextAction;
    private Integer transitionOrder;
    private Integer transitionSubOrder;
    private Date modificationDate;
    private Date createdDate;
}
