package com.astro.dto.workflow;

import lombok.Data;

import java.util.Date;

@Data
public class RoleDto {

    private Integer roleId;
    private String roleName;
    private String createdBy;
    private Date createdDate;
}
