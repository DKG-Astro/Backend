package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.OgpService;
import com.astro.repository.InventoryModule.isn.IssueNoteMasterRepository;
import com.astro.repository.InventoryModule.ogp.OgpDetailRepository;
import com.astro.repository.InventoryModule.ogp.OgpMasterRepository;
import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpMaterialDtlDto;
import com.astro.entity.InventoryModule.OgpDetailEntity;
import com.astro.entity.InventoryModule.OgpMasterEntity;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.util.CommonUtils;
import org.modelmapper.ModelMapper;

@Service
public class OgpServiceImpl implements OgpService {
    
    @Autowired
    private OgpMasterRepository ogpMasterRepository;
    
    @Autowired
    private OgpDetailRepository ogpDetailRepository;
    
    @Autowired
    private IssueNoteMasterRepository isnMasterRepository;

    @Autowired
    private ModelMapper modelMapper;

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
        final OgpMasterEntity ogpMaster = modelMapper.map(req, OgpMasterEntity.class);
        ogpMaster.setOgpDate(CommonUtils.convertStringToDateObject(req.getOgpDate()));
        ogpMaster.setIssueNoteId(issueNoteId);
        ogpMaster.setCreateDate(LocalDateTime.now());
        ogpMaster.setOgpProcessId("INV" + issueNoteId);

        final OgpMasterEntity savedOgpMaster = ogpMasterRepository.save(ogpMaster);

        // Save OGP details
        List<OgpDetailEntity> ogpDetails = req.getMaterialDtlList().stream()
            .map(dtl -> {
                OgpDetailEntity detail = modelMapper.map(dtl, OgpDetailEntity.class);
                detail.setOgpProcessId(savedOgpMaster.getOgpProcessId()); 
                detail.setOgpSubProcessId(savedOgpMaster.getOgpSubProcessId());
                detail.setIssueNoteId(issueNoteId);
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
            .map(detail -> modelMapper.map(detail, OgpMaterialDtlDto.class))
            .collect(Collectors.toList());

        response.setMaterialDtlList(materialDtls);
        return response;
    }

    public void validateIsn(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2 || !processNoSplit[0].equals("ISN")) {
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
}
