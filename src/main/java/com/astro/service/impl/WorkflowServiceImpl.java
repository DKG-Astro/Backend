package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.constant.WorkflowName;
import com.astro.dto.workflow.*;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.soWithTenderAndIndentResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.woWithTenderAndIndentResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.poWithTenderAndIndentResponseDTO;
import com.astro.entity.*;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.*;
import com.astro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    @Autowired
    IndentCreationService indentCreationService;

    @Autowired
    ContigencyPurchaseService contigencyPurchaseService;

    @Autowired
    TenderRequestService tenderRequestService;

    @Autowired
    ServiceOrderService serviceOrderService;

    @Autowired
    WorkOrderService workOrderService;

    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Autowired
    SubWorkflowTransitionRepository subWorkflowTransitionRepository;

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

    private String roleNameByUserId(Integer userId) {
        if(Objects.nonNull(userId)) {
            UserRoleMaster userRoleMaster =  userRoleMasterRepository.findByUserId(userId);
            if(Objects.nonNull(userRoleMaster)){
                return roleMasterRepository.findById(userRoleMaster.getRoleId()).orElse(new RoleMaster()).getRoleName();
            }
        }
            return null;
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
    public WorkflowTransitionDto initiateWorkflow(String requestId, String workflowName, Integer createdBy) {
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

    private void validateWorkflowTransition(String requestId, Integer createdBy, Integer workflowId) {
        WorkflowTransition workflowTransition = workflowTransitionRepository.findByWorkflowIdAndCreatedByAndRequestId(workflowId, createdBy, requestId);
        if(Objects.nonNull(workflowTransition)){
            throw new InvalidInputException(new ErrorDetails(AppConstant.WORKFLOW_ALREADY_EXISTS, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow with same request id and created by already exists."));
        }
    }

    @Override
    public List<WorkflowTransitionDto> workflowTransitionHistory(String requestId) {

        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();
        List<WorkflowTransition> workflowTransitionList = null;
        workflowTransitionList = workflowTransitionRepository.findByRequestId(requestId);
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()) {
            workflowTransitionDtoList = workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getWorkflowSequence).reversed()).map(e -> {
                return mapWorkflowTransitionDto(e);
            }).collect(Collectors.toList());
        }

        return workflowTransitionDtoList;
    }

    @Override
    public List<WorkflowTransitionDto> allWorkflowTransition(String roleName) {
        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();

        List<WorkflowTransition> workflowTransitionList  = workflowTransitionRepository.findByNextRole(roleName);
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()){
            workflowTransitionDtoList = workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getRequestId).thenComparing(WorkflowTransition::getCreatedDate)).map(e ->{
                return mapWorkflowTransitionDto(e);
            }).collect(Collectors.toList());
        }
        return workflowTransitionDtoList;
    }

    @Override
    public List<WorkflowTransitionDto> allPendingWorkflowTransition(String roleName) {
        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();

        List<WorkflowTransition> workflowTransitionList  = workflowTransitionRepository.findByNextActionAndNextRole(AppConstant.PENDING_TYPE, roleName);
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()){
            workflowTransitionDtoList = workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getRequestId).thenComparing(WorkflowTransition::getCreatedDate)).map(e ->{
                return mapWorkflowTransitionDto(e);
            }).collect(Collectors.toList());
        }

        return workflowTransitionDtoList;
    }

    @Override
    public List<String> allPreviousRoleWorkflowTransition(Integer workflowId, String requestId) {
        List<String> allPreviousRole = new ArrayList<>();

        List<WorkflowTransition> workflowTransitionList  = workflowTransitionRepository.findByWorkflowIdAndRequestId(workflowId, requestId);
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty() && workflowTransitionList.size() > 1){
            allPreviousRole = workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getWorkflowTransitionId)).limit(workflowTransitionList.size() -1).map(e ->e.getCurrentRole()).distinct().collect(Collectors.toList());
        }
        return allPreviousRole;
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
        workflowTransitionDto.setWorkflowSequence(workflowTransition.getWorkflowSequence());
        workflowTransitionDto.setAction(workflowTransition.getAction());
        workflowTransitionDto.setRemarks(workflowTransition.getRemarks());
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

    private WorkflowTransition createWorkflowTransition(String requestId, WorkflowDto workflowDto, TransitionDto transitionDto, Integer createdBy) {
        WorkflowTransition workflowTransition = new WorkflowTransition();
        workflowTransition.setTransitionId(transitionDto.getTransitionId());
        workflowTransition.setWorkflowId(workflowDto.getWorkflowId());
        workflowTransition.setTransitionOrder(transitionDto.getTransitionOrder());
        workflowTransition.setTransitionSubOrder(transitionDto.getTransitionSubOrder());
        workflowTransition.setStatus(AppConstant.CREATED_TYPE);
        workflowTransition.setAction(AppConstant.CREATED_TYPE);
        workflowTransition.setNextAction(AppConstant.PENDING_TYPE);
        workflowTransition.setCreatedDate(new Date());
        workflowTransition.setCreatedBy(createdBy);
        workflowTransition.setModifiedBy(null);
        workflowTransition.setModificationDate(null);
        workflowTransition.setRequestId(requestId);
        workflowTransition.setWorkflowName(workflowDto.getWorkflowName());
        workflowTransition.setCurrentRole(transitionDto.getCurrentRoleName());
        workflowTransition.setNextRole(transitionDto.getNextRoleName());
        workflowTransition.setWorkflowSequence(1);

        return workflowTransition;
    }

    @Override
    public TransitionDto nextTransition(Integer workflowId, String workflowName, String currentRole, String requestId) {
        TransitionDto transitionDto = null;

        if(Objects.nonNull(workflowId) && Objects.nonNull(currentRole)){
            List<TransitionDto> nextTransitionDtoList = transitionsByWorkflowId(workflowId).stream().filter(e -> currentRole.equalsIgnoreCase(e.getCurrentRoleName())).sorted(Comparator.comparing(s -> s.getTransitionSubOrder())).collect(Collectors.toList());
            transitionDto = nextTransitionDto(nextTransitionDtoList, workflowName, requestId);
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
        WorkflowTransition workflowTransition = workflowTransitionRepository.findByWorkflowTransitionIdAndRequestId(transitionActionReqDto.getWorkflowTransitionId(), transitionActionReqDto.getRequestId());
        if(Objects.isNull(workflowTransition)){
            throw new InvalidInputException(new ErrorDetails(AppConstant.INVALID_WORKFLOW_TRANSITION, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow transition not found.With given workflow transition id and request id."));
        }
        TransitionMaster currentTransition = transitionMasterRepository.findById(workflowTransition.getTransitionId()).orElse(null);

        validateUserRole(transitionActionReqDto.getActionBy(), currentTransition.getNextRoleId());
        if(AppConstant.COMPLETED_TYPE.equalsIgnoreCase(workflowTransition.getStatus())){
            throw new BusinessException(new ErrorDetails(AppConstant.INVALID_ACTION, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow already completed."));
        }

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

    @Override
    @Transactional
    public WorkflowTransitionDto submitWorkflow(Integer workflowTransitionId, Integer actionBy, String remarks) {
        userService.validateUser(actionBy);
        WorkflowTransition currentWorkflowTransition = workflowTransitionRepository.findById(workflowTransitionId).orElse(null);
        if(Objects.isNull(currentWorkflowTransition)){
            throw new InvalidInputException(new ErrorDetails(AppConstant.INVALID_WORKFLOW_TRANSITION, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Workflow transition not found.With given workflow transition id and request id."));
        }

        TransitionDto nextTransition =  nextTransition(currentWorkflowTransition.getWorkflowId(), currentWorkflowTransition.getWorkflowName() ,roleNameByUserId(actionBy), currentWorkflowTransition.getRequestId());
        if(Objects.isNull(nextTransition)){
            throw new InvalidInputException(new ErrorDetails(AppConstant.NEXT_TRANSITION_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Error occurred at approval. No next transition found."));
        }

        currentWorkflowTransition.setNextAction(AppConstant.COMPLETED_TYPE);
        workflowTransitionRepository.save(currentWorkflowTransition);

        WorkflowTransition nextWorkflowTransition = new WorkflowTransition();
        nextWorkflowTransition.setWorkflowId(nextTransition.getWorkflowId());
        nextWorkflowTransition.setTransitionId(nextTransition.getTransitionId());
        nextWorkflowTransition.setTransitionOrder(nextTransition.getTransitionOrder());
        nextWorkflowTransition.setTransitionSubOrder(nextTransition.getTransitionSubOrder());
        nextWorkflowTransition.setWorkflowName(nextTransition.getWorkflowName());
        nextWorkflowTransition.setStatus(AppConstant.IN_PROGRESS_TYPE);
        nextWorkflowTransition.setNextAction(AppConstant.PENDING_TYPE);
        nextWorkflowTransition.setAction(AppConstant.APPROVE_TYPE);
        nextWorkflowTransition.setRemarks(remarks);
        nextWorkflowTransition.setModifiedBy(actionBy);
        nextWorkflowTransition.setModificationDate(new Date());
        nextWorkflowTransition.setRequestId(currentWorkflowTransition.getRequestId());
        nextWorkflowTransition.setCreatedBy(currentWorkflowTransition.getCreatedBy());
        nextWorkflowTransition.setCreatedDate(currentWorkflowTransition.getCreatedDate());
        nextWorkflowTransition.setCurrentRole(nextTransition.getCurrentRoleName());
        nextWorkflowTransition.setNextRole(nextTransition.getNextRoleName());
        nextWorkflowTransition.setWorkflowSequence(currentWorkflowTransition.getWorkflowSequence()  + 1);

        workflowTransitionRepository.save(nextWorkflowTransition);


        return null;
    }

    @Override
    public List<WorkflowTransitionDto> approvedWorkflowTransition(Integer modifiedBy) {
        List<WorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();
        List<WorkflowTransition> workflowTransitionList = workflowTransitionRepository.findByModifiedBy(modifiedBy);
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()) {
            workflowTransitionDtoList = workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getWorkflowSequence).reversed()).map(e -> {
                return mapWorkflowTransitionDto(e);
            }).collect(Collectors.toList());
        }
        return workflowTransitionDtoList;
    }

    @Override
    public List<SubWorkflowTransitionDto> getSubWorkflowTransition(Integer modifiedBy) {
        List<SubWorkflowTransitionDto> workflowTransitionDtoList = new ArrayList<>();

        List<SubWorkflowTransition> subWorkflowTransitionList = subWorkflowTransitionRepository.findByActionOn(modifiedBy);
        if(Objects.nonNull(subWorkflowTransitionList) && !subWorkflowTransitionList.isEmpty()){
            workflowTransitionDtoList =  subWorkflowTransitionList.stream().map(e -> {
                SubWorkflowTransitionDto subWorkflowTransitionDto = new SubWorkflowTransitionDto();
                subWorkflowTransitionDto.setSubWorkflowTransitionId(e.getSubWorkflowTransitionId());
                subWorkflowTransitionDto.setWorkflowId(e.getWorkflowId());
                subWorkflowTransitionDto.setWorkflowName(e.getWorkflowName());
                subWorkflowTransitionDto.setModifiedBy(e.getModifiedBy());
                subWorkflowTransitionDto.setWorkflowSequence(e.getWorkflowSequence());
                subWorkflowTransitionDto.setStatus(e.getStatus());
                subWorkflowTransitionDto.setRemarks(e.getRemarks());
                subWorkflowTransitionDto.setAction(e.getAction());
                subWorkflowTransitionDto.setActionOn(e.getActionOn());
                subWorkflowTransitionDto.setRequestId(e.getRequestId());
                subWorkflowTransitionDto.setCreatedBy(e.getCreatedBy());
                subWorkflowTransitionDto.setCreatedDate(e.getCreatedDate());
                subWorkflowTransitionDto.setModificationDate(e.getModificationDate());

                return subWorkflowTransitionDto;
            }).collect(Collectors.toList());
        }
        return workflowTransitionDtoList;
    }

    @Override
    @Transactional
    public void approveSubWorkflow(Integer subWorkflowTransitionId) {
        if(Objects.nonNull(subWorkflowTransitionId)){
            Optional<SubWorkflowTransition> subWorkflowTransitionOptional = subWorkflowTransitionRepository.findById(subWorkflowTransitionId);
            if(subWorkflowTransitionOptional.isPresent()){
                SubWorkflowTransition subWorkflowTransition = subWorkflowTransitionOptional.get();
                subWorkflowTransition.setStatus(AppConstant.APPROVE_TYPE);
                subWorkflowTransition.setAction(AppConstant.COMPLETED_TYPE);
                subWorkflowTransition.setModificationDate(new Date());

                subWorkflowTransitionRepository.save(subWorkflowTransition);

            }else{
                throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Invalid sub workflow transition id."));
            }
        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid sub workflow transition id."));
        }
    }

    private void requestChangeTransition(WorkflowTransition currentWorkflowTransition, TransitionMaster currentTransition, TransitionActionReqDto transitionActionReqDto) {
        if(Objects.nonNull(transitionActionReqDto.getAssignmentRole())){
            validateAssignmentRole(transitionActionReqDto.getAssignmentRole(), currentWorkflowTransition);

            currentWorkflowTransition.setNextAction(AppConstant.COMPLETED_TYPE);
            workflowTransitionRepository.save(currentWorkflowTransition);

            WorkflowTransition latestWorkflowTransition = getLatestWorkflowTransiton(currentWorkflowTransition, transitionActionReqDto);

            WorkflowTransition nextWorkflowTransition = new WorkflowTransition();
            nextWorkflowTransition.setWorkflowId(latestWorkflowTransition.getWorkflowId());
            nextWorkflowTransition.setTransitionId(latestWorkflowTransition.getTransitionId());
            nextWorkflowTransition.setTransitionOrder(latestWorkflowTransition.getTransitionOrder());
            nextWorkflowTransition.setTransitionSubOrder(latestWorkflowTransition.getTransitionSubOrder());
            nextWorkflowTransition.setWorkflowName(latestWorkflowTransition.getWorkflowName());
            nextWorkflowTransition.setStatus(AppConstant.IN_PROGRESS_TYPE);
            nextWorkflowTransition.setNextAction(AppConstant.PENDING_TYPE);
            nextWorkflowTransition.setAction(transitionActionReqDto.getAction());
            nextWorkflowTransition.setRemarks(null);
            nextWorkflowTransition.setModifiedBy(transitionActionReqDto.getActionBy());
            nextWorkflowTransition.setModificationDate(new Date());
            nextWorkflowTransition.setRequestId(currentWorkflowTransition.getRequestId());
            nextWorkflowTransition.setCreatedBy(currentWorkflowTransition.getCreatedBy());
            nextWorkflowTransition.setCreatedDate(currentWorkflowTransition.getCreatedDate());
            nextWorkflowTransition.setCurrentRole(currentWorkflowTransition.getNextRole());
            if(transitionActionReqDto.getAssignmentRole().equalsIgnoreCase("Request Creator")) {
                nextWorkflowTransition.setNextRole(latestWorkflowTransition.getCurrentRole());
            }else{
                nextWorkflowTransition.setNextRole(latestWorkflowTransition.getNextRole());
            }
            nextWorkflowTransition.setWorkflowSequence(currentWorkflowTransition.getWorkflowSequence()  + 1);

            workflowTransitionRepository.save(nextWorkflowTransition);
        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid assignment role."));
        }
    }

    private WorkflowTransition getLatestWorkflowTransiton(WorkflowTransition currentWorkflowTransition, TransitionActionReqDto transitionActionReqDto) {
        WorkflowTransition workflowTransition = null;
        List<WorkflowTransition> workflowTransitionList = workflowTransitionRepository.findByWorkflowIdAndRequestIdAndNextRole(currentWorkflowTransition.getWorkflowId(), currentWorkflowTransition.getRequestId(), transitionActionReqDto.getAssignmentRole());
        if(Objects.nonNull(workflowTransitionList) && !workflowTransitionList.isEmpty()){
            workflowTransition =  workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getWorkflowTransitionId).reversed()).limit(1).collect(Collectors.toList()).get(0);
        }else if(transitionActionReqDto.getAssignmentRole().equalsIgnoreCase("Request Creator")){
            workflowTransitionList = workflowTransitionRepository.findByWorkflowIdAndRequestIdAndCurrentRole(currentWorkflowTransition.getWorkflowId(), currentWorkflowTransition.getRequestId(), transitionActionReqDto.getAssignmentRole());
            if(Objects.nonNull(workflowTransitionList)){
                workflowTransition =  workflowTransitionList.get(0);
            }
        }
        return workflowTransition;
    }

    private void validateAssignmentRole(String assignmentRole, WorkflowTransition workflowTransition) {
        List<String> allPreviousRole = allPreviousRoleWorkflowTransition(workflowTransition.getWorkflowId(), workflowTransition.getRequestId());
        if(!allPreviousRole.isEmpty() && allPreviousRole.contains(assignmentRole)){
        }else{
            throw new InvalidInputException(new ErrorDetails(AppConstant.USER_INVALID_INPUT, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid previous assignment role."));
        }
    }

    private void rejectTransition(WorkflowTransition currentWorkflowTransition, TransitionMaster currentTransition, TransitionActionReqDto transitionActionReqDto) {
        //update currentWorkflowTransition and save
        currentWorkflowTransition.setNextAction(AppConstant.COMPLETED_TYPE);
        workflowTransitionRepository.save(currentWorkflowTransition);

        WorkflowTransition nextWorkflowTransition = new WorkflowTransition();
        nextWorkflowTransition.setWorkflowId(currentWorkflowTransition.getWorkflowId());
        nextWorkflowTransition.setTransitionId(currentWorkflowTransition.getTransitionId());
        nextWorkflowTransition.setTransitionOrder(currentWorkflowTransition.getTransitionOrder());
        nextWorkflowTransition.setWorkflowName(currentWorkflowTransition.getWorkflowName());
        nextWorkflowTransition.setCreatedDate(currentWorkflowTransition.getCreatedDate());
        nextWorkflowTransition.setCreatedBy(currentWorkflowTransition.getCreatedBy());
        nextWorkflowTransition.setTransitionSubOrder(currentWorkflowTransition.getTransitionSubOrder());
        nextWorkflowTransition.setModifiedBy(transitionActionReqDto.getActionBy());
        nextWorkflowTransition.setRequestId(currentWorkflowTransition.getRequestId());
        nextWorkflowTransition.setModificationDate(new Date());
        nextWorkflowTransition.setStatus(AppConstant.CANCELED_TYPE);
        nextWorkflowTransition.setAction(transitionActionReqDto.getAction());
        nextWorkflowTransition.setNextAction(null);
        nextWorkflowTransition.setRemarks(transitionActionReqDto.getRemarks());
        nextWorkflowTransition.setCurrentRole(currentWorkflowTransition.getNextRole());
        nextWorkflowTransition.setWorkflowSequence(currentWorkflowTransition.getWorkflowSequence()  + 1);

        workflowTransitionRepository.save(nextWorkflowTransition);
    }

    private WorkflowTransition getPrevWorkflowTransition(WorkflowTransition workflowTransition) {
        List<WorkflowTransition> workflowTransitionList = workflowTransitionRepository.findByRequestId(workflowTransition.getRequestId());
        if(workflowTransitionList.size() == 1){
             return workflowTransitionList.get(0);
        }else{
            return workflowTransitionList.stream().sorted(Comparator.comparing(WorkflowTransition::getWorkflowTransitionId).reversed()).skip(1).findFirst().get();
        }
    }

    private void approveTransition(WorkflowTransition currentWorkflowTransition, TransitionMaster currentTransition, TransitionActionReqDto transitionActionReqDto) {
        TransitionDto nextTransition = null;
        WorkflowTransition nextWorkflowTransition = null;

        if(Objects.isNull(currentTransition.getNextRoleId())){
            currentWorkflowTransition.setNextAction(AppConstant.COMPLETED_TYPE);
            workflowTransitionRepository.save(currentWorkflowTransition);

            nextWorkflowTransition = new WorkflowTransition();
            nextWorkflowTransition.setWorkflowId(currentWorkflowTransition.getWorkflowId());
            nextWorkflowTransition.setTransitionId(currentWorkflowTransition.getTransitionId());
            nextWorkflowTransition.setTransitionOrder(currentWorkflowTransition.getTransitionOrder());
            nextWorkflowTransition.setWorkflowName(currentWorkflowTransition.getWorkflowName());
            nextWorkflowTransition.setCreatedDate(currentWorkflowTransition.getCreatedDate());
            nextWorkflowTransition.setCreatedBy(currentWorkflowTransition.getCreatedBy());
            nextWorkflowTransition.setTransitionSubOrder(currentWorkflowTransition.getTransitionSubOrder());
            nextWorkflowTransition.setModifiedBy(transitionActionReqDto.getActionBy());
            nextWorkflowTransition.setModificationDate(new Date());
            nextWorkflowTransition.setStatus(AppConstant.COMPLETED_TYPE);
            nextWorkflowTransition.setAction(transitionActionReqDto.getAction());
            nextWorkflowTransition.setNextAction(null);
            nextWorkflowTransition.setRemarks(transitionActionReqDto.getRemarks());
            nextWorkflowTransition.setCurrentRole(currentWorkflowTransition.getNextRole());
            nextWorkflowTransition.setWorkflowSequence(currentWorkflowTransition.getWorkflowSequence()  + 1);

            workflowTransitionRepository.save(nextWorkflowTransition);
        }else{
            nextTransition =  nextTransition(currentTransition.getWorkflowId(), currentWorkflowTransition.getWorkflowName() ,roleNameByUserId(transitionActionReqDto.getActionBy()), currentWorkflowTransition.getRequestId());
            if(Objects.isNull(nextTransition)){
                throw new InvalidInputException(new ErrorDetails(AppConstant.NEXT_TRANSITION_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Error occurred at approval. No next transition found."));
            }

            //update currentWorkflowTransition nextSatus and save
            currentWorkflowTransition.setNextAction(AppConstant.COMPLETED_TYPE);
            workflowTransitionRepository.save(currentWorkflowTransition);

            nextWorkflowTransition = new WorkflowTransition();
            nextWorkflowTransition.setWorkflowId(nextTransition.getWorkflowId());
            nextWorkflowTransition.setTransitionId(nextTransition.getTransitionId());
            nextWorkflowTransition.setTransitionOrder(nextTransition.getTransitionOrder());
            nextWorkflowTransition.setTransitionSubOrder(nextTransition.getTransitionSubOrder());
            nextWorkflowTransition.setWorkflowName(nextTransition.getWorkflowName());
            if(Objects.isNull(nextTransition.getNextRoleId())){
                nextWorkflowTransition.setStatus(AppConstant.COMPLETED_TYPE);
                nextWorkflowTransition.setNextAction(null);
            }else{
                nextWorkflowTransition.setStatus(AppConstant.IN_PROGRESS_TYPE);
                nextWorkflowTransition.setNextAction(AppConstant.PENDING_TYPE);
            }

            nextWorkflowTransition.setAction(transitionActionReqDto.getAction());
            nextWorkflowTransition.setRemarks(transitionActionReqDto.getRemarks());
            nextWorkflowTransition.setModifiedBy(transitionActionReqDto.getActionBy());
            nextWorkflowTransition.setModificationDate(new Date());
            nextWorkflowTransition.setRequestId(currentWorkflowTransition.getRequestId());
            nextWorkflowTransition.setCreatedBy(currentWorkflowTransition.getCreatedBy());
            nextWorkflowTransition.setCreatedDate(currentWorkflowTransition.getCreatedDate());
            nextWorkflowTransition.setCurrentRole(nextTransition.getCurrentRoleName());
            nextWorkflowTransition.setNextRole(nextTransition.getNextRoleName());
            nextWorkflowTransition.setWorkflowSequence(currentWorkflowTransition.getWorkflowSequence()  + 1);

            workflowTransitionRepository.save(nextWorkflowTransition);
        }

        //validation for tender workflow
        if(WorkflowName.TENDER.getValue().equalsIgnoreCase(currentWorkflowTransition.getWorkflowName())) {
            validateTenderWorkFlow(nextTransition, currentWorkflowTransition, nextWorkflowTransition);
        }

    }

    private void validateTenderWorkFlow(TransitionDto nextTransition, WorkflowTransition currentWorkflowTransition, WorkflowTransition nextWorkflowTransition ) {
        if((nextWorkflowTransition.getCurrentRole().equalsIgnoreCase("Tender Evaluator")) || (nextWorkflowTransition.getCurrentRole().equalsIgnoreCase("Purchase Dept") && nextWorkflowTransition.getNextRole().equalsIgnoreCase("Purchase Dept"))){
            TenderWithIndentResponseDTO tenderWithIndentResponseDTO = tenderRequestService.getTenderRequestById(currentWorkflowTransition.getRequestId());
            if(Objects.nonNull(tenderWithIndentResponseDTO) && Objects.nonNull(tenderWithIndentResponseDTO.getIndentResponseDTO()) && !tenderWithIndentResponseDTO.getIndentResponseDTO().isEmpty()){
                List<IndentCreationResponseDTO> indentResponseDTO = tenderWithIndentResponseDTO.getIndentResponseDTO();
                List<Integer> indenterList = indentResponseDTO.stream().map(e -> e.getCreatedBy()).collect(Collectors.toList());
                if(Objects.nonNull(indenterList) && !indenterList.isEmpty()){
                    AtomicInteger seq = new AtomicInteger(1);
                    indenterList.forEach(e -> {
                        SubWorkflowTransition subWorkflowTransition = new SubWorkflowTransition();
                        subWorkflowTransition.setWorkflowId(currentWorkflowTransition.getWorkflowId());
                        subWorkflowTransition.setWorkflowTransitionId(nextWorkflowTransition.getWorkflowTransitionId());
                        subWorkflowTransition.setAction(AppConstant.PENDING_TYPE);
                        subWorkflowTransition.setWorkflowName(nextWorkflowTransition.getWorkflowName());
                        subWorkflowTransition.setActionOn(e);
                        subWorkflowTransition.setCreatedBy(nextWorkflowTransition.getModifiedBy());
                        subWorkflowTransition.setWorkflowSequence(seq.get());
                        subWorkflowTransition.setRequestId(nextWorkflowTransition.getRequestId());
                        subWorkflowTransition.setStatus(AppConstant.PENDING_TYPE);
                        subWorkflowTransition.setCreatedDate(new Date());
                        seq.getAndIncrement();

                        subWorkflowTransitionRepository.save(subWorkflowTransition);
                    });
                }
            }
        }
        if(nextWorkflowTransition.getCurrentRole().equalsIgnoreCase("Purchase Dept") && Objects.isNull(nextWorkflowTransition.getNextRole())){
            List<SubWorkflowTransition> subWorkflowTransitionList = subWorkflowTransitionRepository.findByWorkflowTransitionIdAndStatus(currentWorkflowTransition.getWorkflowTransitionId(), AppConstant.PENDING_TYPE);
            if(Objects.nonNull(subWorkflowTransitionList) && !subWorkflowTransitionList.isEmpty()){
                throw new InvalidInputException(new ErrorDetails(AppConstant.NEXT_TRANSITION_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Error occurred at approval. All indentor not performed action for this workflow to approve."));
            }

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

    private TransitionDto nextTransitionDto(List<TransitionDto> nextTransitionDtoList, String workflowName, String requestId) {
        TransitionDto transitionDto = null;
        List<Integer> conditionIdList = nextTransitionDtoList.stream().filter(f -> Objects.nonNull(f.getConditionId())).map(e -> e.getConditionId()).collect(Collectors.toList());

        //for without any condition move and have only one next move
        if (conditionIdList.isEmpty() && nextTransitionDtoList.size() == 1) {
            return nextTransitionDtoList.get(0);
        } else {
                                                                                                                                        List<TransitionConditionMaster> transitionConditionMasterList = transitionConditionMasterRepository.findAllById(conditionIdList);

            switch (workflowName.toUpperCase()) {
                case "INDENT WORKFLOW":
                    //get indent data here
                    IndentCreationResponseDTO indentCreationResponseDTO = indentCreationService.getIndentById(requestId);
                    for(TransitionDto dto : nextTransitionDtoList){
                        Integer conditionId = dto.getConditionId();
                        if (Objects.nonNull(conditionId)) {
                            TransitionConditionMaster transitionConditionMaster = transitionConditionMasterList.stream().filter(f -> f.getConditionId().equals(dto.getConditionId())).findFirst().get();
                            String conditionKey = transitionConditionMaster.getConditionKey();
                            String conditionValue = transitionConditionMaster.getConditionValue();
                            Object dataValue = null;
                            boolean conditionCheckFlag = Boolean.FALSE;
                            if(conditionKey.equalsIgnoreCase("ProjectName")){
                                dataValue =  indentCreationResponseDTO.getProjectName();
                                conditionCheckFlag = Objects.nonNull(dataValue);
                            }else if(conditionKey.equalsIgnoreCase("MaterialCategory")){
                                dataValue =  indentCreationResponseDTO.getMaterialCategory();
                                conditionCheckFlag = ((String) dataValue).equalsIgnoreCase(conditionValue);
                            }else if(conditionKey.equalsIgnoreCase("ConsignesLocation")){
                                dataValue =  indentCreationResponseDTO.getConsignesLocation();
                                conditionCheckFlag = ((String) dataValue).equalsIgnoreCase(conditionValue);
                            }else if(conditionKey.equalsIgnoreCase("TotalPriceOfAllMaterials")){
                                dataValue =  indentCreationResponseDTO.getTotalPriceOfAllMaterials();
                                conditionCheckFlag = ((BigDecimal) dataValue).doubleValue() <= Double.valueOf(conditionValue);
                            }
                            if(conditionCheckFlag){
                                transitionDto = dto;
                                break;
                            }
                        }
                    }
                    break;
                case "CONTINGENCY PURCHASE WORKFLOW":
                    ContigencyPurchaseResponseDto contigencyPurchaseResponseDto = contigencyPurchaseService.getContigencyPurchaseById(requestId);
                    for(TransitionDto dto : nextTransitionDtoList){
                        Integer conditionId = dto.getConditionId();
                        if (Objects.nonNull(conditionId)) {
                            TransitionConditionMaster transitionConditionMaster = transitionConditionMasterList.stream().filter(f -> f.getConditionId().equals(dto.getConditionId())).findFirst().get();
                            String conditionKey = transitionConditionMaster.getConditionKey();
                            String conditionValue = transitionConditionMaster.getConditionValue();
                            Object dataValue = null;
                            boolean conditionCheckFlag = Boolean.FALSE;
                            if(conditionKey.equalsIgnoreCase("ProjectName")){
                                dataValue =  contigencyPurchaseResponseDto.getProjectName();
                                if("Empty".equalsIgnoreCase(conditionValue)){
                                    conditionCheckFlag = Objects.isNull(dataValue);
                                }else if("Not Empty".equalsIgnoreCase(conditionValue)) {
                                    conditionCheckFlag = Objects.nonNull(dataValue);
                                }
                            }

                            if(conditionCheckFlag){
                                transitionDto = dto;
                                break;
                            }
                        }
                    }
                    break;
                    case "TENDER WORKFLOW":
                        TenderWithIndentResponseDTO tenderWithIndentResponseDTO = tenderRequestService.getTenderRequestById(requestId);
                        for(TransitionDto dto : nextTransitionDtoList){
                            Integer conditionId = dto.getConditionId();
                            if (Objects.nonNull(conditionId)) {
                                TransitionConditionMaster transitionConditionMaster = transitionConditionMasterList.stream().filter(f -> f.getConditionId().equals(dto.getConditionId())).findFirst().get();
                                String conditionKey = transitionConditionMaster.getConditionKey();
                                String conditionValue = transitionConditionMaster.getConditionValue();
                                Object dataValue = null;
                                boolean conditionCheckFlag = Boolean.FALSE;
                                if(conditionKey.equalsIgnoreCase("totalTenderValue")){
                                    dataValue =  tenderWithIndentResponseDTO.getTotalTenderValue();
                                    conditionCheckFlag = ((BigDecimal) dataValue).doubleValue() <= Double.valueOf(conditionValue);
                                }else if(conditionKey.equalsIgnoreCase("bidType")){
                                    dataValue =  tenderWithIndentResponseDTO.getBidType();
                                    conditionCheckFlag = ((String) dataValue).equalsIgnoreCase(conditionValue);
                                }
                                if(conditionCheckFlag){
                                    transitionDto = dto;
                                    break;
                                }
                            }
                        }
                        break;
                    case "SO WORKFLOW":
                        soWithTenderAndIndentResponseDTO soWithTenderAndIndentResponseDTO = serviceOrderService.getServiceOrderById(requestId);
                        for(TransitionDto dto : nextTransitionDtoList){
                            Integer conditionId = dto.getConditionId();
                            if (Objects.nonNull(conditionId)) {
                                TransitionConditionMaster transitionConditionMaster = transitionConditionMasterList.stream().filter(f -> f.getConditionId().equals(dto.getConditionId())).findFirst().get();
                                String conditionKey = transitionConditionMaster.getConditionKey();
                                String conditionValue = transitionConditionMaster.getConditionValue();
                                Object dataValue = null;
                                boolean conditionCheckFlag = Boolean.FALSE;
                                if(conditionKey.equalsIgnoreCase("ProjectName")){
                                    dataValue =  soWithTenderAndIndentResponseDTO.getProjectName();
                                    conditionCheckFlag = Objects.nonNull(dataValue);
                                }else if(conditionKey.equalsIgnoreCase("TotalPriceOfAllMaterials")){
                                    dataValue =  soWithTenderAndIndentResponseDTO.getTotalValueOfSo();
                                    conditionCheckFlag = ((BigDecimal) dataValue).doubleValue() <= Double.valueOf(conditionValue);
                                }
                                if(conditionCheckFlag){
                                    transitionDto = dto;
                                    break;
                                }
                            }
                        }
                        break;
                case "WO WORKFLOW":
                    woWithTenderAndIndentResponseDTO woWithTenderAndIndentResponseDTO = workOrderService.getWorkOrderById(requestId);
                    for(TransitionDto dto : nextTransitionDtoList){
                        Integer conditionId = dto.getConditionId();
                        if (Objects.nonNull(conditionId)) {
                            TransitionConditionMaster transitionConditionMaster = transitionConditionMasterList.stream().filter(f -> f.getConditionId().equals(dto.getConditionId())).findFirst().get();
                            String conditionKey = transitionConditionMaster.getConditionKey();
                            String conditionValue = transitionConditionMaster.getConditionValue();
                            Object dataValue = null;
                            boolean conditionCheckFlag = Boolean.FALSE;
                            if(conditionKey.equalsIgnoreCase("ProjectName")){
                                dataValue =  woWithTenderAndIndentResponseDTO.getProjectName();
                                conditionCheckFlag = Objects.nonNull(dataValue);
                            }else if(conditionKey.equalsIgnoreCase("TotalPriceOfAllMaterials")){
                                dataValue =  woWithTenderAndIndentResponseDTO.getTotalValueOfWo();
                                conditionCheckFlag = ((BigDecimal) dataValue).doubleValue() <= Double.valueOf(conditionValue);
                            }
                            if(conditionCheckFlag){
                                transitionDto = dto;
                                break;
                            }
                        }
                    }
                    break;
                case "PO WORKFLOW":
                    poWithTenderAndIndentResponseDTO poWithTenderAndIndentResponseDTO = purchaseOrderService.getPurchaseOrderById(requestId);
                    for(TransitionDto dto : nextTransitionDtoList){
                        Integer conditionId = dto.getConditionId();
                        if (Objects.nonNull(conditionId)) {
                            TransitionConditionMaster transitionConditionMaster = transitionConditionMasterList.stream().filter(f -> f.getConditionId().equals(dto.getConditionId())).findFirst().get();
                            String conditionKey = transitionConditionMaster.getConditionKey();
                            String conditionValue = transitionConditionMaster.getConditionValue();
                            Object dataValue = null;
                            boolean conditionCheckFlag = Boolean.FALSE;
                            if(conditionKey.equalsIgnoreCase("ProjectName")){
                                dataValue =  poWithTenderAndIndentResponseDTO.getProjectName();
                                conditionCheckFlag = Objects.nonNull(dataValue);
                            }else if(conditionKey.equalsIgnoreCase("TotalPriceOfAllMaterials")){
                                dataValue =  poWithTenderAndIndentResponseDTO.getTotalValueOfPo();
                                conditionCheckFlag = ((BigDecimal) dataValue).doubleValue() <= Double.valueOf(conditionValue);
                            }
                            if(conditionCheckFlag){
                                transitionDto = dto;
                                break;
                            }
                        }
                    }
                    break;
                    //add more case here

            }
        }
        return transitionDto;
    }
    public List<String> getApprovedIndents() {
        return workflowTransitionRepository.findApprovedIndentRequestIds();
    }

}
