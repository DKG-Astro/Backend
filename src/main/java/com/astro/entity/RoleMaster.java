package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ROLE_MASTER")
@Data
public class RoleMaster {

    @Id
    @Column(name = "ROLEID")
    private Integer roleId;

    @Column(name = "ROLENAME")
    private String roleName;

    @Column(name = "CREATEDBY")
    private String createdBy;

    @Column(name = "CREATEDDATE")
    private Date createdDate;
}
