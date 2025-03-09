package com.astro.repository;

import com.astro.entity.WorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowTransitionRepository extends JpaRepository<WorkflowTransition, Integer> {
    List<WorkflowTransition> findByWorkflowId(Integer workflowId);
    WorkflowTransition findByWorkflowIdAndTransitionOrder(Integer workflowId, Integer order);
    List<WorkflowTransition> findByWorkflowIdOrCreatedByOrRequestIdOrTransitionId(Integer workflowId, Integer createdBy, Integer requestId, Integer nextTransitionId);
    WorkflowTransition findByWorkflowIdAndCreatedByAndRequestId(Integer workflowId, Integer createdBy, String requestId);
    List<WorkflowTransition> findByWorkflowIdOrCreatedByOrRequestId(Integer workflowId, Integer createdBy, Integer requestId);
    List<WorkflowTransition> findByWorkflowIdAndCurrentRole(Integer workflowId, String roleName);
    List<WorkflowTransition> findByRequestId(String requestId);
    WorkflowTransition findByWorkflowTransitionIdAndRequestId(Integer workflowTransitionId, String requestId);
    List<WorkflowTransition> findByNextActionAndNextRole(String pendingType, String roleName);
    List<WorkflowTransition> findByNextRole(String roleName);
    List<WorkflowTransition> findByWorkflowIdAndRequestId(Integer workflowId, String requestId);
    List<WorkflowTransition> findByWorkflowIdAndRequestIdAndCurrentRole(Integer workflowId, String requestId, String assignmentRole);
    List<WorkflowTransition> findByWorkflowIdAndRequestIdAndNextRole(Integer workflowId, String requestId, String assignmentRole);
    List<WorkflowTransition> findByModifiedBy(Integer modifiedBy);

    @Query("SELECT wt.requestId FROM WorkflowTransition wt WHERE wt.workflowName = 'Indent Workflow' AND wt.status = 'Completed' AND wt.nextAction IS NULL AND wt.requestId NOT IN (SELECT i.indentId FROM IndentId i WHERE i.tenderRequest IS NOT NULL)")
    List<String> findApprovedIndentRequestIds();

    //@Query("SELECT wt.requestId FROM WorkflowTransition wt WHERE wt.workflowName = 'Tender Approver Workflow' AND wt.status = 'Completed' AND wt.nextAction IS NULL")
    @Query("SELECT wt.requestId FROM WorkflowTransition wt WHERE wt.workflowName = 'Tender Approver Workflow' AND wt.status = 'Completed' AND wt.nextAction IS NULL AND wt.requestId NOT IN (SELECT swt.requestId FROM SubWorkflowTransition swt) AND wt.requestId NOT IN (SELECT po.tenderId FROM PurchaseOrder po) AND wt.requestId NOT IN (SELECT so.tenderId FROM ServiceOrder so)")
    List<String> findApprovedTenderRequestIds();

    @Query("Select wt.requestId from WorkflowTransition wt WHERE wt.workflowName = 'Tender Evaluator Workflow' AND wt.status = 'Completed' AND wt.nextAction IS NULL AND wt.requestId NOT IN (SELECT po.tenderId FROM PurchaseOrder po) AND wt.requestId NOT IN (SELECT so.tenderId FROM ServiceOrder so)")
    List<String> findApprovedTenderIdsForPOANDSO();

}
