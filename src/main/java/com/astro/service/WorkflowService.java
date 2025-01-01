package com.astro.service;

import com.astro.dto.workflow.TransitionDto;
import com.astro.dto.workflow.WorkflowDto;
import com.astro.dto.workflow.WorkflowTransitionDto;

import java.util.List;

public interface WorkflowService {

    public List<WorkflowTransitionDto> workflowTransitionsByWorkflowId(Integer workflowId);
    public WorkflowDto workflowByWorkflowName(String workflowName);
    public TransitionDto nextTransition(String workflowId, String currentRole, String tranConditionKey, String tranConditionValue);
}
