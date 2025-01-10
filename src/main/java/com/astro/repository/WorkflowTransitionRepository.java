package com.astro.repository;

import com.astro.entity.WorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowTransitionRepository extends JpaRepository<WorkflowTransition, Integer> {
    List<WorkflowTransition> findByWorkflowId(Integer workflowId);
    WorkflowTransition findByWorkflowIdAndTransitionOrder(Integer workflowId, Integer order);
    List<WorkflowTransition> findByWorkflowIdOrCreatedByOrRequestIdOrTransitionId(Integer workflowId, Integer createdBy, Integer requestId, Integer nextTransitionId);
    WorkflowTransition findByWorkflowIdAndCreatedByAndRequestId(Integer workflowId, Integer createdBy, Integer requestId);
    List<WorkflowTransition> findByWorkflowIdOrCreatedByOrRequestId(Integer workflowId, Integer createdBy, Integer requestId);
    List<WorkflowTransition> findByWorkflowIdAndCurrentRole(Integer workflowId, String roleName);
    List<WorkflowTransition> findByRequestId(Integer requestId);
    WorkflowTransition findByWorkflowTransitionIdAndRequestId(Integer workflowTransitionId, Integer requestId);
    List<WorkflowTransition> findByNextActionAndNextRole(String pendingType, String roleName);
    List<WorkflowTransition> findByNextRole(String roleName);
}
