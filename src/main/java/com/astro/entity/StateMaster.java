package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "STATE_MASTER")
@Data
public class StateMaster {

    @Id
    @GeneratedValue
    @Column(name = "STATEID")
    private Integer stateId;

    @Column(name = "STATENAME")
    private String stateName;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
