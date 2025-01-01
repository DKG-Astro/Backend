package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "WORKFLOW_MASTER")
@Data
public class WorkflowMaster {

    @Id
    @Column(name = "WORKFLOWID")
    private Integer workflowId;

    @Column(name = "WORKFLOWNAME")
    private String workflowName;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
