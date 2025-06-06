package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderMaterialRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderMaterialResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderResponseDTO;

import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderAttributesDTO;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import com.astro.entity.ProcurementModule.ServiceOrder;
import com.astro.entity.ProcurementModule.ServiceOrderMaterial;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.ServiceOrderRepository.ServiceOrderMaterialRepository;
import com.astro.repository.ProcurementModule.ServiceOrderRepository.ServiceOrderRepository;
import com.astro.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;
    @Autowired
    private ServiceOrderMaterialRepository serviceOrderMaterialRepository;
    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO serviceOrderRequestDTO) {
        // Check if the indentorId already exists
        if (serviceOrderRepository.existsById(serviceOrderRequestDTO.getSoId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Service Order ID", "SO ID " + serviceOrderRequestDTO.getSoId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

        // Iterate over materialDetails and check if materialCode already exists
        for (ServiceOrderMaterialRequestDTO materialRequest : serviceOrderRequestDTO.getMaterials()) {
            if (serviceOrderMaterialRepository.existsById(materialRequest.getMaterialCode())) {
                ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Material Code",
                        "Material Code " + materialRequest.getMaterialCode() + " already exists.");
                throw new InvalidInputException(errorDetails);
            }
        }

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setSoId(serviceOrderRequestDTO.getSoId());
        serviceOrder.setTenderId(serviceOrderRequestDTO.getTenderId());
        serviceOrder.setConsignesAddress(serviceOrderRequestDTO.getConsignesAddress());
        serviceOrder.setBillingAddress(serviceOrderRequestDTO.getBillingAddress());
        serviceOrder.setJobCompletionPeriod(serviceOrderRequestDTO.getJobCompletionPeriod());
        serviceOrder.setIfLdClauseApplicable(serviceOrderRequestDTO.getIfLdClauseApplicable());
        serviceOrder.setIncoTerms(serviceOrderRequestDTO.getIncoTerms());
        serviceOrder.setPaymentTerms(serviceOrderRequestDTO.getPaymentTerms());
        serviceOrder.setVendorName(serviceOrderRequestDTO.getVendorName());
        serviceOrder.setVendorAddress(serviceOrderRequestDTO.getVendorAddress());
        serviceOrder.setApplicablePBGToBeSubmitted(serviceOrderRequestDTO.getApplicablePBGToBeSubmitted());
        serviceOrder.setVendorsAccountNo(serviceOrderRequestDTO.getVendorsAccountNo());
        serviceOrder.setVendorsZRSCCode(serviceOrderRequestDTO.getVendorsZRSCCode());
        serviceOrder.setVendorsAccountName(serviceOrderRequestDTO.getVendorsAccountName());
        serviceOrder.setCreatedBy(serviceOrderRequestDTO.getCreatedBy());
        serviceOrder.setUpdatedBy(serviceOrderRequestDTO.getUpdatedBy());
        List<ServiceOrderMaterial> serviceOrderMaterials = serviceOrderRequestDTO.getMaterials().stream()
            .map(dto -> {
                    ServiceOrderMaterial material = new ServiceOrderMaterial();
                    material.setMaterialCode(dto.getMaterialCode());
                    material.setMaterialDescription(dto.getMaterialDescription());
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

        serviceOrder.setMaterials(serviceOrderMaterials);
        serviceOrderRepository.save(serviceOrder);

        return mapToResponseDTO(serviceOrder);
    }
    public ServiceOrderResponseDTO updateServiceOrder(String soId, ServiceOrderRequestDTO serviceOrderRequestDTO) {
        ServiceOrder existingServiceOrder = serviceOrderRepository.findById(soId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Service order not found for the provided SO ID.")
                ));
        existingServiceOrder.setTenderId(serviceOrderRequestDTO.getTenderId());
        existingServiceOrder.setConsignesAddress(serviceOrderRequestDTO.getConsignesAddress());
        existingServiceOrder.setBillingAddress(serviceOrderRequestDTO.getBillingAddress());
        existingServiceOrder.setJobCompletionPeriod(serviceOrderRequestDTO.getJobCompletionPeriod());
        existingServiceOrder.setIfLdClauseApplicable(serviceOrderRequestDTO.getIfLdClauseApplicable());
        existingServiceOrder.setIncoTerms(serviceOrderRequestDTO.getIncoTerms());
        existingServiceOrder.setPaymentTerms(serviceOrderRequestDTO.getPaymentTerms());
        existingServiceOrder.setVendorName(serviceOrderRequestDTO.getVendorName());
        existingServiceOrder.setVendorAddress(serviceOrderRequestDTO.getVendorAddress());
        existingServiceOrder.setApplicablePBGToBeSubmitted(serviceOrderRequestDTO.getApplicablePBGToBeSubmitted());
        existingServiceOrder.setVendorsAccountNo(serviceOrderRequestDTO.getVendorsAccountNo());
        existingServiceOrder.setVendorsZRSCCode(serviceOrderRequestDTO.getVendorsZRSCCode());
        existingServiceOrder.setVendorsAccountName(serviceOrderRequestDTO.getVendorsAccountName());

        existingServiceOrder.setUpdatedBy(serviceOrderRequestDTO.getUpdatedBy());
        existingServiceOrder.setCreatedBy(serviceOrderRequestDTO.getCreatedBy());
        // Update materials
        List<ServiceOrderMaterial> updatedMaterials = serviceOrderRequestDTO.getMaterials().stream()
                .map(dto -> {
                    ServiceOrderMaterial material = new ServiceOrderMaterial();
                    material.setMaterialCode(dto.getMaterialCode());
                    material.setMaterialDescription(dto.getMaterialDescription());
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

        serviceOrderMaterialRepository.deleteAll(existingServiceOrder.getMaterials());
        existingServiceOrder.setMaterials(updatedMaterials);
        serviceOrderRepository.save(existingServiceOrder);

        return mapToResponseDTO(existingServiceOrder);
    }

    public List<ServiceOrderResponseDTO> getAllServiceOrders() {
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findAll();
        return serviceOrders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ServiceOrderResponseDTO getServiceOrderById(String soId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(soId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "service order not found for the provided soId.")
                ));
        return mapToResponseDTO(serviceOrder);
    }

    public void deleteServiceOrder(String soId) {

        ServiceOrder serviceOrder=serviceOrderRepository.findById(soId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "service order not found for the provided ID."
                        )
                ));
        try {
            serviceOrderRepository.delete(serviceOrder);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  so."
                    ),
                    ex
            );
        }

    }


    private ServiceOrderResponseDTO mapToResponseDTO(ServiceOrder ServiceOrder) {
        ServiceOrderResponseDTO response = new ServiceOrderResponseDTO();
        response.setSoId(ServiceOrder.getSoId());
        response.setTenderId(ServiceOrder.getTenderId());
        response.setConsignesAddress(ServiceOrder.getConsignesAddress());
        response.setBillingAddress(ServiceOrder.getBillingAddress());
        response.setJobCompletionPeriod(ServiceOrder.getJobCompletionPeriod());
        response.setIfLdClauseApplicable(ServiceOrder.getIfLdClauseApplicable());
        response.setIncoTerms(ServiceOrder.getIncoTerms());
        response.setPaymentTerms(ServiceOrder.getPaymentTerms());
        response.setVendorName(ServiceOrder.getVendorName());
        response.setVendorAddress(ServiceOrder.getVendorAddress());
        response.setApplicablePBGToBeSubmitted(ServiceOrder.getApplicablePBGToBeSubmitted());
        response.setVendorsAccountNo(ServiceOrder.getVendorsAccountNo());
        response.setVendorsZRSCCode(ServiceOrder.getVendorsZRSCCode());
        response.setVendorsAccountName(ServiceOrder.getVendorsAccountName());
        response.setCreatedBy(ServiceOrder.getCreatedBy());
        response.setUpdatedBy(ServiceOrder.getUpdatedBy());
        response.setCreatedDate(ServiceOrder.getCreatedDate());
        response.setUpdatedDate(ServiceOrder.getUpdatedDate());
        response.setMaterials(ServiceOrder.getMaterials().stream()
                .map(dto -> {
                    ServiceOrderMaterialResponseDTO material = new ServiceOrderMaterialResponseDTO();
                    material.setMaterialCode(dto.getMaterialCode());
                    material.setMaterialDescription(dto.getMaterialDescription());
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
