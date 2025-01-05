package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_ROLE_MASTER")
@Data
public class UserRoleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERROLEID")
    private Integer userRoleId;

    @Column(name = "ROLEID")
    private Integer roleId;

    @Column(name = "USERID")
    private Integer userId;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
