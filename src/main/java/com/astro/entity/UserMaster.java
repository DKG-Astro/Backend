package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "USER_MASTER")
@Data
public class UserMaster {

    @Id
    @Column(name = "USERID")
    private Integer userId;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "MOBILENUMBER")
    private String mobileNumber;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;

}
