package com.astro.service.impl;



import com.astro.constant.AppConstant;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.ProcurementActivityReportResponse;
import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.*;
import com.astro.dto.workflow.VendorContractReportDTO;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import com.astro.entity.ProcurementModule.PurchaseOrderAttributes;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.entity.ProjectMaster;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.IndentIdRepository;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderAttributesRepository;

import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderRepository;

import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.repository.ProjectMasterRepository;
import com.astro.service.IndentCreationService;
import com.astro.service.PurchaseOrderService;
import com.astro.service.TenderRequestService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderAttributesRepository purchaseOrderAttributesRepository;
    @Autowired
    private IndentCreationService indentCreationService;
    @Autowired
    private TenderRequestService tenderRequestService;
    @Autowired
    private IndentIdRepository indentIdRepository;
    @Autowired
    private TenderRequestRepository tenderRequestRepository;
    @Autowired
    private ProjectMasterRepository projectMasterRepository;

    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO) {

        // Check if the indentorId already exists
        if (purchaseOrderRepository.existsById(purchaseOrderRequestDTO.getPoId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Purchase Order ID", "PO ID " + purchaseOrderRequestDTO.getPoId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }


/*
        // Iterate over materialDetails and check if materialCode already exists
        for (PurchaseOrderAttributesDTO materialRequest : purchaseOrderRequestDTO.getPurchaseOrderAttributes()) {
            if (purchaseOrderAttributesRepository.existsById(materialRequest.getMaterialCode())) {
                ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Material Code",
                        "Material Code " + materialRequest.getMaterialCode() + " already exists.");
                throw new InvalidInputException(errorDetails);
            }
        }

 */
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPoId(purchaseOrderRequestDTO.getPoId());
        purchaseOrder.setTenderId(purchaseOrderRequestDTO.getTenderId());
        purchaseOrder.setIndentId(purchaseOrderRequestDTO.getIndentId());
        purchaseOrder.setWarranty(purchaseOrderRequestDTO.getWarranty());
        purchaseOrder.setConsignesAddress(purchaseOrderRequestDTO.getConsignesAddress());
        purchaseOrder.setBillingAddress(purchaseOrderRequestDTO.getBillingAddress());
        purchaseOrder.setDeliveryPeriod(purchaseOrderRequestDTO.getDeliveryPeriod());
        purchaseOrder.setIfLdClauseApplicable(purchaseOrderRequestDTO.getIfLdClauseApplicable());
        purchaseOrder.setIncoTerms(purchaseOrderRequestDTO.getIncoTerms());
        purchaseOrder.setPaymentTerms(purchaseOrderRequestDTO.getPaymentTerms());
        purchaseOrder.setVendorName(purchaseOrderRequestDTO.getVendorName());
        purchaseOrder.setVendorAddress(purchaseOrderRequestDTO.getVendorAddress());
        purchaseOrder.setApplicablePbgToBeSubmitted(purchaseOrderRequestDTO.getApplicablePbgToBeSubmitted());
        purchaseOrder.setTransporterAndFreightForWarderDetails(purchaseOrderRequestDTO.getTransporterAndFreightForWarderDetails());
        purchaseOrder.setVendorAccountNumber(purchaseOrderRequestDTO.getVendorAccountNumber());
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsZfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        //  purchaseOrder.setTotalValueOfPo(purchaseOrderRequestDTO.getTotalValueOfPo());
        purchaseOrder.setProjectName(purchaseOrderRequestDTO.getProjectName());
        purchaseOrder.setCreatedBy(purchaseOrderRequestDTO.getCreatedBy());
        purchaseOrder.setUpdatedBy(purchaseOrderRequestDTO.getUpdatedBy());
        List<PurchaseOrderAttributes> purchaseOrderAttributes = purchaseOrderRequestDTO.getPurchaseOrderAttributes().stream()
                .map(dto -> {

                    PurchaseOrderAttributes attribute = new PurchaseOrderAttributes();
                    attribute.setMaterialCode(dto.getMaterialCode());
                    attribute.setPoId(purchaseOrderRequestDTO.getPoId());
                    attribute.setMaterialDescription(dto.getMaterialDescription());
                    attribute.setQuantity(dto.getQuantity());
                    attribute.setRate(dto.getRate());
                    attribute.setCurrency(dto.getCurrency());
                    attribute.setExchangeRate(dto.getExchangeRate());
                    attribute.setGst(dto.getGst());
                    attribute.setDuties(dto.getDuties());
                    attribute.setFreightCharge(dto.getFreightCharge());
                    attribute.setBudgetCode(dto.getBudgetCode());
                    attribute.setPurchaseOrder(purchaseOrder);  // Associate with PurchaseOrder
                    return attribute;
                })
                .collect(Collectors.toList());
        // purchaseOrder.setPurchaseOrderAttributes(purchaseOrderAttributes);
        // purchaseOrderRepository.save(purchaseOrder);
        // Set attributes and save order
        purchaseOrder.setPurchaseOrderAttributes(purchaseOrderAttributes);
        List<String> indentIds = indentIdRepository.findTenderWithIndent(purchaseOrder.getTenderId());

        // Calculate total tender value by summing totalPriceOfAllMaterials of all indents
        BigDecimal totalTenderValue = indentIds.stream()
                .map(indentCreationService::getIndentById) // Fetch Indent data
                .map(IndentCreationResponseDTO::getTotalPriceOfAllMaterials) // Extract total price
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up values
        purchaseOrder.setTotalValueOfPo(totalTenderValue);
        System.out.println("tottalTenderValue" + totalTenderValue);
        purchaseOrderRepository.save(purchaseOrder);
        return mapToResponseDTO(purchaseOrder);
    }

    public PurchaseOrderResponseDTO updatePurchaseOrder(String poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Purchase order not found for the provided asset ID.")
                ));

        purchaseOrder.setTenderId(purchaseOrderRequestDTO.getTenderId());
        purchaseOrder.setIndentId(purchaseOrderRequestDTO.getIndentId());
        purchaseOrder.setWarranty(purchaseOrderRequestDTO.getWarranty());
        purchaseOrder.setConsignesAddress(purchaseOrderRequestDTO.getConsignesAddress());
        purchaseOrder.setBillingAddress(purchaseOrderRequestDTO.getBillingAddress());
        purchaseOrder.setDeliveryPeriod(purchaseOrderRequestDTO.getDeliveryPeriod());
        purchaseOrder.setIfLdClauseApplicable(purchaseOrderRequestDTO.getIfLdClauseApplicable());
        purchaseOrder.setIncoTerms(purchaseOrderRequestDTO.getIncoTerms());
        purchaseOrder.setPaymentTerms(purchaseOrderRequestDTO.getPaymentTerms());
        purchaseOrder.setVendorName(purchaseOrderRequestDTO.getVendorName());
        purchaseOrder.setVendorAddress(purchaseOrderRequestDTO.getVendorAddress());
        purchaseOrder.setApplicablePbgToBeSubmitted(purchaseOrderRequestDTO.getApplicablePbgToBeSubmitted());
        purchaseOrder.setTransporterAndFreightForWarderDetails(purchaseOrderRequestDTO.getTransporterAndFreightForWarderDetails());
        purchaseOrder.setVendorAccountNumber(purchaseOrderRequestDTO.getVendorAccountNumber());
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsZfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        purchaseOrder.setProjectName(purchaseOrderRequestDTO.getProjectName());
        //   purchaseOrder.setTotalValueOfPo(purchaseOrderRequestDTO.getTotalValueOfPo());
        purchaseOrder.setUpdatedBy(purchaseOrderRequestDTO.getUpdatedBy());
        purchaseOrder.setCreatedBy(purchaseOrderRequestDTO.getCreatedBy());
        // Update attributes
        List<PurchaseOrderAttributes> existingAttributes = purchaseOrder.getPurchaseOrderAttributes();

        // Remove orphaned attributes manually
        existingAttributes.clear();

        List<PurchaseOrderAttributes> purchaseOrderAttributes = purchaseOrderRequestDTO.getPurchaseOrderAttributes().stream()
                .map(dto -> {

                    PurchaseOrderAttributes attribute = new PurchaseOrderAttributes();
                    attribute.setMaterialCode(dto.getMaterialCode());
                    attribute.setPoId(purchaseOrderRequestDTO.getPoId());
                    attribute.setMaterialDescription(dto.getMaterialDescription());
                    attribute.setQuantity(dto.getQuantity());
                    attribute.setRate(dto.getRate());
                    attribute.setCurrency(dto.getCurrency());
                    attribute.setExchangeRate(dto.getExchangeRate());
                    attribute.setGst(dto.getGst());
                    attribute.setDuties(dto.getDuties());
                    attribute.setFreightCharge(dto.getFreightCharge());
                    attribute.setBudgetCode(dto.getBudgetCode());
                    attribute.setPurchaseOrder(purchaseOrder);  // Associate with PurchaseOrder
                    return attribute;
                })
                .collect(Collectors.toList());

        // purchaseOrder.setPurchaseOrderAttributes(purchaseOrderAttributes);
        // purchaseOrderRepository.save(purchaseOrder);

        existingAttributes.addAll(purchaseOrderAttributes);
        // purchaseOrder.setPurchaseOrderAttributes(purchaseOrderAttributes);
        purchaseOrderRepository.save(purchaseOrder);

        return mapToResponseDTO(purchaseOrder);
    }

    public poWithTenderAndIndentResponseDTO getPurchaseOrderById(String poId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Purchase order not found for the provided asset ID.")
                ));

        // Fetch related Tender & Indent
        TenderWithIndentResponseDTO tenderWithIndent = tenderRequestService.getTenderRequestById(purchaseOrder.getTenderId());

        poWithTenderAndIndentResponseDTO responseDTO = new poWithTenderAndIndentResponseDTO();
        responseDTO.setPoId(purchaseOrder.getPoId());
        responseDTO.setTenderId(purchaseOrder.getTenderId());
        responseDTO.setIndentId(purchaseOrder.getIndentId());
        responseDTO.setWarranty(purchaseOrder.getWarranty());
        responseDTO.setConsignesAddress(purchaseOrder.getConsignesAddress());
        responseDTO.setBillingAddress(purchaseOrder.getBillingAddress());
        responseDTO.setDeliveryPeriod(purchaseOrder.getDeliveryPeriod());
        responseDTO.setIfLdClauseApplicable(purchaseOrder.getIfLdClauseApplicable());
        responseDTO.setIncoTerms(purchaseOrder.getIncoTerms());
        responseDTO.setPaymentTerms(purchaseOrder.getPaymentTerms());
        responseDTO.setVendorName(purchaseOrder.getVendorName());
        responseDTO.setVendorAddress(purchaseOrder.getVendorAddress());
        responseDTO.setApplicablePbgToBeSubmitted(purchaseOrder.getApplicablePbgToBeSubmitted());
        responseDTO.setTransporterAndFreightForWarderDetails(purchaseOrder.getTransporterAndFreightForWarderDetails());
        responseDTO.setVendorAccountNumber(purchaseOrder.getVendorAccountNumber());
        responseDTO.setVendorsZfscCode(purchaseOrder.getVendorsZfscCode());
        responseDTO.setVendorAccountName(purchaseOrder.getVendorAccountName());
        responseDTO.setProjectName(purchaseOrder.getProjectName());
        responseDTO.setTotalValueOfPo(tenderWithIndent.getTotalTenderValue());
        responseDTO.setCreatedBy(purchaseOrder.getCreatedBy());
        responseDTO.setUpdatedBy(purchaseOrder.getUpdatedBy());
        responseDTO.setCreatedDate(purchaseOrder.getCreatedDate());
        responseDTO.setUpdatedDate(purchaseOrder.getUpdatedDate());

        responseDTO.setPurchaseOrderAttributes(purchaseOrder.getPurchaseOrderAttributes().stream()
                .map(attribute -> {
                    PurchaseOrderAttributesResponseDTO attributeDTO = new PurchaseOrderAttributesResponseDTO();
                    attributeDTO.setMaterialCode(attribute.getMaterialCode());
                    attributeDTO.setMaterialDescription(attribute.getMaterialDescription());
                    attributeDTO.setQuantity(attribute.getQuantity());
                    attributeDTO.setRate(attribute.getRate());
                    attributeDTO.setCurrency(attribute.getCurrency());
                    attributeDTO.setExchangeRate(attribute.getExchangeRate());
                    attributeDTO.setGst(attribute.getGst());
                    attributeDTO.setDuties(attribute.getDuties());
                    attributeDTO.setFreightCharge(attribute.getFreightCharge());
                    attributeDTO.setBudgetCode(attribute.getBudgetCode());
                    return attributeDTO;
                })
                .collect(Collectors.toList()));
        // Set Tender & Indent details
        responseDTO.setTenderDetails(tenderWithIndent);
        return responseDTO;

    }


    @Override
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        return purchaseOrders.stream().map(this::mapToResponseDTO).collect(Collectors.toList());  // Map each PurchaseOrder to its DTO
    }


    public void deletePurchaseOrder(String poId) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Purchase order not found for the provided ID."
                        )
                ));
        try {
            purchaseOrderRepository.delete(purchaseOrder);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  po."
                    ),
                    ex
            );
        }

    }


    private PurchaseOrderResponseDTO mapToResponseDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDTO responseDTO = new PurchaseOrderResponseDTO();
        responseDTO.setPoId(purchaseOrder.getPoId());
        responseDTO.setTenderId(purchaseOrder.getTenderId());
        responseDTO.setIndentId(purchaseOrder.getIndentId());
        responseDTO.setWarranty(purchaseOrder.getWarranty());
        responseDTO.setConsignesAddress(purchaseOrder.getConsignesAddress());
        responseDTO.setBillingAddress(purchaseOrder.getBillingAddress());
        responseDTO.setDeliveryPeriod(purchaseOrder.getDeliveryPeriod());
        responseDTO.setIfLdClauseApplicable(purchaseOrder.getIfLdClauseApplicable());
        responseDTO.setIncoTerms(purchaseOrder.getIncoTerms());
        responseDTO.setPaymentTerms(purchaseOrder.getPaymentTerms());
        responseDTO.setVendorName(purchaseOrder.getVendorName());
        responseDTO.setVendorAddress(purchaseOrder.getVendorAddress());
        responseDTO.setApplicablePbgToBeSubmitted(purchaseOrder.getApplicablePbgToBeSubmitted());
        responseDTO.setTransporterAndFreightForWarderDetails(purchaseOrder.getTransporterAndFreightForWarderDetails());
        responseDTO.setVendorAccountNumber(purchaseOrder.getVendorAccountNumber());
        responseDTO.setVendorsZfscCode(purchaseOrder.getVendorsZfscCode());
        responseDTO.setVendorAccountName(purchaseOrder.getVendorAccountName());
        responseDTO.setProjectName(purchaseOrder.getProjectName());
        //  responseDTO.setTotalValueOfPo(purchaseOrder.getTotalValueOfPo());
        responseDTO.setCreatedBy(purchaseOrder.getCreatedBy());
        responseDTO.setUpdatedBy(purchaseOrder.getUpdatedBy());
        responseDTO.setCreatedDate(purchaseOrder.getCreatedDate());
        responseDTO.setUpdatedDate(purchaseOrder.getUpdatedDate());

        responseDTO.setPurchaseOrderAttributes(purchaseOrder.getPurchaseOrderAttributes().stream()
                .map(attribute -> {
                    PurchaseOrderAttributesResponseDTO attributeDTO = new PurchaseOrderAttributesResponseDTO();
                    attributeDTO.setMaterialCode(attribute.getMaterialCode());
                    attributeDTO.setMaterialDescription(attribute.getMaterialDescription());
                    attributeDTO.setQuantity(attribute.getQuantity());
                    attributeDTO.setRate(attribute.getRate());
                    attributeDTO.setCurrency(attribute.getCurrency());
                    attributeDTO.setExchangeRate(attribute.getExchangeRate());
                    attributeDTO.setGst(attribute.getGst());
                    attributeDTO.setDuties(attribute.getDuties());
                    attributeDTO.setFreightCharge(attribute.getFreightCharge());
                    attributeDTO.setBudgetCode(attribute.getBudgetCode());
                    return attributeDTO;
                })
                .collect(Collectors.toList()));
        List<String> indentIds = indentIdRepository.findTenderWithIndent(purchaseOrder.getTenderId());

        // Calculate total tender value by summing totalPriceOfAllMaterials of all indents
        BigDecimal totalTenderValue = indentIds.stream()
                .map(indentCreationService::getIndentById) // Fetch Indent data
                .map(IndentCreationResponseDTO::getTotalPriceOfAllMaterials) // Extract total price
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up values
        responseDTO.setTotalValue(totalTenderValue);
        System.out.println("tottalTenderValue" + totalTenderValue);

        Optional<TenderRequest> tenderRequest = tenderRequestRepository.findByTenderId(purchaseOrder.getTenderId());

        String projectName = tenderRequest.map(TenderRequest::getProjectName).orElse(null);
        responseDTO.setProjectName(projectName);
        System.out.println("projectName:" + projectName);
        BigDecimal allocatedAmount = projectMasterRepository
                .findByProjectNameDescription(projectName)
                .map(ProjectMaster::getAllocatedAmount)
                .orElse(BigDecimal.ZERO);
        responseDTO.setProjectLimit(allocatedAmount);
        System.out.println("allocatedAmount: " + allocatedAmount);


        return responseDTO;
    }

    @Override
    public List<VendorContractReportDTO> getVendorContractDetails(String startDate, String endDate) {
        List<Object[]> results = purchaseOrderRepository.getVendorContractDetails(
                CommonUtils.convertStringToDateObject(startDate),
                CommonUtils.convertStringToDateObject(endDate)
        );

        return results.stream().map(row -> new VendorContractReportDTO(
                (row[0] != null) ? row[0].toString() : "",  // orderId
                (row[1] != null) ? row[1].toString() : "",  // modeOfProcurement
                (row[2] != null) ? row[2].toString() : "",  // underAMC
                (row[3] != null) ? row[3].toString() : "",  // amcFor
                (row[4] != null) ? row[4].toString() : "",  // endUser
                (row[5] != null) ? ((Number) row[5]).intValue() : null,  // noOfParticipants
                (row[6] != null) ? ((Number) row[6]).doubleValue() : null,  // value
                (row[7] != null) ? row[7].toString() : "",  // location
                (row[8] != null) ? row[8].toString() : "",  // vendorName
                (row[9] != null) ? row[9].toString() : "",  // previouslyRenewedAMCs
                (row[10] != null) ? row[10].toString() : "",  // categoryOfSecurity
                (row[11] != null) ? row[11].toString() : ""  // validityOfSecurity
        )).collect(Collectors.toList());
    }

    @Override
    public List<ProcurementActivityReportResponse> getProcurementActivityReport(String startDate, String endDate) {
        List<Object[]> results = purchaseOrderRepository.getProcurementActivityReport(CommonUtils.convertStringToDateObject(startDate),
                CommonUtils.convertStringToDateObject(endDate));

        return results.stream().map(row ->
                new ProcurementActivityReportResponse(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (BigDecimal) row[3],
                        (String) row[4],
                        (String) row[5]
                )
        ).collect(Collectors.toList());
    }
}



