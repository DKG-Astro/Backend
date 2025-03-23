package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.IsnService;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.repository.InventoryModule.isn.*;
import com.astro.repository.ohq.OhqMasterRepository;
import com.astro.entity.InventoryModule.*;
import com.astro.dto.workflow.InventoryModule.isn.*;
import com.astro.exception.*;
import com.astro.constant.AppConstant;
import com.astro.util.CommonUtils;
import org.modelmapper.ModelMapper;

@Service
public class IsnServiceImpl implements IsnService {
    @Autowired
    private IssueNoteMasterRepository isnmr;
    
    @Autowired
    private IssueNoteMaterialDtlRepository isnmdr;
    
    @Autowired
    private OhqMasterRepository ohqmr;

    @Autowired
    private AssetMasterRepository amr;

    @Override
    @Transactional
    public String saveIsn(IsnDto req) {
        ModelMapper mapper = new ModelMapper();

        IsnMasterEntity isnMaster = new IsnMasterEntity();
        isnMaster.setIssueDate(CommonUtils.convertStringToDateObject(req.getIssueDate()));
        isnMaster.setCreatedBy(req.getCreatedBy());
        isnMaster.setCreateDate(LocalDateTime.now());
        isnMaster.setLocationId(req.getLocationId());
        isnMaster.setIndentorName(req.getIndentorName());
        isnMaster.setConsigneeDetail(req.getConsigneeDetail());
        isnMaster.setFieldStation(req.getFieldStation());
        isnMaster.setIssueNoteType(req.getIssueNoteType());  

        isnMaster = isnmr.save(isnMaster);

        List<IsnMaterialDtlEntity> isnMaterialDtlList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        for (IsnMaterialDtlDto materialDtl : req.getMaterialDtlList()) {
            Optional<OhqMasterEntity> ohq = ohqmr.findByAssetIdAndLocatorId(
                    materialDtl.getAssetId(), 
                    materialDtl.getLocatorId());

            if (ohq.isEmpty()) {
                errorMessage.append("Asset ID " + materialDtl.getAssetId() + 
                        " not found in location. ");
                errorFound = true;
                continue;
            }

            if (ohq.get().getQuantity().compareTo(materialDtl.getQuantity()) < 0) {
                errorMessage.append("Issue quantity exceeds available quantity for Asset ID " + 
                        materialDtl.getAssetId() + ". ");
                errorFound = true;
                continue;
            }

            IsnMaterialDtlEntity isnMaterialDtl = new IsnMaterialDtlEntity();
            mapper.map(materialDtl, isnMaterialDtl);
            isnMaterialDtl.setIssueNoteId(isnMaster.getIssueNoteId());

            isnMaterialDtlList.add(isnMaterialDtl);

            // Update OHQ entry
            ohq.get().setQuantity(ohq.get().getQuantity().subtract(materialDtl.getQuantity()));
            ohqmr.save(ohq.get());
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }

        isnmdr.saveAll(isnMaterialDtlList);

        return "INV" + isnMaster.getIssueNoteId();
    }

    @Override
    public IsnDto getIsnDtls(String processNo) {
        ModelMapper mapper = new ModelMapper();
        String[] processNoSplit = processNo.split("/");
        
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer isnSubProcessId = Integer.parseInt(processNoSplit[1]);

        IsnMasterEntity isnMaster = isnmr.findById(isnSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "ISN not found for the provided process ID.")));

        List<IsnMaterialDtlEntity> isnMaterialList = isnmdr.findByIssueNoteId(isnMaster.getIssueNoteId());

        List<IsnMaterialDtlDto> materialDtlListRes = isnMaterialList.stream()
                .map(material -> mapper.map(material, IsnMaterialDtlDto.class))
                .collect(Collectors.toList());

        IsnDto isnRes = new IsnDto();
        isnRes.setIssueNoteNo(processNo);
        isnRes.setConsigneeDetail(isnMaster.getConsigneeDetail());
        isnRes.setFieldStation(isnMaster.getFieldStation());
        isnRes.setIndentorName(isnMaster.getIndentorName());
        isnRes.setIssueDate(CommonUtils.convertDateToString(isnMaster.getIssueDate()));
        isnRes.setCreatedBy(isnMaster.getCreatedBy());
        isnRes.setLocationId(isnMaster.getLocationId());
        isnRes.setMaterialDtlList(materialDtlListRes);

        return isnRes;
    }

    @Override
    public List<IsnAssetOhqDtlsDto> getAssetMasterAndOhqDtls() {
        List<IsnAssetOhqDtlsDto> response = new ArrayList<>();

        List<AssetMasterEntity> assets = amr.findAll();

        for (AssetMasterEntity asset : assets) {
            List<OhqMasterEntity> ohqList = ohqmr.findByAssetId(asset.getAssetId());

            if (ohqList.isEmpty() || ohqList.stream()
                    .allMatch(ohq -> ohq.getQuantity().compareTo(BigDecimal.ZERO) == 0)) {
                continue;
            }

            IsnAssetOhqDtlsDto assetDto = new IsnAssetOhqDtlsDto();
            assetDto.setAssetId(asset.getAssetId());
            assetDto.setAssetDesc(asset.getAssetDesc());
            assetDto.setUomId(asset.getUomId());

            List<IsnOhqDtlsDto> ohqDtoList = new ArrayList<>();
            for (OhqMasterEntity ohq : ohqList) {
                if (ohq.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    IsnOhqDtlsDto ohqDto = new IsnOhqDtlsDto();
                    ohqDto.setLocatorId(ohq.getLocatorId().toString());
                    ohqDto.setQuantity(ohq.getQuantity());
                    ohqDtoList.add(ohqDto);
                }
            }

            if (!ohqDtoList.isEmpty()) {
                assetDto.setQtyList(ohqDtoList);
                response.add(assetDto);
            }
        }

        return response;
    }
}