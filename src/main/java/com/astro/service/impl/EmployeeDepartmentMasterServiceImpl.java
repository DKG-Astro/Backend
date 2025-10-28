package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.EmployeeDepartmentMasterRequestDto;
import com.astro.dto.workflow.EmployeeDepartmentMasterResponseDto;
import com.astro.dto.workflow.EmployeeSearchResponseDto;
import com.astro.dto.workflow.employeedto;
import com.astro.entity.EmployeeDepartmentMaster;

import com.astro.entity.EmployeeIdSequence;
import com.astro.entity.MaterialIdSequence;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.EmployeeDepartmentMasterRepository;
import com.astro.repository.EmployeeIdSequenceRepository;
import com.astro.service.EmployeeDepartmentMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeDepartmentMasterServiceImpl implements EmployeeDepartmentMasterService {

    @Autowired
    private EmployeeDepartmentMasterRepository employeeRepository;
    @Autowired
    private EmployeeIdSequenceRepository employeeIdSequenceRepository;


    @Override
    public EmployeeDepartmentMasterResponseDto createEmployeeDepartment(EmployeeDepartmentMasterRequestDto employeeRequestDto) {

        Integer maxNumber = employeeIdSequenceRepository.findMaxEmployeeId();
        int nextNumber = (maxNumber == null) ? 1100 : maxNumber + 1;

        String employeeId = "E" + nextNumber;

        EmployeeIdSequence em = new EmployeeIdSequence();
        em.setEmployeeId(nextNumber);
        employeeIdSequenceRepository.save(em);
        EmployeeDepartmentMaster employee = new EmployeeDepartmentMaster();
        employee.setEmployeeId(employeeId);
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
    public List<employeedto> getAllEmployeeDepartmentMasterswithName() {
        List<EmployeeDepartmentMaster> employees = employeeRepository.findAll();
        return employees.stream()
                .map(emp -> new employeedto(emp.getEmployeeId(), emp.getEmployeeName()))
                .collect(Collectors.toList());
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

    @Override
    public List<EmployeeSearchResponseDto> searchEmployees(String keyword) {
        List<Object[]> results = employeeRepository.searchEmployeesForDropdown(keyword);

        return results.stream()
                .map(obj -> new EmployeeSearchResponseDto(
                        (String) obj[0],  // employee_id
                        (String) obj[1],  // employee_name
                        (String) obj[2],  // department_name
                        (String) obj[3]   // designation
                ))
                .collect(Collectors.toList());
    }

}
