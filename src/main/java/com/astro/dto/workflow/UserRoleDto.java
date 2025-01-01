package com.astro.dto.workflow;

import lombok.Data;

import java.util.Date;

@Data
public class UserRoleDto {

    private Integer userRoleId;
    private Integer roleId;
    private Integer userId;
    private String createdBy;
    private Date createdDate;
}
