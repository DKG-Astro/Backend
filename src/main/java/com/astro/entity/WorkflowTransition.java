package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "WORKFLOW_TRANSITION")
@Data
public class WorkflowTransition {

    @Id
    @Column(name = "WORKFLOWTRANSITIONID")
    private Integer workflowTransitionId;

    @Column(name = "WORKFLOWID")
    private Integer workflowId;

    @Column(name = "WORKFLOWNAME")
    private String workflowName;

    @Column(name = "TRANSITIONID")
    private Integer transitionId;

    @Column(name = "REQUESTID")
    private Integer requestId;

    @Column(name = "CREATEDBY")
    private Integer createdBy;

    @Column(name = "MODIFIEDBY")
    private Integer modifiedBy;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TRANSITIONORDER")
    private Integer transitionOrder;

    @Column(name = "TRANSITIONSUBORDER")
    private Integer transitionSubOrder;

    @Column(name = "MODIFICATIONDATE")
    private Date modificationDate;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
