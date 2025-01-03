package com.astro.service;

import com.astro.dto.workflow.TransitionDto;
import com.astro.dto.workflow.WorkflowDto;
import com.astro.dto.workflow.WorkflowTransitionDto;

import java.util.List;

public interface WorkflowService {

    public WorkflowDto workflowByWorkflowName(String workflowName);
    public List<TransitionDto> transitionsByWorkflowId(Integer workflowId);
    public TransitionDto transitionsByWorkflowIdAndOrder(Integer workflowId, Integer order);
    public WorkflowTransitionDto initiateWorkflow(Integer requestId, String workflowName, Integer createdBy);
    public List<WorkflowTransitionDto> workflowTransitionHistory(Integer workflowId, Integer createdBy, Integer requestId, String roleName);
    public TransitionDto nextTransition(String workflowId, String currentRole, String tranConditionKey, String tranConditionValue);
}
