package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.TransitionDto;
import com.astro.dto.workflow.WorkflowDto;
import com.astro.dto.workflow.WorkflowTransitionDto;
import com.astro.entity.WorkflowMaster;
import com.astro.entity.WorkflowTransition;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.WorkflowMasterRepository;
import com.astro.repository.WorkflowTransitionRepository;
import com.astro.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    WorkflowTransitionRepository workflowTransitionRepository;

    @Autowired
    WorkflowMasterRepository workflowMasterRepository;

    @Override
    public List<WorkflowTransitionDto> workflowTransitionsByWorkflowId(Integer workflowId) {

        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();

        List<WorkflowTransition> workflowTransitionList = workflowTransitionRepository.findByWorkflowId(workflowId);

        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()){
            workflowTransitionList.stream().map(e -> {
                WorkflowTransitionDto workflowTransitionDto = new WorkflowTransitionDto();
                workflowTransitionDto.setTransitionId(e.getTransitionId());
                workflowTransitionDto.setWorkflowId(e.getWorkflowId());
                workflowTransitionDto.setWorkflowTransitionId(e.getWorkflowTransitionId());
                workflowTransitionDto.setStatus(e.getStatus());
                workflowTransitionDto.setCreatedDate(e.getCreatedDate());
                workflowTransitionDto.setTransitionOrder(e.getTransitionOrder());
                workflowTransitionDto.setWorkflowName(e.getWorkflowName());
                workflowTransitionDto.setModificationDate(e.getModificationDate());
                workflowTransitionDto.setCreatedBy(e.getCreatedBy());
                workflowTransitionDto.setModifiedBy(e.getModifiedBy());
                workflowTransitionDto.setRequestId(e.getRequestId());
                workflowTransitionDto.setTransitionSubOrder(e.getTransitionSubOrder());
                return workflowTransitionDto;
            }).collect(Collectors.toList());
        }
        return workflowTransitionDtoList;
    }

    @Override
    public WorkflowDto workflowByWorkflowName(String workflowName) {
        WorkflowDto workflowDto = null;

        if(Objects.nonNull(workflowName)){
           WorkflowMaster workflowMaster = workflowMasterRepository.findByWorkflowName(workflowName);
           if(Objects.nonNull(workflowMaster)){
               workflowDto = new WorkflowDto();
               workflowDto.setWorkflowId(workflowMaster.getWorkflowId());
               workflowDto.setWorkflowName(workflowMaster.getWorkflowName());
               workflowDto.setCreatedBy(workflowMaster.getCreatedBy());
               workflowDto.setCreatedDate(workflowMaster.getCreatedDate());
           }
        }else {
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid input."));
        }

        return workflowDto;
    }

    @Override
    public TransitionDto nextTransition(String workflowId, String currentRole, String tranConditionKey, String tranConditionValue) {
        TransitionDto transitionDto = null;

        if(Objects.nonNull(workflowId) && Objects.nonNull(tranConditionKey)){

        }else{

        }

        return null;
    }
}
