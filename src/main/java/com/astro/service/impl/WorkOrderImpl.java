package com.astro.service.impl;




import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderMaterialResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderResponseDTO;
import com.astro.entity.ProcurementModule.ServiceOrder;
import com.astro.entity.ProcurementModule.WorkOrder;
import com.astro.entity.ProcurementModule.WorkOrderMaterial;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.ProcurementModule.WorkOrder.WorkOrderMaterialRepository;
import com.astro.repository.ProcurementModule.WorkOrder.WorkOrderRepository;
import com.astro.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderImpl implements WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderMaterialRepository workOrderMaterialRepository;
    public WorkOrderResponseDTO createWorkOrder(WorkOrderRequestDTO workOrderRequestDTO) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setTenderId(workOrderRequestDTO.getTenderId());
        workOrder.setConsignesAddress(workOrderRequestDTO.getConsignesAddress());
        workOrder.setBillingAddress(workOrderRequestDTO.getBillingAddress());
        workOrder.setJobCompletionPeriod(workOrderRequestDTO.getJobCompletionPeriod());
        workOrder.setIfLdClauseApplicable(workOrderRequestDTO.getIfLdClauseApplicable());
        workOrder.setIncoTerms(workOrderRequestDTO.getIncoTerms());
        workOrder.setPaymentTerms(workOrderRequestDTO.getPaymentTerms());
        workOrder.setVendorName(workOrderRequestDTO.getVendorName());
        workOrder.setVendorAddress(workOrderRequestDTO.getVendorAddress());
        workOrder.setApplicablePBGToBeSubmitted(workOrderRequestDTO.getApplicablePBGToBeSubmitted());
        workOrder.setVendorsAccountNo(workOrderRequestDTO.getVendorsAccountNo());
        workOrder.setVendorsZRSCCode(workOrderRequestDTO.getVendorsZRSCCode());
        workOrder.setVendorsAccountName(workOrderRequestDTO.getVendorsAccountName());
        workOrder.setCreatedBy(workOrderRequestDTO.getCreatedBy());
        workOrder.setUpdatedBy(workOrderRequestDTO.getUpdatedBy());
        List<WorkOrderMaterial>workOrderMaterials = workOrderRequestDTO.getMaterials().stream()
                .map(dto -> {
                    WorkOrderMaterial material = new WorkOrderMaterial();
                    material.setWorkCode(dto.getWorkCode());
                    material.setWorkDescription(dto.getWorkDescription());
                    material.setQuantity(dto.getQuantity());
                    material.setRate(dto.getRate());
                    material.setExchangeRate(dto.getExchangeRate());
                    material.setCurrency(dto.getCurrency());
                    material.setGst(dto.getGst());
                    material.setDuties(dto.getDuties());
                    material.setBudgetCode(dto.getBudgetCode()); // Associate with PurchaseOrder
                    return material;
                })
                .collect(Collectors.toList());
        workOrder.setMaterials(workOrderMaterials);
        workOrderRepository.save(workOrder);

        return mapToResponseDTO(workOrder);
    }
    public WorkOrderResponseDTO updateWorkOrder(Long id, WorkOrderRequestDTO workOrderRequestDTO) {
        WorkOrder existingWorkOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Work order not found for the provided asset ID.")
                ));
        existingWorkOrder.setTenderId(workOrderRequestDTO.getTenderId());
        existingWorkOrder.setConsignesAddress(workOrderRequestDTO.getConsignesAddress());
        existingWorkOrder.setBillingAddress(workOrderRequestDTO.getBillingAddress());
        existingWorkOrder.setJobCompletionPeriod(workOrderRequestDTO.getJobCompletionPeriod());
        existingWorkOrder.setIfLdClauseApplicable(workOrderRequestDTO.getIfLdClauseApplicable());
        existingWorkOrder.setIncoTerms(workOrderRequestDTO.getIncoTerms());
        existingWorkOrder.setPaymentTerms(workOrderRequestDTO.getPaymentTerms());
        existingWorkOrder.setVendorName(workOrderRequestDTO.getVendorName());
        existingWorkOrder.setVendorAddress(workOrderRequestDTO.getVendorAddress());
        existingWorkOrder.setApplicablePBGToBeSubmitted(workOrderRequestDTO.getApplicablePBGToBeSubmitted());
        existingWorkOrder.setVendorsAccountNo(workOrderRequestDTO.getVendorsAccountNo());
        existingWorkOrder.setVendorsZRSCCode(workOrderRequestDTO.getVendorsZRSCCode());
        existingWorkOrder.setVendorsAccountName(workOrderRequestDTO.getVendorsAccountName());

        existingWorkOrder.setUpdatedBy(workOrderRequestDTO.getUpdatedBy());
        existingWorkOrder.setCreatedBy(workOrderRequestDTO.getCreatedBy());
        // Update materials
        List<WorkOrderMaterial> updatedWorkMaterials = workOrderRequestDTO.getMaterials().stream()
                .map(dto -> {
                    WorkOrderMaterial material = new WorkOrderMaterial();
                    material.setWorkCode(dto.getWorkCode());
                    material.setWorkDescription(dto.getWorkDescription());
                    material.setQuantity(dto.getQuantity());
                    material.setRate(dto.getRate());
                    material.setExchangeRate(dto.getExchangeRate());
                    material.setCurrency(dto.getCurrency());
                    material.setGst(dto.getGst());
                    material.setDuties(dto.getDuties());
                    material.setBudgetCode(dto.getBudgetCode());
                    return material;
                })
                .collect(Collectors.toList());

        workOrderMaterialRepository.deleteAll(existingWorkOrder.getMaterials());
        existingWorkOrder.setMaterials(updatedWorkMaterials);
        workOrderRepository.save(existingWorkOrder);

        return mapToResponseDTO(existingWorkOrder);
    }

    public List<WorkOrderResponseDTO> getAllWorkOrders() {
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        return workOrders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public WorkOrderResponseDTO getWorkOrderById(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "work order not found for the provided asset ID.")
                ));
        return mapToResponseDTO(workOrder);
    }

    public void deleteWorkOrder(Long id) {

      WorkOrder workOrder=workOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Work order not found for the provided ID."
                        )
                ));
        try {
            workOrderRepository.delete(workOrder);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  wo."
                    ),
                    ex
            );
        }
    }


    private WorkOrderResponseDTO mapToResponseDTO(WorkOrder workOrder) {
        WorkOrderResponseDTO response = new WorkOrderResponseDTO();
        response.setId(workOrder.getId());
        response.setTenderId(workOrder.getTenderId());
        response.setConsignesAddress(workOrder.getConsignesAddress());
        response.setBillingAddress(workOrder.getBillingAddress());
        response.setJobCompletionPeriod(workOrder.getJobCompletionPeriod());
        response.setIfLdClauseApplicable(workOrder.getIfLdClauseApplicable());
        response.setIncoTerms(workOrder.getIncoTerms());
        response.setPaymentTerms(workOrder.getPaymentTerms());
        response.setVendorName(workOrder.getVendorName());
        response.setVendorAddress(workOrder.getVendorAddress());
        response.setApplicablePBGToBeSubmitted(workOrder.getApplicablePBGToBeSubmitted());
        response.setVendorsAccountNo(workOrder.getVendorsAccountNo());
        response.setVendorsZRSCCode(workOrder.getVendorsZRSCCode());
        response.setVendorsAccountName(workOrder.getVendorsAccountName());
        response.setCreatedBy(workOrder.getCreatedBy());
        response.setUpdatedBy(workOrder.getUpdatedBy());
        response.setCreatedDate(workOrder.getCreatedDate());
        response.setUpdatedDate(workOrder.getUpdatedDate());
        response.setMaterials(workOrder.getMaterials().stream()
                .map(dto -> {
                    WorkOrderMaterialResponseDTO material = new WorkOrderMaterialResponseDTO();
                    material.setId(dto.getId());
                    material.setWorkCode(dto.getWorkCode());
                    material.setWorkDescription(dto.getWorkDescription());
                    material.setQuantity(dto.getQuantity());
                    material.setRate(dto.getRate());
                    material.setExchangeRate(dto.getExchangeRate());
                    material.setCurrency(dto.getCurrency());
                    material.setGst(dto.getGst());
                    material.setDuties(dto.getDuties());
                    material.setBudgetCode(dto.getBudgetCode()); // Associate with PurchaseOrder
                    return material;
                })
                .collect(Collectors.toList()));
        return response;
    }


}
