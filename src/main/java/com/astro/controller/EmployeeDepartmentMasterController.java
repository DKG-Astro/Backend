package com.astro.controller;

import com.astro.dto.workflow.EmployeeDepartmentMasterRequestDto;
import com.astro.dto.workflow.EmployeeDepartmentMasterResponseDto;
import com.astro.service.EmployeeDepartmentMasterService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-department-master")
public class EmployeeDepartmentMasterController {

    @Autowired
    private EmployeeDepartmentMasterService employeeService;
    @PostMapping
    public ResponseEntity<Object> createEmployeeMaster(@RequestBody EmployeeDepartmentMasterRequestDto requestDTO) {
        EmployeeDepartmentMasterResponseDto material = employeeService.createEmployeeDepartment(requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(material), HttpStatus.OK);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Object> updateEmployeeMaster(@PathVariable String employeeId,
                                                     @RequestBody EmployeeDepartmentMasterRequestDto requestDTO) {
       EmployeeDepartmentMasterResponseDto response = employeeService.updateEmployeeDepartmentMaster(employeeId, requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllEMployeeMaster() {
        List<EmployeeDepartmentMasterResponseDto> response = employeeService.getAllEmployeeDepartmentMasters();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Object> getEmployeeMasterById(@PathVariable String employeeId) {
        EmployeeDepartmentMasterResponseDto responseDTO = employeeService.getEmployeeDepartmentMasterById(employeeId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployeeMaster(@PathVariable String employeeId) {
        employeeService.deleteEmployeeDepartmentMasterr(employeeId);
        return ResponseEntity.ok("Employee master deleted successfully. EmployeeId:"+" " +employeeId);
    }



}
