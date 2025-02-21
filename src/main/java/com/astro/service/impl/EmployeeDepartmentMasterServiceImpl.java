package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.EmployeeDepartmentMasterRequestDto;
import com.astro.dto.workflow.EmployeeDepartmentMasterResponseDto;
import com.astro.entity.EmployeeDepartmentMaster;

import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.EmployeeDepartmentMasterRepository;
import com.astro.service.EmployeeDepartmentMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeDepartmentMasterServiceImpl implements EmployeeDepartmentMasterService {

    @Autowired
    private EmployeeDepartmentMasterRepository employeeRepository;


    @Override
    public EmployeeDepartmentMasterResponseDto createEmployeeDepartment(EmployeeDepartmentMasterRequestDto employeeRequestDto) {

        // Check if the indentorId already exists
        if (employeeRepository.existsById(employeeRequestDto.getEmployeeId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Employee id", "Employee id" + employeeRequestDto.getEmployeeId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }



        EmployeeDepartmentMaster employee = new EmployeeDepartmentMaster();
        employee.setEmployeeId(employeeRequestDto.getEmployeeId());
        employee.setEmployeeName(employeeRequestDto.getEmployeeName());
        employee.setLocation(employeeRequestDto.getLocation());
        employee.setDepartmentName(employeeRequestDto.getDepartmentName());
        employee.setDesignation(employeeRequestDto.getDesignation());
        employee.setContactDetails(employeeRequestDto.getContactDetails());
        employee.setCreatedBy(employeeRequestDto.getCreatedBy());
        employee.setUpdatedBy(employeeRequestDto.getUpdatedBy());

        employeeRepository.save(employee);
        return mapToResponseDTO(employee);
    }

    private EmployeeDepartmentMasterResponseDto mapToResponseDTO(EmployeeDepartmentMaster employee) {

        EmployeeDepartmentMasterResponseDto responseDto = new EmployeeDepartmentMasterResponseDto();
        responseDto.setEmployeeId(employee.getEmployeeId());
        responseDto.setLocation(employee.getLocation());
        responseDto.setEmployeeName(employee.getEmployeeName());
        responseDto.setDepartmentName(employee.getDepartmentName());
        responseDto.setDesignation(employee.getDesignation());
        responseDto.setContactDetails(employee.getContactDetails());
        responseDto.setCreatedBy(employee.getCreatedBy());
        responseDto.setUpdatedBy(employee.getUpdatedBy());
        responseDto.setCreatedDate(employee.getCreatedDate());
        responseDto.setUpdatedDate(employee.getUpdatedDate());


        return responseDto;
    }

    @Override
    public EmployeeDepartmentMasterResponseDto updateEmployeeDepartmentMaster(String employeeId, EmployeeDepartmentMasterRequestDto employeeRequestDto) {

      EmployeeDepartmentMaster employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Employee departmnet not found for the provided employee id.")
                ));

        //employee.setEmployeeId(employeeRequestDto.getEmployeeId());
        employee.setEmployeeName(employeeRequestDto.getEmployeeName());
        employee.setDepartmentName(employeeRequestDto.getDepartmentName());
        employee.setLocation(employeeRequestDto.getLocation());
        employee.setDesignation(employeeRequestDto.getDesignation());
        employee.setContactDetails(employeeRequestDto.getContactDetails());
        employee.setCreatedBy(employeeRequestDto.getCreatedBy());
        employee.setUpdatedBy(employeeRequestDto.getUpdatedBy());

        employeeRepository.save(employee);


        return mapToResponseDTO(employee);
    }

    @Override
    public List<EmployeeDepartmentMasterResponseDto> getAllEmployeeDepartmentMasters() {
        List<EmployeeDepartmentMaster> employees= employeeRepository.findAll();
        return employees.stream().map(this::mapToResponseDTO).collect(Collectors.toList());


    }

    @Override
    public EmployeeDepartmentMasterResponseDto getEmployeeDepartmentMasterById(String employeeId) {

        EmployeeDepartmentMaster employees= employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Employee not found for the provided employee id.")
                ));
        return mapToResponseDTO(employees);
    }

    @Override
    public void deleteEmployeeDepartmentMasterr(String employeeId) {

        EmployeeDepartmentMaster employee=employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Employee not found for the provided employee ID."
                        )
                ));
        try {
           employeeRepository.delete(employee);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the employee."
                    ),
                    ex
            );
        }

    }
}
