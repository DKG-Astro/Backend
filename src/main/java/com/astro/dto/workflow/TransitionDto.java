package com.astro.dto.workflow;

import lombok.Data;

import java.util.Date;

@Data
public class TransitionDto {

    private Integer transitionId;
    private String transitionName;
    private Integer currentRoleId;
    private Integer nextRoleId;
    private Integer previousRoleId;
    private String tranConditionKey;
    private String tranConditionValue;
    private Integer transitionOrder;
    private Integer transitionSubOrder;
    private String createdBy;
    private Date createdDate;
}
