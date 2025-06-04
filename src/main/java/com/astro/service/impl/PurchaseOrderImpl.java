package com.astro.service.impl;


import com.astro.constant.AppConstant;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.ProcurementActivityReportResponse;
import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.*;
import com.astro.dto.workflow.VendorContractReportDTO;
import com.astro.entity.ProcurementModule.MaterialDetails;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import com.astro.entity.ProcurementModule.PurchaseOrderAttributes;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.entity.ProjectMaster;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.IndentCreation.MaterialDetailsRepository;
import com.astro.repository.ProcurementModule.IndentIdRepository;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderAttributesRepository;

import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderRepository;

import com.astro.repository.ProcurementModule.ServiceOrderRepository.ServiceOrderRepository;
import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.repository.ProjectMasterRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialDtlRepository;
import com.astro.service.IndentCreationService;
import com.astro.service.PurchaseOrderService;
import com.astro.service.TenderRequestService;
import com.astro.util.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private ServiceOrderRepository serviceOrderRepository;


    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO) {

        // Check if the indentorId already exists
     /*   if (purchaseOrderRepository.existsById(purchaseOrderRequestDTO.getPoId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Purchase Order ID", "PO ID " + purchaseOrderRequestDTO.getPoId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

      */


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

        String tenderId = purchaseOrderRequestDTO.getTenderId();
        String poId = generatePoId(tenderId);

        //  purchaseOrder.setPoId(purchaseOrderRequestDTO.getPoId());
        purchaseOrder.setPoId(poId);
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
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsIfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        purchaseOrder.setVendorId(purchaseOrderRequestDTO.getVendorId());
        //  purchaseOrder.setTotalValueOfPo(purchaseOrderRequestDTO.getTotalValueOfPo());
        String Date = purchaseOrderRequestDTO.getDeliveryDate();
        if (Date != null) {
            purchaseOrder.setDeliveryDate(CommonUtils.convertStringToDateObject(Date));
        } else {
            purchaseOrder.setDeliveryDate(null);
        }
        purchaseOrder.setProjectName(purchaseOrderRequestDTO.getProjectName());
        purchaseOrder.setCreatedBy(purchaseOrderRequestDTO.getCreatedBy());
        purchaseOrder.setUpdatedBy(purchaseOrderRequestDTO.getUpdatedBy());
        List<PurchaseOrderAttributes> purchaseOrderAttributes = purchaseOrderRequestDTO.getPurchaseOrderAttributes().stream()
                .map(dto -> {

                    PurchaseOrderAttributes attribute = new PurchaseOrderAttributes();
                    attribute.setMaterialCode(dto.getMaterialCode());
                    // attribute.setPoId(purchaseOrderRequestDTO.getPoId());
                    attribute.setPoId(poId);
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

    public String generatePoId(String tenderId) {
        String numericPart = tenderId.replaceAll("\\D+", "");
        return "PO" + numericPart;
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
        purchaseOrder.setVendorsZfscCode(purchaseOrderRequestDTO.getVendorsIfscCode());
        purchaseOrder.setVendorAccountName(purchaseOrderRequestDTO.getVendorAccountName());
        purchaseOrder.setProjectName(purchaseOrderRequestDTO.getProjectName());
        purchaseOrder.setVendorId(purchaseOrderRequestDTO.getVendorId());
        String Date = purchaseOrderRequestDTO.getDeliveryDate();
        if (Date != null) {
            purchaseOrder.setDeliveryDate(CommonUtils.convertStringToDateObject(Date));
        } else {
            purchaseOrder.setDeliveryDate(null);
        }
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
                    //   attribute.setPoId(purchaseOrderRequestDTO.getPoId());
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

    @Autowired
    private GprnMaterialDtlRepository gprnMaterialDtlRepository;

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
        Map<String, MaterialDetailsResponseDTO> indentMaterialMap = new HashMap<>();

        for (IndentCreationResponseDTO indent : tenderWithIndent.getIndentResponseDTO()) {
            for (MaterialDetailsResponseDTO material : indent.getMaterialDetails()) {
                indentMaterialMap.put(material.getMaterialCode(), material);
            }
        }


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
        responseDTO.setVendorsIfscCode(purchaseOrder.getVendorsZfscCode());
        responseDTO.setVendorAccountName(purchaseOrder.getVendorAccountName());
        responseDTO.setVendorId(purchaseOrder.getVendorId());
        //  responseDTO.setProjectName(purchaseOrder.getProjectName());
        responseDTO.setTotalValueOfPo(tenderWithIndent.getTotalTenderValue());
        LocalDate date = purchaseOrder.getDeliveryDate();
        if (date != null) {
            responseDTO.setDeliveryDate(CommonUtils.convertDateToString(date));
        } else {
            responseDTO.setDeliveryDate(null);
        }
        responseDTO.setCreatedBy(purchaseOrder.getCreatedBy());
        responseDTO.setUpdatedBy(purchaseOrder.getUpdatedBy());
        responseDTO.setCreatedDate(purchaseOrder.getCreatedDate());
        responseDTO.setUpdatedDate(purchaseOrder.getUpdatedDate());
        List<String> indentIds = indentIdRepository.findTenderWithIndent(purchaseOrder.getTenderId());

        responseDTO.setIndentIds(indentIds);

        responseDTO.setPurchaseOrderAttributes(purchaseOrder.getPurchaseOrderAttributes().stream()
                .map(attribute -> {
                    PurchaseOrderAttributesResponseDTO attributeDTO = new PurchaseOrderAttributesResponseDTO();
                    attributeDTO.setMaterialCode(attribute.getMaterialCode());
                    attributeDTO.setMaterialDescription(attribute.getMaterialDescription());

                    // Get sum of GPRN quantities for this material
                    BigDecimal gprnQuantity = gprnMaterialDtlRepository
                            .findByPoIdAndMaterialCode(poId, attribute.getMaterialCode())
                            .stream()
                            .map(gprn -> gprn.getReceivedQuantity())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Set remaining quantity
                    attributeDTO.setQuantity(attribute.getQuantity().subtract(gprnQuantity));
                    attributeDTO.setReceivedQuantity(attribute.getReceivedQuantity());
                    attributeDTO.setRate(attribute.getRate());
                    attributeDTO.setCurrency(attribute.getCurrency());
                    attributeDTO.setExchangeRate(attribute.getExchangeRate());
                    attributeDTO.setGst(attribute.getGst());
                    attributeDTO.setDuties(attribute.getDuties());
                    attributeDTO.setFreightCharge(attribute.getFreightCharge());
                    attributeDTO.setBudgetCode(attribute.getBudgetCode());
                    MaterialDetailsResponseDTO indentMaterial = indentMaterialMap.get(attribute.getMaterialCode());
                    attributeDTO.setUnitPrice(indentMaterial.getUnitPrice());
                    attributeDTO.setUom(indentMaterial.getUom());
                    attributeDTO.setCategory(indentMaterial.getMaterialCategory());
                    return attributeDTO;
                })
                .collect(Collectors.toList()));
        String projectName = tenderWithIndent.getIndentResponseDTO()
                .stream()
                .findFirst()
                .map(IndentCreationResponseDTO::getProjectName)
                .orElse(null);
        BigDecimal projectLimit = tenderWithIndent.getIndentResponseDTO()
                .stream()
                .findFirst()
                .map(IndentCreationResponseDTO::getProjectLimit)
                .orElse(null);
        responseDTO.setProjectName(projectName);
        responseDTO.setProjectLimit(projectLimit);
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
        responseDTO.setVendorsIfscCode(purchaseOrder.getVendorsZfscCode());
        responseDTO.setVendorAccountName(purchaseOrder.getVendorAccountName());
        responseDTO.setVendorId(purchaseOrder.getVendorId());
        responseDTO.setProjectName(purchaseOrder.getProjectName());
        //  responseDTO.setTotalValueOfPo(purchaseOrder.getTotalValueOfPo());
        LocalDate date = purchaseOrder.getDeliveryDate();
        if (date != null) {
            responseDTO.setDeliveryDate(CommonUtils.convertDateToString(date));
        } else {
            responseDTO.setDeliveryDate(null);
        }
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


    @Override
    public List<ApprovedPoListReportDto> getApprovedPoReport(String startDate, String endDate) {
        LocalDate from = CommonUtils.convertStringToDateObject(startDate);
        LocalDate to = CommonUtils.convertStringToDateObject(endDate);

        List<Object[]> rows = purchaseOrderRepository.getApprovedPoReport(from, to);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return rows.stream().map(row -> {
            ApprovedPoListReportDto dto = new ApprovedPoListReportDto();

            dto.setApprovedDate(CommonUtils.convertDateToString(row[0] != null
                    ? ((Timestamp) row[0]).toLocalDateTime().toLocalDate()
                    : null));
            dto.setPoId((String) row[1]);
            dto.setVendorName((String) row[2]);
            dto.setValue(((BigDecimal) row[3]).doubleValue());
            dto.setTenderId((String) row[4]);
            dto.setProject((String) row[5]);
            dto.setVendorId((String) row[6]);
            dto.setIndentIds((String) row[7]);
            dto.setModeOfProcurement((String) row[8]);

            // Parse JSON array of attributes (column index 9)
            String json = (String) row[9];
            try {
                List<PurchaseOrderAttributesResponseDTO> attrs = mapper.readValue(
                        json,
                        mapper.getTypeFactory().constructCollectionType(
                                List.class,
                                PurchaseOrderAttributesResponseDTO.class
                        )
                );
                dto.setPurchaseOrderAttributes(attrs);
            } catch (Exception e) {
                dto.setPurchaseOrderAttributes(new ArrayList<>());
            }

            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public List<pendingPoReportDto> getPendingPoReport(String startDate, String endDate) {

        LocalDate from = CommonUtils.convertStringToDateObject(startDate);
        LocalDate to = CommonUtils.convertStringToDateObject(endDate);

        List<Object[]> rows = purchaseOrderRepository.getPendingPoReport(from, to);


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return rows.stream().map(row -> {
            pendingPoReportDto dto = new pendingPoReportDto();
            dto.setPoId((String) row[0]);
            dto.setTenderId((String) row[1]);
            dto.setIndentIds((String) row[2]);
            dto.setValue(((BigDecimal) row[3]).doubleValue());
            dto.setVendorName((String) row[4]);

            if (row[5] != null) {
                LocalDate submitted = ((Timestamp) row[5]).toLocalDateTime().toLocalDate();
                dto.setSubmittedDate(CommonUtils.convertDateToString(submitted));
            }
            dto.setPendingWith((String) row[6]);
            if (row[7] != null) {
                LocalDate pending = ((Timestamp) row[7]).toLocalDateTime().toLocalDate();
                dto.setPendingFrom(CommonUtils.convertDateToString(pending));
            }

            dto.setStatus((String) row[8]);
            dto.setAsOnDate(LocalDate.now());

            // parse the JSON array of attributes
            String json = (String) row[9];
            try {
                List<PurchaseOrderAttributesResponseDTO> attrs = mapper.readValue(
                        json,
                        mapper.getTypeFactory().constructCollectionType(
                                List.class,
                                PurchaseOrderAttributesResponseDTO.class
                        )
                );
                dto.setPurchaseOrderAttributes(attrs);
            } catch (Exception e) {
                dto.setPurchaseOrderAttributes(new ArrayList<>());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<QuarterlyVigilanceReportDto> getQuarterlyVigilanceReport() {
        LocalDate[] range = CommonUtils.getPreviousQuarterRange();
        LocalDateTime start = range[0].atStartOfDay();
        LocalDateTime end = range[1].atTime(23, 59, 59);

        List<Object[]> results = purchaseOrderRepository.findQuarterlyVigilanceReportDto(start, end);
        List<Object[]> serviceresult = serviceOrderRepository.findQuarterlyVigilanceSoReportDto(start, end);

        List<QuarterlyVigilanceReportDto> orders = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        List<Object[]> combined = new ArrayList<>();
        combined.addAll(results);
        combined.addAll(serviceresult);

        for (Object[] row : combined) {
            QuarterlyVigilanceReportDto dto = new QuarterlyVigilanceReportDto();

            dto.setOrderNo((String) row[0]);

            if (row[1] instanceof java.sql.Date) {
                dto.setOrderDate(((java.sql.Date) row[1]).toLocalDate());
            } else if (row[1] instanceof LocalDate) {
                dto.setOrderDate((LocalDate) row[1]);
            }

            dto.setValue(row[2] != null ? ((Number) row[2]).doubleValue() : null);

            try {
                String descriptionsJson = (String) row[3];
                List<PoMaterialReport> descriptions = mapper.readValue(
                        descriptionsJson,
                        mapper.getTypeFactory().constructCollectionType(List.class, PoMaterialReport.class)
                );
                dto.setDescriptions(descriptions);
            } catch (Exception e) {
                dto.setDescriptions(new ArrayList<>());
            }

            dto.setVendorName((String) row[4]);
            dto.setLocation((String) row[5]);

            //  deliveryDate may or may not be present
            if (row.length > 6 && row[6] != null) {
                dto.setDeliveryDate(CommonUtils.convertDateToString(((java.sql.Date) row[6]).toLocalDate()));
            } else {
                dto.setDeliveryDate(null);
            }

            orders.add(dto);
        }

        return orders;
    }

@Override
public List<ShortClosedCancelledOrderReportDto> getShortClosedCancelledOrders(String startDate, String endDate) {

    List<Object[]> poOrder = purchaseOrderRepository.findShortClosedCancelledOrder(
            CommonUtils.convertStringToDateObject(startDate),
            CommonUtils.convertStringToDateObject(endDate)
    );

    List<Object[]> soOrder = serviceOrderRepository.findShortClosedCancelledSoOrders(
            CommonUtils.convertStringToDateObject(startDate),
            CommonUtils.convertStringToDateObject(endDate)
    );


    ObjectMapper mapper = new ObjectMapper();
    List<ShortClosedCancelledOrderReportDto> result = new ArrayList<>();


    processOrders(poOrder, result, mapper);

    processOrders(soOrder, result, mapper);

    return result;
}



    private void processOrders(List<Object[]> orders, List<ShortClosedCancelledOrderReportDto> result, ObjectMapper mapper) {
        for (Object[] row : orders) {
            ShortClosedCancelledOrderReportDto dto = new ShortClosedCancelledOrderReportDto();

            dto.setPoId((String) row[0]);
            dto.setTenderId((String) row[1]);
            dto.setIndentIds((String) row[2]);
            dto.setValue(row[3] != null ? ((Number) row[3]).doubleValue() : null);
            dto.setVendorName((String) row[4]);

            if (row[5] != null) {
                LocalDate submitted = ((Timestamp) row[5]).toLocalDateTime().toLocalDate();
                dto.setSubmittedDate(CommonUtils.convertDateToString(submitted));
            }

            String materialJson = (String) row[6];
            try {
                List<PoMaterialReport> materials = mapper.readValue(
                        materialJson,
                        mapper.getTypeFactory().constructCollectionType(List.class, PoMaterialReport.class)
                );
                dto.setMaterials(materials);
            } catch (Exception e) {
                dto.setMaterials(new ArrayList<>());
            }

            dto.setReason((String) row[7]);

            result.add(dto);
        }
    }

    @Override
    public List<MonthlyProcurementReportDto> getMonthlyProcurementReport(String startDate, String endDate) {

        List<Object[]> rows = purchaseOrderRepository.getMonthlyProcurementReport( CommonUtils.convertStringToDateObject(startDate),
                CommonUtils.convertStringToDateObject(endDate));
        System.out.println(rows);
        List<MonthlyProcurementReportDto> reports = new ArrayList<>();

        for (Object[] row : rows) {
            MonthlyProcurementReportDto dto = new MonthlyProcurementReportDto();
            dto.setMonth(row[0] != null ? row[0].toString() : "Unknown");
            dto.setPoNumber((String) row[1]);
            dto.setDate(row[2].toString());
            dto.setIndentIds((String) row[3]);
            dto.setValue(row[4] != null ? ((Number) row[4]).doubleValue() : null);
            dto.setVendorName((String) row[5]);

            String mode = (String) row[6];
            String mappedMode =null;
            if(mode!= null){
             mappedMode = switch (mode) {
                case "GeM" -> "GeM";
                case "Proprietary/Single Tender" -> "Non-GeM (Proprietary/Single Tender)";
                case "Limited Pre Approved Vendor Tender" -> "Non-GeM (Limited Pre Approved Vendor Tender)";
                case "Brand PAC" -> "Non-GeM (Brand PAC)";
                case "Open Tender" -> "Non-GeM (Open Tender)";
                case "Global Tender" -> "Non-GeM (Global Tender)";
                default -> "Other";
            };}else{
                dto.setModeOfProcurement(null);
            }

            dto.setModeOfProcurement(mappedMode);

            reports.add(dto);
        }

        return reports;

    }



}



