package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "STATE_MASTER")
@Data
public class StateMaster {

    @Id
    @Column(name = "STATEID")
    private Integer stateId;

    @Column(name = "STATENAME")
    private String stateName;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
