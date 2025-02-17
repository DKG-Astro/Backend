package com.astro.dto.workflow;

import lombok.Data;

import java.util.Date;

@Data
public class UserRoleDto {

    private Integer userRoleId;
    private Integer roleId;
    private String role;
    private Integer userId;
    private boolean readPermission;
    private boolean writePermission;
    private String createdBy;
    private Date createdDate;
}
