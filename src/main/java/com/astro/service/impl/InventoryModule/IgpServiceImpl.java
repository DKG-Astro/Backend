package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.IgpService;
import com.astro.repository.InventoryModule.igp.*;
import com.astro.repository.InventoryModule.isn.IssueNoteMasterRepository;
import com.astro.repository.InventoryModule.ogp.OgpDetailRepository;
import com.astro.repository.InventoryModule.ogp.OgpMasterRepository;
import com.astro.entity.InventoryModule.*;
import com.astro.dto.workflow.InventoryModule.igp.*;
import com.astro.exception.*;
import com.astro.constant.AppConstant;
import com.astro.util.CommonUtils;
import org.modelmapper.ModelMapper;

@Service
public class IgpServiceImpl implements IgpService {
    
    @Autowired
    private IgpMasterRepository igpMasterRepository;
    
    @Autowired
    private IgpDetailRepository igpDetailRepository;
    
    @Autowired
    private IssueNoteMasterRepository isnMasterRepository;

    @Autowired
    private OgpMasterRepository ogpMasterRepository;

    @Autowired
    private OgpDetailRepository ogpDetailRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public String saveIgp(IgpDto req) {
        validateOgp(req.getOgpId());
        Integer ogpSubProcessId = Integer.parseInt(req.getOgpId().split("/")[1]);

        // Get OGP details and validate issue note type
        OgpMasterEntity ogpMaster = ogpMasterRepository.findById(ogpSubProcessId)
            .orElseThrow(() -> new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "OGP not found")));

        // Validate issue note type
        IsnMasterEntity issueNote = isnMasterRepository.findById(ogpMaster.getIssueNoteId())
            .orElseThrow(() -> new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Issue Note not found")));

        if ("Non Returnable".equals(issueNote.getIssueNoteType().toString())) {
            throw new BusinessException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Cannot create IGP for Non-Returnable Issue Note"));
        }

        // Get existing IGPs for this OGP
        List<IgpMasterEntity> existingIgpMasters = igpMasterRepository.findByOgpSubProcessId(ogpSubProcessId);
        Map<String, BigDecimal> totalIgpQuantities = new HashMap<>();

        // Calculate total IGP quantities for each asset and locator combination
        for (IgpMasterEntity existingIgp : existingIgpMasters) {
            List<IgpDetailEntity> existingDetails = igpDetailRepository.findByIgpSubProcessId(existingIgp.getIgpSubProcessId());
            for (IgpDetailEntity detail : existingDetails) {
                String key = detail.getAssetId() + "_" + detail.getLocatorId();
                totalIgpQuantities.merge(key, detail.getQuantity(), BigDecimal::add);
            }
        }

        // Validate new IGP quantities against existing ones
        StringBuilder errorMessage = new StringBuilder();
        for (IgpMaterialDtlDto dtl : req.getMaterialDtlList()) {
            String key = dtl.getAssetId() + "_" + dtl.getLocatorId();
            BigDecimal existingQty = totalIgpQuantities.getOrDefault(key, BigDecimal.ZERO);
            
            // Check for duplicate entries
            if (existingIgpMasters.stream()
                    .flatMap(igp -> igpDetailRepository.findByIgpSubProcessId(igp.getIgpSubProcessId()).stream())
                    .anyMatch(detail -> detail.getAssetId().equals(dtl.getAssetId()) 
                            && detail.getLocatorId().equals(dtl.getLocatorId())
                            && detail.getQuantity().equals(dtl.getQuantity()))) {
                errorMessage.append("Duplicate IGP entry found for Asset ID: ")
                    .append(dtl.getAssetId())
                    .append(" at Locator: ")
                    .append(dtl.getLocatorId())
                    .append(" with same quantity. ");
                continue;
            }

            // Check if total quantity exceeds OGP quantity
            BigDecimal newTotalQty = existingQty.add(dtl.getQuantity());
            // You need to implement a method to get OGP quantity for this asset and locator
            BigDecimal ogpQuantity = getOgpQuantity(ogpSubProcessId, dtl.getAssetId(), dtl.getLocatorId());
            
            if (newTotalQty.compareTo(ogpQuantity) > 0) {
                errorMessage.append("Total IGP quantity (")
                    .append(newTotalQty)
                    .append(") exceeds OGP quantity (")
                    .append(ogpQuantity)
                    .append(") for Asset ID: ")
                    .append(dtl.getAssetId())
                    .append(" at Locator: ")
                    .append(dtl.getLocatorId())
                    .append(". ");
            }
        }

        if (errorMessage.length() > 0) {
            throw new BusinessException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                errorMessage.toString()));
        }

        // Create IGP master and details as before
        final IgpMasterEntity igpMaster = new IgpMasterEntity();
        igpMaster.setIgpDate(CommonUtils.convertStringToDateObject(req.getIgpDate()));
        igpMaster.setOgpSubProcessId(ogpSubProcessId);
        igpMaster.setLocationId(req.getLocationId());
        igpMaster.setCreatedBy(req.getCreatedBy());
        igpMaster.setCreateDate(LocalDateTime.now());
        igpMaster.setIgpProcessId("INV" + ogpSubProcessId);

        final IgpMasterEntity savedIgpMaster = igpMasterRepository.save(igpMaster);

        // Save IGP details
        List<IgpDetailEntity> igpDetails = req.getMaterialDtlList().stream()
            .map(dtl -> {
                IgpDetailEntity detail = new IgpDetailEntity();
                detail.setIgpProcessId(savedIgpMaster.getIgpProcessId());
                detail.setIgpSubProcessId(savedIgpMaster.getIgpSubProcessId());
                detail.setIssueNoteId(ogpMaster.getIssueNoteId());  // Fixed: using issue note ID from OGP master
                detail.setAssetId(dtl.getAssetId());
                detail.setLocatorId(dtl.getLocatorId());
                detail.setQuantity(dtl.getQuantity());
                return detail;
            })
            .collect(Collectors.toList());
        
        igpDetailRepository.saveAll(igpDetails);

        return savedIgpMaster.getIgpProcessId() + "/" + savedIgpMaster.getIgpSubProcessId();
    }

    @Override
    public IgpDto getIgpDtls(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer igpSubProcessId = Integer.parseInt(processNoSplit[1]);
        
        IgpMasterEntity igpMaster = igpMasterRepository.findById(igpSubProcessId)
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "IGP not found")));

        List<IgpDetailEntity> igpDetails = igpDetailRepository.findByIgpSubProcessId(igpSubProcessId);

        IgpDto response = modelMapper.map(igpMaster, IgpDto.class);
        response.setIgpDate(CommonUtils.convertDateToString(igpMaster.getIgpDate()));
        response.setIgpId(igpMaster.getIgpProcessId() + "/" + igpMaster.getIgpSubProcessId());

        List<IgpMaterialDtlDto> materialDtls = igpDetails.stream()
            .map(detail -> modelMapper.map(detail, IgpMaterialDtlDto.class))
            .collect(Collectors.toList());

        response.setMaterialDtlList(materialDtls);
        return response;
    }

    private void validateOgp(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2 || !processNoSplit[0].equals("OGP")) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid OGP No."));
        }

        try {
            Integer subProcessId = Integer.parseInt(processNoSplit[1]);
            
            if (!ogpMasterRepository.existsById(subProcessId)) {
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Provided OGP No. is not valid."));
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid OGP number format"));
        }
    }

    private BigDecimal getOgpQuantity(Integer ogpSubProcessId, Integer assetId, Integer locatorId) {
        // Implement this method to get the OGP quantity for the given asset and locator
        return ogpDetailRepository.findQuantityByOgpSubProcessIdAndAssetIdAndLocatorId(
            ogpSubProcessId, assetId, locatorId)
            .orElse(BigDecimal.ZERO);
    }
}