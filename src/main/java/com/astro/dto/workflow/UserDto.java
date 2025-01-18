package com.astro.dto.workflow;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private Integer userId;
    private String password;
    private String userName;
    private String mobileNumber;
    private String createdBy;
    private Date createdDate;
}
