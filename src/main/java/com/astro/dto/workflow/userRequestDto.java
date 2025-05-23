package com.astro.dto.workflow;

import lombok.Data;


@Data
public class userRequestDto {

    private String userName;
    private String password;
    private String roleName;
    private String email;
    private String mobileNumber;
    private String employeeId;
    private String createdBy;

}
