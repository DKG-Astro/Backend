package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.JobMasterRequestDto;
import com.astro.dto.workflow.JobMasterResponseDto;
import com.astro.entity.JobMaster;

import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.JobMasterRepository;
import com.astro.service.JobMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobMasterServiceImpl implements JobMasterService {

    @Autowired
    private JobMasterRepository jobMasterRepository;
    @Override
    public JobMasterResponseDto createJobMaster(JobMasterRequestDto jobMasterRequestDto) {

        // Check if the indentorId already exists
        if (jobMasterRepository.existsById(jobMasterRequestDto.getJobCode())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate job code", "job code " + jobMasterRequestDto.getJobCode() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }
        JobMaster jobMaster = new JobMaster();
        jobMaster.setJobCode(jobMasterRequestDto.getJobCode());
        jobMaster.setCategory(jobMasterRequestDto.getCategory());
        jobMaster.setJobDescription(jobMasterRequestDto.getJobDescription());
        jobMaster.setAssetId(jobMasterRequestDto.getAssetId());
        jobMaster.setUom(jobMasterRequestDto.getUom());
        jobMaster.setValue(jobMasterRequestDto.getValue());
        jobMaster.setCreatedBy(jobMasterRequestDto.getCreatedBy());
        jobMaster.setUpdatedBy(jobMasterRequestDto.getUpdatedBy());
        jobMasterRepository.save(jobMaster);

        return mapToResponseDTO(jobMaster);
    }



    @Override
    public JobMasterResponseDto updateJobMaster(String jobCode, JobMasterRequestDto jobMasterRequestDto) {

        JobMaster jobMaster= jobMasterRepository.findById(jobCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Job Code not found for the provided JOb code.")
                ));
        //jobMaster.setJobCode(jobMasterRequestDto.getJobCode());
        jobMaster.setCategory(jobMasterRequestDto.getCategory());
        jobMaster.setJobDescription(jobMasterRequestDto.getJobDescription());
        jobMaster.setAssetId(jobMasterRequestDto.getAssetId());
        jobMaster.setUom(jobMasterRequestDto.getUom());
        jobMaster.setValue(jobMasterRequestDto.getValue());
        jobMaster.setCreatedBy(jobMasterRequestDto.getCreatedBy());
        jobMaster.setUpdatedBy(jobMasterRequestDto.getUpdatedBy());
        jobMasterRepository.save(jobMaster);


        return mapToResponseDTO(jobMaster);
    }

    @Override
    public List<JobMasterResponseDto> getAllJobMasters() {
        List<JobMaster> jobMasters= jobMasterRepository.findAll();
        return jobMasters.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public JobMasterResponseDto getJobMasterById(String jobCode) {
        JobMaster jobMaster= jobMasterRepository.findById(jobCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Job Master not found for the provided job code.")
                ));
        return mapToResponseDTO(jobMaster);
    }

    @Override
    public void deleteJobMaster(String jobCode) {

        JobMaster jobMaster=jobMasterRepository.findById(jobCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Job Master not found for the provided Job code."
                        )
                ));
        try {
            jobMasterRepository.delete(jobMaster);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the Job master."
                    ),
                    ex
            );
        }

    }

    private JobMasterResponseDto mapToResponseDTO(JobMaster jobMaster) {

        JobMasterResponseDto responseDto = new JobMasterResponseDto();
        responseDto.setJobCode(jobMaster.getJobCode());
        responseDto.setCategory(jobMaster.getCategory());
        responseDto.setJobDescription(jobMaster.getJobDescription());
        responseDto.setAssetId(jobMaster.getAssetId());
        responseDto.setUom(jobMaster.getUom());
        responseDto.setValue(jobMaster.getValue());
        responseDto.setUpdatedBy(jobMaster.getUpdatedBy());
        responseDto.setCreatedBy(jobMaster.getCreatedBy());
        responseDto.setCreatedDate(jobMaster.getCreatedDate());
        responseDto.setUpdatedDate(jobMaster.getUpdatedDate());

        return responseDto;
    }
}
