package com.astro.service;

import com.astro.dto.workflow.TransitionActionReqDto;
import com.astro.dto.workflow.TransitionDto;
import com.astro.dto.workflow.WorkflowDto;
import com.astro.dto.workflow.WorkflowTransitionDto;

import java.util.List;

public interface WorkflowService {

    public WorkflowDto workflowByWorkflowName(String workflowName);
    public List<TransitionDto> transitionsByWorkflowId(Integer workflowId);
    public TransitionDto transitionsByWorkflowIdAndOrder(Integer workflowId, Integer order, Integer subOrder);
    public WorkflowTransitionDto initiateWorkflow(String requestId, String workflowName, Integer createdBy);
    public List<WorkflowTransitionDto> workflowTransitionHistory(String requestId);
    public List<WorkflowTransitionDto> allWorkflowTransition(String roleName);
    public List<WorkflowTransitionDto> allPendingWorkflowTransition(String roleName);
    public List<String> allPreviousRoleWorkflowTransition(Integer workflowId, String requestId);
    public TransitionDto nextTransition(Integer workflowId, String workflowName, String currentRole, String requestId);
    public WorkflowTransitionDto performTransitionAction(TransitionActionReqDto transitionActionReqDto);
    public WorkflowTransitionDto submitWorkflow(Integer workflowTransitionId, Integer actionBy, String remarks);
    public List<WorkflowTransitionDto> approvedWorkflowTransition(Integer modifiedBy);
}
