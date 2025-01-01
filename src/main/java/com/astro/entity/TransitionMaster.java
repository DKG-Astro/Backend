package com.astro.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "TRANSITION_MASTER")
@Data
public class TransitionMaster {

    @Id
    @Column(name = "TRANSITIONID")
    private Integer transitionId;

    @Column(name = "TRANSITIONNAME")
    private String transitionName;

    @Column(name = "CURRENTROLEID")
    private Integer currentRoleId;

    @Column(name = "NEXTROLEID")
    private Integer nextRoleId;

    @Column(name = "PREVIOUSROLEID")
    private Integer previousRoleId;

    @Column(name = "TRANCONDITIONKEY")
    private String tranConditionKey;

    @Column(name = "TRANCONDITIONVALUE")
    private String tranConditionValue;

    @Column(name = "TRANSITIONORDER")
    private Integer transitionOrder;

    @Column(name = "TRANSITIONSUBORDER")
    private Integer transitionSubOrder;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
