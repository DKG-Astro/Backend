package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.*;
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

    @Autowired
    UserRoleMasterRepository userRoleMasterRepository;

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
            validateWorkflowTransition(requestId, createdBy, workflowDto.getWorkflowId());

            WorkflowTransition workflowTransition = createWorkflowTransition(requestId, workflowDto, transitionDto, createdBy);
            workflowTransitionRepository.save(workflowTransition);
            workflowTransitionDto = mapWorkflowTransitionDto(workflowTransition);

        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid input."));

        }
        return workflowTransitionDto;
    }

    private void validateWorkflowTransition(Integer requestId, Integer createdBy, Integer workflowId) {
        WorkflowTransition workflowTransition = workflowTransitionRepository.findByWorkflowIdAndCreatedByAndRequestId(workflowId, createdBy, requestId);
        if(Objects.nonNull(workflowTransition)){
            throw new InvalidInputException(new ErrorDetails(AppConstant.WORKFLOW_ALREADY_EXISTS, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow with same request id and created by already exists."));
        }
    }

    @Override
    public List<WorkflowTransitionDto> workflowTransitionHistory(Integer workflowId, Integer createdBy, Integer requestId, String roleName) {

        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();
        List<WorkflowTransition> workflowTransitionList = null;
        if(Objects.nonNull(roleName)){
            workflowTransitionList = workflowTransitionRepository.findByWorkflowIdAndCurrentRole(workflowId, roleName);
        }else {
            workflowTransitionList = workflowTransitionRepository.findByWorkflowIdOrCreatedByOrRequestId(workflowId, createdBy, requestId);
        }
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
        workflowTransitionDto.setNextAction(workflowTransition.getNextAction());
        workflowTransitionDto.setCreatedRole(roleNameById(workflowTransition.getCreatedBy()));
        workflowTransitionDto.setModifiedRole(roleNameById(workflowTransition.getModifiedBy()));
        workflowTransitionDto.setCurrentRole(workflowTransition.getCurrentRole());
        workflowTransitionDto.setNextRole(workflowTransition.getNextRole());
        TransitionMaster transitionMaster = transitionById(workflowTransition.getTransitionId());
        if(Objects.nonNull(transitionMaster)) {
            workflowTransitionDto.setNextActionId(transitionMaster.getNextRoleId());
            workflowTransitionDto.setNextActionRole(roleNameById(transitionMaster.getNextRoleId()));
        }
        return workflowTransitionDto;
    }

    private TransitionMaster transitionById(Integer transitionId) {
        return transitionMasterRepository.findById(transitionId).orElse(null);
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
        workflowTransition.setCurrentRole(transitionDto.getCurrentRoleName());
        workflowTransition.setNextRole(transitionDto.getNextRoleName());

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

    @Override
    @Transactional
    public WorkflowTransitionDto performTransitionAction(TransitionActionReqDto transitionActionReqDto) {
        userService.validateUser(transitionActionReqDto.getActionBy());
        WorkflowTransition workflowTransition = workflowTransitionRepository.findById(transitionActionReqDto.getWorkflowTransitionId()).orElse(null);
        if(Objects.isNull(workflowTransition)){
            throw new InvalidInputException(new ErrorDetails(AppConstant.INVALID_WORKFLOW_TRANSITION, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow transition not found."));
        }
        TransitionMaster currentTransition = transitionMasterRepository.findById(workflowTransition.getTransitionId()).orElse(null);
        validateUserRole(transitionActionReqDto.getActionBy(), currentTransition.getNextRoleId());

        if(AppConstant.APPROVE_TYPE.equalsIgnoreCase(transitionActionReqDto.getAction())){
            approveTransition(workflowTransition, currentTransition, transitionActionReqDto);
        }else if(AppConstant.REJECT_TYPE.equalsIgnoreCase(transitionActionReqDto.getAction())){
            rejectTransition(workflowTransition, currentTransition, transitionActionReqDto);
        }else if(AppConstant.CHANGE_REQUEST_TYPE.equalsIgnoreCase(transitionActionReqDto.getAction())){
            requestChangeTransition(workflowTransition, currentTransition, transitionActionReqDto);
        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.INVALID_TRANSITION_ACTION, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid transition action."));
        }


        return null;
    }

    private void requestChangeTransition(WorkflowTransition workflowTransition, TransitionMaster currentTransition, TransitionActionReqDto transitionActionReqDto) {
    }

    private void rejectTransition(WorkflowTransition workflowTransition, TransitionMaster currentTransition, TransitionActionReqDto transitionActionReqDto) {
    }

    private void approveTransition(WorkflowTransition currentWorkflowTransition, TransitionMaster currentTransition, TransitionActionReqDto transitionActionReqDto) {
        if(Objects.isNull(currentTransition.getNextRoleId())){
            currentWorkflowTransition.setModifiedBy(transitionActionReqDto.getActionBy());
            currentWorkflowTransition.setModificationDate(new Date());
            currentWorkflowTransition.setNextAction(null);
            currentWorkflowTransition.setStatus(AppConstant.COMPLETED_TYPE);
            workflowTransitionRepository.save(currentWorkflowTransition);
        }else{
           TransitionDto nextTransition =  nextTransition(currentTransition.getWorkflowId(), transitionActionReqDto.getUserRole(), transitionActionReqDto.getTranConditionKey(), transitionActionReqDto.getTranConditionValue());
            if(Objects.isNull(nextTransition)){
                throw new InvalidInputException(new ErrorDetails(AppConstant.NEXT_TRANSITION_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Error occurred at approval. No next transition found."));
            }
            WorkflowTransition nextWorkflowTransition = new WorkflowTransition();
            nextWorkflowTransition.setWorkflowId(nextTransition.getWorkflowId());
            nextWorkflowTransition.setTransitionId(nextTransition.getTransitionId());
            nextWorkflowTransition.setTransitionOrder(nextTransition.getTransitionOrder());
            nextWorkflowTransition.setTransitionSubOrder(nextTransition.getTransitionSubOrder());
            nextWorkflowTransition.setWorkflowName(nextTransition.getWorkflowName());
            nextWorkflowTransition.setStatus(AppConstant.IN_PROGRESS_TYPE);
            nextWorkflowTransition.setAction(transitionActionReqDto.getAction());
            nextWorkflowTransition.setRemarks(transitionActionReqDto.getRemarks());
            nextWorkflowTransition.setModifiedBy(transitionActionReqDto.getActionBy());
            nextWorkflowTransition.setModificationDate(new Date());
            nextWorkflowTransition.setNextAction(AppConstant.PENDING_TYPE);
            nextWorkflowTransition.setRequestId(currentWorkflowTransition.getRequestId());
            nextWorkflowTransition.setCreatedBy(currentWorkflowTransition.getCreatedBy());
            nextWorkflowTransition.setCreatedDate(currentWorkflowTransition.getCreatedDate());
            nextWorkflowTransition.setCurrentRole(nextTransition.getCurrentRoleName());
            nextWorkflowTransition.setNextRole(nextTransition.getNextRoleName());

            workflowTransitionRepository.save(nextWorkflowTransition);
        }

    }

    private void validateUserRole(Integer actionBy, Integer roleId) {
        if(Objects.nonNull(roleId)) {
            UserRoleMaster userRoleMaster = userRoleMasterRepository.findByRoleIdAndUserId(roleId, actionBy);
            if(Objects.isNull(userRoleMaster)){
                throw new InvalidInputException(new ErrorDetails(AppConstant.UNAUTHORIZED_ACTION, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Unauthorized user."));
            }
        }
    }
}
