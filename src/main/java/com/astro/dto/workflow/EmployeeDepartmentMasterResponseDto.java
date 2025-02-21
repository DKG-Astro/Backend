package com.astro.dto.workflow;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class EmployeeDepartmentMasterResponseDto {

    private String employeeId;
    private String employeeName;
    private String departmentName;
    private String location;
    private String designation;
    private String contactDetails;
    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
