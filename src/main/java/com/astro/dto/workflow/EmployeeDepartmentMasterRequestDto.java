package com.astro.dto.workflow;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class EmployeeDepartmentMasterRequestDto {

    private String employeeId;
    private String employeeName;
    private String departmentName;
    private String designation;
    private String contactDetails;
    private String createdBy;
    private String updatedBy;


}
