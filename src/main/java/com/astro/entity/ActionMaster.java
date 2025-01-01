package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ACTION_MASTER")
@Data
public class ActionMaster {

    @Id
    @Column(name = "ACTIONID")
    private Integer actionId;

    @Column(name = "ACTIONNAME")
    private String actionName;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
