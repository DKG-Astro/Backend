package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WORKFLOW_TRANSITION")
@Data
public class WorkflowTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKFLOWTRANSITIONID")
    private Integer workflowTransitionId;

    @Column(name = "WORKFLOWID")
    private Integer workflowId;

    @Column(name = "WORKFLOWNAME")
    private String workflowName;

    @Column(name = "TRANSITIONID")
    private Integer transitionId;

    @Column(name = "REQUESTID")
    private String requestId;

    @Column(name = "CREATEDBY")
    private Integer createdBy;

    @Column(name = "MODIFIEDBY")
    private Integer modifiedBy;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "NEXTACTION")
    private String nextAction;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "CURRENTROLE")
    private String currentRole;

    @Column(name = "NEXTROLE")
    private String nextRole;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "TRANSITIONORDER")
    private Integer transitionOrder;

    @Column(name = "WORKFLOWSEQUENCE")
    private Integer workflowSequence;

    @Column(name = "TRANSITIONSUBORDER")
    private Integer transitionSubOrder;

    @Column(name = "MODIFICATIONDATE")
    private Date modificationDate;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
