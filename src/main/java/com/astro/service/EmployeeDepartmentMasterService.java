package com.astro.service;

import com.astro.dto.workflow.EmployeeDepartmentMasterRequestDto;
import com.astro.dto.workflow.EmployeeDepartmentMasterResponseDto;
import java.util.List;

public interface EmployeeDepartmentMasterService {

    public EmployeeDepartmentMasterResponseDto createEmployeeDepartment(EmployeeDepartmentMasterRequestDto employeeRequestDto);
    public EmployeeDepartmentMasterResponseDto  updateEmployeeDepartmentMaster(String employeeId,EmployeeDepartmentMasterRequestDto employeeRequestDto);
    public List<EmployeeDepartmentMasterResponseDto> getAllEmployeeDepartmentMasters();

    public EmployeeDepartmentMasterResponseDto  getEmployeeDepartmentMasterById(String employeeId);
    public void deleteEmployeeDepartmentMasterr(String employeeId);

}
