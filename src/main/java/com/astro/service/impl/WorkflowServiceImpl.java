package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.TransitionConditionDto;
import com.astro.dto.workflow.TransitionDto;
import com.astro.dto.workflow.WorkflowDto;
import com.astro.dto.workflow.WorkflowTransitionDto;
import com.astro.entity.*;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.*;
import com.astro.service.UserService;
import com.astro.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    WorkflowTransitionRepository workflowTransitionRepository;

    @Autowired
    WorkflowMasterRepository workflowMasterRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransitionMasterRepository transitionMasterRepository;

    @Autowired
    RoleMasterRepository roleMasterRepository;

    @Autowired
    TransitionConditionMasterRepository transitionConditionMasterRepository;

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
            }else{
                throw new InvalidInputException(new ErrorDetails(AppConstant.WORKFLOW_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Workflow not found."));
            }
        }else {
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid input."));
        }

        return workflowDto;
    }

    @Override
    public List<TransitionDto> transitionsByWorkflowId(Integer workflowId) {

        List<TransitionDto> transitionDtoList = new ArrayList<>();
        List<TransitionMaster> transitionMasterList = transitionMasterRepository.findByWorkflowId(workflowId);

        if(Objects.nonNull(transitionMasterList) && !transitionMasterList.isEmpty()){
            transitionDtoList = transitionMasterList.stream().map(transitionMaster -> {
                TransitionDto transitionDto = new TransitionDto();
                transitionDto.setWorkflowId(transitionMaster.getWorkflowId());
                transitionDto.setTransitionId(transitionMaster.getTransitionId());
                transitionDto.setTransitionSubOrder(transitionMaster.getTransitionSubOrder());
                transitionDto.setCreatedDate(transitionMaster.getCreatedDate());
                transitionDto.setCreatedBy(transitionMaster.getCreatedBy());
                transitionDto.setTransitionOrder(transitionMaster.getTransitionOrder());
                transitionDto.setConditionId(transitionMaster.getConditionId());
                transitionDto.setCurrentRoleId(transitionMaster.getCurrentRoleId());
                transitionDto.setNextRoleId(transitionMaster.getNextRoleId());
                transitionDto.setPreviousRoleId(transitionMaster.getPreviousRoleId());
                transitionDto.setTransitionName(transitionMaster.getTransitionName());
                transitionDto.setWorkflowName(workflowNameById(transitionMaster.getWorkflowId()));
                transitionDto.setCurrentRoleName(roleNameById(transitionMaster.getCurrentRoleId()));
                transitionDto.setNextRoleName(roleNameById(transitionMaster.getNextRoleId()));
                transitionDto.setPreviousRoleName(roleNameById(transitionMaster.getPreviousRoleId()));
                TransitionConditionDto transitionConditionDto = transitionConditionById(transitionMaster.getConditionId());
                transitionDto.setConditionKey(transitionConditionDto.getConditionKey());
                transitionDto.setConditionValue(transitionConditionDto.getConditionValue());

                return transitionDto;
            }).collect(Collectors.toList());
        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.WORKFLOW_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow not found."));
        }
        return transitionDtoList;
    }

    private TransitionConditionDto transitionConditionById(Integer conditionId) {
        TransitionConditionDto transitionConditionDto = new TransitionConditionDto();
        if(Objects.nonNull(conditionId)){
            TransitionConditionMaster transitionConditionMaster = transitionConditionMasterRepository.findById(conditionId).orElse(null);
                if(Objects.nonNull(transitionConditionMaster)){
                    transitionConditionDto.setConditionId(transitionConditionMaster.getConditionId());
                    transitionConditionDto.setConditionKey(transitionConditionMaster.getConditionKey());
                    transitionConditionDto.setWorkflowId(transitionConditionMaster.getWorkflowId());
                    transitionConditionDto.setConditionValue(transitionConditionMaster.getConditionValue());
                    transitionConditionDto.setCreatedDate(transitionConditionMaster.getCreatedDate());
                    transitionConditionDto.setCreatedBy(transitionConditionMaster.getCreatedBy());
                }
        }

        return transitionConditionDto;
    }

    private String roleNameById(Integer roleId) {
       if(Objects.nonNull(roleId)) {
           return roleMasterRepository.findById(roleId).orElse(new RoleMaster()).getRoleName();
       }else{
           return null;
       }
    }

    private String workflowNameById(Integer workflowId) {
        if(Objects.nonNull(workflowId)) {
            return workflowMasterRepository.findById(workflowId).orElse(new WorkflowMaster()).getWorkflowName();
        }else {
            return null;
        }
    }

    @Override
    public TransitionDto transitionsByWorkflowIdAndOrder(Integer workflowId, Integer order, Integer subOrder) {
        TransitionDto transitionDto = null;
        TransitionMaster transitionMaster  = transitionMasterRepository.findByWorkflowIdAndTransitionOrderAndTransitionSubOrder(workflowId, order, subOrder);
            if(Objects.nonNull(transitionMaster)){
                transitionDto = new TransitionDto();
                transitionDto.setWorkflowId(transitionMaster.getWorkflowId());
                transitionDto.setTransitionId(transitionMaster.getTransitionId());
                transitionDto.setTransitionSubOrder(transitionMaster.getTransitionSubOrder());
                transitionDto.setCreatedDate(transitionMaster.getCreatedDate());
                transitionDto.setCreatedBy(transitionMaster.getCreatedBy());
                transitionDto.setTransitionOrder(transitionMaster.getTransitionOrder());
                transitionDto.setConditionId(transitionMaster.getConditionId());
                transitionDto.setCurrentRoleId(transitionMaster.getCurrentRoleId());
                transitionDto.setNextRoleId(transitionMaster.getNextRoleId());
                transitionDto.setPreviousRoleId(transitionMaster.getPreviousRoleId());
                transitionDto.setTransitionName(transitionMaster.getTransitionName());
                transitionDto.setWorkflowName(workflowNameById(transitionMaster.getWorkflowId()));
                transitionDto.setCurrentRoleName(roleNameById(transitionMaster.getCurrentRoleId()));
                transitionDto.setNextRoleName(roleNameById(transitionMaster.getNextRoleId()));
                transitionDto.setPreviousRoleName(roleNameById(transitionMaster.getPreviousRoleId()));
                TransitionConditionDto transitionConditionDto = transitionConditionById(transitionMaster.getConditionId());
                transitionDto.setConditionKey(transitionConditionDto.getConditionKey());
                transitionDto.setConditionValue(transitionConditionDto.getConditionValue());
            }
        return transitionDto;
    }

    @Override
    @Transactional
    public WorkflowTransitionDto initiateWorkflow(Integer requestId, String workflowName, Integer createdBy) {
        WorkflowTransitionDto workflowTransitionDto = null;
        if(Objects.nonNull(requestId) && Objects.nonNull(workflowName) && Objects.nonNull(createdBy)){
            userService.validateUser(createdBy);
            WorkflowDto workflowDto = workflowByWorkflowName(workflowName);
            TransitionDto transitionDto = transitionsByWorkflowIdAndOrder(workflowDto.getWorkflowId(), 1, 1);
            if(Objects.isNull(transitionDto)){
                throw new InvalidInputException(new ErrorDetails(AppConstant.TRANSITION_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Transition not found."));
            }
            WorkflowTransition workflowTransition = createWorkflowTransition(requestId, workflowDto, transitionDto, createdBy);
            workflowTransitionRepository.save(workflowTransition);
            workflowTransitionDto = mapWorkflowTransitionDto(workflowTransition);

        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid input."));

        }
        return workflowTransitionDto;
    }

    @Override
    public List<WorkflowTransitionDto> workflowTransitionHistory(Integer workflowId, Integer createdBy, Integer requestId, String roleName) {

        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();

        Integer nextTransitionId = null;
        RoleMaster roleMaster = roleMasterRepository.findByRoleName(roleName);
        if(Objects.nonNull(roleMaster)){
            TransitionMaster transitionMaster = transitionMasterRepository.findByWorkflowIdAndNextRoleId(workflowId, roleMaster.getRoleId());
            nextTransitionId = transitionMaster.getTransitionId();
        }
        List<WorkflowTransition> workflowTransitionList = workflowTransitionRepository.findByWorkflowIdOrCreatedByOrRequestIdOrTransitionId(workflowId, createdBy, requestId, nextTransitionId);
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()){
            workflowTransitionDtoList = workflowTransitionList.stream().map(e -> {
                return mapWorkflowTransitionDto(e);
            }).collect(Collectors.toList());
        }

        return workflowTransitionDtoList;
    }

    private WorkflowTransitionDto mapWorkflowTransitionDto(WorkflowTransition workflowTransition) {
        WorkflowTransitionDto workflowTransitionDto = new WorkflowTransitionDto();
        workflowTransitionDto.setWorkflowTransitionId(workflowTransition.getWorkflowTransitionId());
        workflowTransitionDto.setTransitionId(workflowTransition.getTransitionId());
        workflowTransitionDto.setWorkflowId(workflowTransition.getWorkflowId());
        workflowTransitionDto.setWorkflowName(workflowTransition.getWorkflowName());
        workflowTransitionDto.setModificationDate(workflowTransition.getModificationDate());
        workflowTransitionDto.setCreatedBy(workflowTransition.getCreatedBy());
        workflowTransitionDto.setTransitionOrder(workflowTransition.getTransitionOrder());
        workflowTransitionDto.setRequestId(workflowTransition.getRequestId());
        workflowTransitionDto.setStatus(workflowTransition.getStatus());
        workflowTransitionDto.setTransitionSubOrder(workflowTransition.getTransitionSubOrder());
        workflowTransitionDto.setCreatedDate(workflowTransition.getCreatedDate());
        workflowTransitionDto.setModifiedBy(workflowTransition.getModifiedBy());

        return workflowTransitionDto;
    }

    private WorkflowTransition createWorkflowTransition(Integer requestId, WorkflowDto workflowDto, TransitionDto transitionDto, Integer createdBy) {
        WorkflowTransition workflowTransition = new WorkflowTransition();
        workflowTransition.setTransitionId(transitionDto.getTransitionId());
        workflowTransition.setWorkflowId(workflowDto.getWorkflowId());
        workflowTransition.setTransitionOrder(transitionDto.getTransitionOrder());
        workflowTransition.setTransitionSubOrder(transitionDto.getTransitionSubOrder());
        workflowTransition.setStatus(AppConstant.CREATED_TYPE);
        workflowTransition.setNextAction(AppConstant.PENDING_TYPE);
        workflowTransition.setCreatedDate(new Date());
        workflowTransition.setCreatedBy(createdBy);
        workflowTransition.setModifiedBy(null);
        workflowTransition.setModificationDate(null);
        workflowTransition.setRequestId(requestId);
        workflowTransition.setWorkflowName(workflowDto.getWorkflowName());

        return workflowTransition;
    }

    @Override
    public TransitionDto nextTransition(Integer workflowId, String currentRole, String tranConditionKey, String tranConditionValue) {
        TransitionDto transitionDto = null;

        if(Objects.nonNull(workflowId) && Objects.nonNull(currentRole)){
            List<TransitionDto> transitionDtoList = transitionsByWorkflowId(workflowId);
            if(Objects.nonNull(transitionDtoList) && !transitionDtoList.isEmpty()){

                if(Objects.nonNull(tranConditionValue) && Objects.nonNull(tranConditionKey)) {
                    List<TransitionDto> filteredList = transitionDtoList.stream().filter(e -> e.getCurrentRoleName().equalsIgnoreCase(currentRole) && tranConditionKey.equalsIgnoreCase(e.getConditionKey()) &&
                            tranConditionValue.equalsIgnoreCase(e.getConditionValue())).collect(Collectors.toList());
                    if (Objects.nonNull(filteredList) && !filteredList.isEmpty()) {
                        transitionDto = filteredList.get(0);
                    }
                }else{
                    List<TransitionDto> filteredList = transitionDtoList.stream().filter(e -> e.getCurrentRoleName().equalsIgnoreCase(currentRole)).collect(Collectors.toList());
                    if (Objects.nonNull(filteredList) && !filteredList.isEmpty()) {
                        transitionDto = filteredList.get(0);
                    }
                }
            }
        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid input."));
        }
        return transitionDto;
    }
}
