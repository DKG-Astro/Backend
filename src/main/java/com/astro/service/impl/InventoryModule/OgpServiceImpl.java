package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.OgpService;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.repository.InventoryModule.isn.IssueNoteMasterRepository;
import com.astro.repository.InventoryModule.ogp.OgpDetailRepository;
import com.astro.repository.InventoryModule.ogp.OgpMasterPoRepository;
import com.astro.repository.InventoryModule.ogp.OgpPoDetailRepository;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderAttributesRepository;
import com.astro.repository.InventoryModule.ogp.OgpMasterRepository;
import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.ogp.GprApprovalDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpDetailReportDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpMaterialDtlDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoDtlDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoMaterialDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoResponseDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpReportDto;
import com.astro.entity.InventoryModule.OgpDetailEntity;
import com.astro.entity.InventoryModule.OgpMasterEntity;
import com.astro.entity.InventoryModule.OgpMasterPoEntity;
import com.astro.entity.InventoryModule.OgpPoDetailEntity;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.util.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.modelmapper.ModelMapper;

@Service
public class OgpServiceImpl implements OgpService {
    
    @Autowired
    private OgpMasterPoRepository ogpMasterPoRepository;
    
    @Autowired
    private OgpPoDetailRepository ogpPoDetailRepository;
    
    @Autowired
    private OgpMasterRepository ogpMasterRepository;
    
    @Autowired
    private OgpDetailRepository ogpDetailRepository;
    
    @Autowired
    private IssueNoteMasterRepository isnMasterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssetMasterRepository amr;

    @Autowired
    private PurchaseOrderAttributesRepository poMasterRepository;

    @Override
    @Transactional
    public String saveOgp(OgpDto req) {
        validateIsn(req.getIssueNoteId());
        Integer issueNoteId = Integer.parseInt(req.getIssueNoteId().split("/")[1]);

        // Validate if OGP already exists for these specific items
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        for (OgpMaterialDtlDto material : req.getMaterialDtlList()) {
            if (ogpDetailRepository.existsByIssueNoteIdAndAssetIdAndLocatorId(
                    issueNoteId, 
                    material.getAssetId(), 
                    material.getLocatorId())) {
                errorMessage.append("OGP already exists for Asset ID: ")
                    .append(material.getAssetId())
                    .append(" at Locator: ")
                    .append(material.getLocatorId())
                    .append(". ");
                errorFound = true;
            }
        }

        if (errorFound) {
            throw new BusinessException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                errorMessage.toString()));
        }

        // Create OGP master
        final OgpMasterEntity ogpMaster = new OgpMasterEntity();  // Manual mapping instead of ModelMapper
        ogpMaster.setOgpDate(CommonUtils.convertStringToDateObject(req.getOgpDate()));
        ogpMaster.setIssueNoteId(issueNoteId);
        ogpMaster.setCreateDate(LocalDateTime.now());
        ogpMaster.setOgpProcessId("INV" + issueNoteId);
        ogpMaster.setCreatedBy(req.getCreatedBy());
        ogpMaster.setLocationId(req.getLocationId());
        ogpMaster.setOgpType(req.getOgpType());
        ogpMaster.setStatus("AWAITING APPROVAL");
        System.out.println("STATUS SET");
        if(Objects.nonNull(req.getDateOfReturn())){
            ogpMaster.setDateOfReturn(CommonUtils.convertStringToDateObject(req.getDateOfReturn()));
        }
        ogpMaster.setReceiverLocation(req.getReceiverLocation());
        ogpMaster.setReceiverName(req.getReceiverName());

        final OgpMasterEntity savedOgpMaster = ogpMasterRepository.save(ogpMaster);

        // Save OGP details
        List<OgpDetailEntity> ogpDetails = req.getMaterialDtlList().stream()
            .map(dtl -> {
                OgpDetailEntity detail = new OgpDetailEntity();  // Manual mapping instead of ModelMapper
                detail.setOgpProcessId(savedOgpMaster.getOgpProcessId()); 
                detail.setOgpSubProcessId(savedOgpMaster.getOgpSubProcessId());
                detail.setIssueNoteId(issueNoteId);
                detail.setAssetId(dtl.getAssetId());
                detail.setLocatorId(dtl.getLocatorId());
                detail.setQuantity(dtl.getQuantity());
                return detail;
            })
            .collect(Collectors.toList());
        
        ogpDetailRepository.saveAll(ogpDetails);

        return ogpMaster.getOgpProcessId() + "/" + ogpMaster.getOgpSubProcessId();
    }

    @Override
    public OgpDto getOgpDtls(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer ogpSubProcessId = Integer.parseInt(processNoSplit[1]);
        
        OgpMasterEntity ogpMaster = ogpMasterRepository.findById(ogpSubProcessId)
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "OGP not found")));

        List<OgpDetailEntity> ogpDetails = ogpDetailRepository.findByOgpSubProcessId(ogpSubProcessId);

        OgpDto response = modelMapper.map(ogpMaster, OgpDto.class);
        response.setOgpDate(CommonUtils.convertDateToString(ogpMaster.getOgpDate()));
        response.setIssueNoteId(processNo);
        response.setOgpId(ogpMaster.getOgpProcessId() + "/" + ogpMaster.getOgpSubProcessId());

        List<OgpMaterialDtlDto> materialDtls = ogpDetails.stream()
            .map(detail -> {
                OgpMaterialDtlDto dto = modelMapper.map(detail, OgpMaterialDtlDto.class);
                amr.findById(detail.getAssetId())
                    .ifPresent(asset -> {
                        dto.setAssetDesc(asset.getAssetDesc());
                        dto.setUomId(asset.getUomId());
                    });
                return dto;
            })
            .collect(Collectors.toList());

        response.setMaterialDtlList(materialDtls);
        return response;
    }

    public void validateIsn(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2 || !processNoSplit[0].equals("INV")) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid ISN No."));
        }

        try {
            Integer subProcessId = Integer.parseInt(processNoSplit[1]);
            
            if (!isnMasterRepository.existsById(subProcessId)) {
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Provided ISN No. is not valid."));
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid ISN number format"));
        }
    }

    @Override
    public List<OgpReportDto> getOgpReport(String startDate, String endDate) {
        List<LocalDateTime> dateRange = CommonUtils.getDateRenge(startDate, endDate);
        List<Object[]> results = ogpMasterRepository.getOgpReport(dateRange.get(0), dateRange.get(1));
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return results.stream().map(row -> {
            OgpReportDto dto = new OgpReportDto();
            dto.setOgpProcessId((String) row[0]);
            dto.setOgpSubProcessId((Integer) row[1]);
            dto.setIssueNoteId((Integer) row[2]);
            dto.setOgpDate(CommonUtils.convertSqlDateToString((java.sql.Date) row[3]));
            // dto.setOgpDate( row[3] != null ? ((java.sql.Timestamp) row[3]).toLocalDateTime() : null);
            dto.setLocationId((String) row[4]);
            dto.setCreatedBy((Integer) row[5]);
            
            try {
                String detailsJson = (String) row[7];
                List<OgpDetailReportDto> details = mapper.readValue(
                    detailsJson, 
                    new TypeReference<List<OgpDetailReportDto>>() {}
                );
                dto.setOgpDetails(details);
            } catch (Exception e) {
                dto.setOgpDetails(new ArrayList<>());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String savePoOgp(OgpPoDto request) {
        // Get existing OGP quantities for this PO
        List<OgpMasterPoEntity> existingOgps = ogpMasterPoRepository.findByPoId(request.getPoId());
        
        // Validate quantities against PO
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;
        
        for (OgpPoDtlDto dtl : request.getMaterialDtlList()) {
            // Get sum of existing OGP quantities for this material
            BigDecimal existingQuantity = existingOgps.stream()
                .flatMap(ogp -> ogpPoDetailRepository.findByOgpSubProcessIdAndMaterialCode(
                    ogp.getOgpSubProcessId(), 
                    dtl.getMaterialCode()
                ).stream())
                .map(OgpPoDetailEntity::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Add current quantity
            BigDecimal totalQuantity = existingQuantity.add(dtl.getQuantity());
            
            // Get PO quantity from PO master
            BigDecimal poQuantity = poMasterRepository.findQuantityByPoIdAndMaterialCode(
                request.getPoId(), 
                dtl.getMaterialCode()
            ).orElse(BigDecimal.ZERO);
            
            if (totalQuantity.compareTo(poQuantity) > 0) {
                errorMessage.append("Total OGP quantity for material ")
                    .append(dtl.getMaterialCode())
                    .append(" exceeds PO quantity. PO quantity: ")
                    .append(poQuantity)
                    .append(", Total OGP quantity: ")
                    .append(totalQuantity)
                    .append(". ");
                errorFound = true;
            }
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                errorMessage.toString()));
        }

        // Create and save OGP Master PO
        OgpMasterPoEntity ogpMasterPo = new OgpMasterPoEntity();
        ogpMasterPo.setPoId(request.getPoId());
        ogpMasterPo.setStatus("AWAITING APPROVAL");
        ogpMasterPo.setOgpDate(CommonUtils.convertStringToDateObject(request.getOgpDate()));
        ogpMasterPo.setLocationId(request.getLocationId());
        ogpMasterPo.setCreatedBy(request.getCreatedBy());
        ogpMasterPo.setCreateDate(LocalDateTime.now());
        ogpMasterPo.setOgpType(request.getOgpType());
        if(Objects.nonNull(request.getDateOfReturn())){
            ogpMasterPo.setDateOfReturn(CommonUtils.convertStringToDateObject(request.getDateOfReturn()));
        }
        ogpMasterPo.setReceiverLocation(request.getReceiverLocation());
        ogpMasterPo.setReceiverName(request.getReceiverName());

        OgpMasterPoEntity savedMaster = ogpMasterPoRepository.save(ogpMasterPo);

        // Save OGP PO Details
        List<OgpPoDetailEntity> details = request.getMaterialDtlList().stream()
            .map(dtl -> {
                OgpPoDetailEntity detail = new OgpPoDetailEntity();
                detail.setOgpSubProcessId(savedMaster.getOgpSubProcessId());
                detail.setMaterialCode(dtl.getMaterialCode());
                detail.setMaterialDescription(dtl.getMaterialDescription());
                detail.setUomId(dtl.getUom());
                detail.setQuantity(dtl.getQuantity());
                return detail;
            })
            .collect(Collectors.toList());

        ogpPoDetailRepository.saveAll(details);

        return "INV/" + savedMaster.getOgpSubProcessId().toString();
    }
    
    @Override
    public OgpPoResponseDto getPoOgp(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer ogpSubProcessId = Integer.parseInt(processNoSplit[1]);
        
        // Get OGP PO master
        OgpMasterPoEntity ogpMasterPo = ogpMasterPoRepository.findById(ogpSubProcessId)
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "OGP PO not found")));

        // Get OGP PO details
        List<OgpPoDetailEntity> ogpPoDetails = ogpPoDetailRepository.findByOgpSubProcessId(ogpSubProcessId);

        // Map to response DTO
        OgpPoResponseDto response = new OgpPoResponseDto();
        response.setOgpId("INV/" + ogpMasterPo.getOgpSubProcessId());
        response.setPoId(ogpMasterPo.getPoId());
        response.setOgpDate(CommonUtils.convertDateToString(ogpMasterPo.getOgpDate()));
        response.setLocationId(ogpMasterPo.getLocationId());
        response.setCreatedBy(ogpMasterPo.getCreatedBy());
        response.setOgpType(ogpMasterPo.getOgpType());

        // Map details
        List<OgpPoMaterialDto> materialDtls = ogpPoDetails.stream()
            .map(detail -> {
                OgpPoMaterialDto dto = new OgpPoMaterialDto();
                dto.setMaterialCode(detail.getMaterialCode());
                dto.setMaterialDescription(detail.getMaterialDescription());
                dto.setUomId(detail.getUomId());
                dto.setQuantity(detail.getQuantity());
                return dto;
            })
            .collect(Collectors.toList());

        response.setMaterialDtlList(materialDtls);
        return response;
    }

    @Override
    @Transactional
    public void approveOgp(GprApprovalDto req) {
        String processNo = req.getProcessNo();
        String type = req.getType();
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer ogpSubProcessId = Integer.parseInt(processNoSplit[1]);
        
        if ("ISN".equals(type)) {
            // Handle ISN type OGP
            OgpMasterEntity ogpMaster = ogpMasterRepository.findById(ogpSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "OGP not found")));

            ogpMaster.setStatus("APPROVED");
            ogpMasterRepository.save(ogpMaster);
        } else {
            // Handle PO type OGP
            OgpMasterPoEntity poOgp = ogpMasterPoRepository.findById(ogpSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "OGP PO not found")));

            poOgp.setStatus("APPROVED");
            ogpMasterPoRepository.save(poOgp);
        }
    }

    @Override
    @Transactional
    public void rejectOgp(GprApprovalDto req) {
        String processNo = req.getProcessNo();
        String type = req.getType();
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer ogpSubProcessId = Integer.parseInt(processNoSplit[1]);
        
        if ("ISN".equals(type)) {
            // Handle ISN type OGP
            OgpMasterEntity ogpMaster = ogpMasterRepository.findById(ogpSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "OGP not found")));

            ogpMaster.setStatus("REJECTED");
            ogpMasterRepository.save(ogpMaster);
        } else {
            // Handle PO type OGP
            OgpMasterPoEntity poOgp = ogpMasterPoRepository.findById(ogpSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "OGP PO not found")));

            poOgp.setStatus("REJECTED");
            ogpMasterPoRepository.save(poOgp);
        }
    }
}
