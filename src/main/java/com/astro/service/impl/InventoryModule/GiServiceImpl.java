package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.GiService;
import com.astro.service.InventoryModule.GprnService;
import com.astro.repository.InventoryModule.GiRepository.*;
import com.astro.repository.MaterialMasterRepository;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.entity.MaterialMaster;
import com.astro.entity.InventoryModule.*;
import com.astro.dto.workflow.InventoryModule.GiDto.*;
import com.astro.exception.*;
import com.astro.constant.AppConstant;
import com.astro.util.CommonUtils;
import org.modelmapper.ModelMapper;

@Service
public class GiServiceImpl implements GiService {
    @Value("${filePath}")
    private String bp;

    @Autowired
    private GiMasterRepository gimr;
    
    @Autowired
    private GiMaterialDtlRepository gimdr;
    
    @Autowired
    private GprnService gprnService;
    
    @Autowired
    private AssetMasterRepository amr;

    @Autowired
    private MaterialMasterRepository mmr;

    private final String basePath;

    public GiServiceImpl(@Value("${filePath}") String bp) {
        this.basePath = bp + "/INV";
    }

    @Override
    @Transactional
    public String saveGi(SaveGiDto req) {
        gprnService.validateGprnSubProcessId(req.getGprnNo());

        ModelMapper mapper = new ModelMapper();
        GiMasterEntity gime = new GiMasterEntity();
        gime.setCommissioningDate(CommonUtils.convertStringToDateObject(req.getCommissioningDate()));
        gime.setInstallationDate(CommonUtils.convertStringToDateObject(req.getCommissioningDate()));
        gime.setGprnProcessId(req.getGprnNo().split("/")[0].substring(3));
        gime.setGprnSubProcessId(Integer.parseInt(req.getGprnNo().split("/")[1]));
        gime.setCreateDate(LocalDateTime.now());
        gime.setCreatedBy(req.getCreatedBy());
        gime.setLocationId(req.getLocationId());

        gime = gimr.save(gime);

        List<GiMaterialDtlEntity> gimdeList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        for (GiMaterialDtlDto gmdd : req.getMaterialDtlList()) {
            Optional<GiMaterialDtlEntity> gimdeOpt = gimdr.findByGprnSubProcessIdAndMaterialCode(
                    Integer.parseInt(req.getGprnNo().split("/")[1]), gmdd.getMaterialCode());
                    
            if (gimdeOpt.isPresent()) {
                errorMessage.append("Inspection already done for the provided GPRN No. " + req.getGprnNo()
                        + " and Material Code " + gmdd.getMaterialCode());
                errorFound = true;
                continue;
            } else if (!gimdeOpt.isPresent()
                    && (gmdd.getReceivedQuantity()
                            .compareTo(gmdd.getAcceptedQuantity().add(gmdd.getRejectedQuantity())) != 0)) {
                errorMessage.append("Total received quantity for " + gmdd.getMaterialCode()
                        + " is not equal to accepted quantity + rejected quantity.");
                errorFound = true;
                continue;
            }

            Integer assetId = null;
            if(gmdd.getAcceptedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                assetId = createNewAsset(gmdd, req.getCreatedBy());
            }

            GiMaterialDtlEntity gimde = new GiMaterialDtlEntity();
            mapper.map(gmdd, gimde);
            gimde.setAssetId(assetId);
            gimde.setInspectionSubProcessId(gime.getInspectionSubProcessId());
            gimde.setGprnSubProcessId(Integer.parseInt(req.getGprnNo().split("/")[1]));
            gimde.setGprnProcessId(Integer.parseInt(req.getGprnNo().split("/")[0].substring(3)));

            try {
                String instlRepFileName = CommonUtils.saveBase64Image(gmdd.getInstallationReportBase64(), basePath);
                gimde.setInstallationReportFileName(instlRepFileName);
            } catch (Exception e) {
                // Log error
            }

            gimdeList.add(gimde);
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }

        gimdr.saveAll(gimdeList);
        return "INV" + gime.getGprnProcessId() + "/" + gime.getInspectionSubProcessId();
    }

    @Override
    public Map<String, Object> getGiDtls(String processNo) {
        ModelMapper mapper = new ModelMapper();
        String[] processNoSplit = processNo.split("/");
        
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer inspectionId = Integer.parseInt(processNoSplit[1]);
        GiMasterEntity gime = gimr.findById(inspectionId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Goods Inspection not found for the provided process ID.")));

        List<GiMaterialDtlEntity> gimdeList = gimdr.findByInspectionSubProcessId(gime.getInspectionSubProcessId());
        List<GiMaterialDtlDto> materialDtlListRes = gimdeList.stream()
                .map(gimde -> {
                    GiMaterialDtlDto gmdd = mapper.map(gimde, GiMaterialDtlDto.class);
                    gmdd.setAssetId(gimde.getAssetId());

                    Optional<AssetMasterEntity> aeOpt = amr.findById(gimde.getAssetId());
                    if(aeOpt.isPresent()) {
                        gmdd.setAssetDesc(aeOpt.get().getAssetDesc());
                        gmdd.setUomId(aeOpt.get().getUomId());
                    }
                    
                    try {
                        String imageBase64 = CommonUtils.convertImageToBase64(gimde.getInstallationReportFileName(),
                                basePath);
                        gmdd.setInstallationReportBase64(imageBase64);
                    } catch (Exception e) {
                        // Log error
                    }
                    return gmdd;
                })
                .collect(Collectors.toList());

        SaveGiDto giRes = new SaveGiDto();
        giRes.setInspectionNo(processNo);
        giRes.setGprnNo(processNo);
        giRes.setInstallationDate(CommonUtils.convertDateToString(gime.getInstallationDate()));
        giRes.setCommissioningDate(CommonUtils.convertDateToString(gime.getCommissioningDate()));
        giRes.setMaterialDtlList(materialDtlListRes);

        Map<String, Object> combinedRes = new HashMap<>();
        combinedRes.put("giDtls", giRes);
        combinedRes.put("gprnDtls", gprnService.getGprnDtls(processNo.split("/")[0] + "/" + gime.getGprnSubProcessId()));

        return combinedRes;
    }

    private Integer createNewAsset(GiMaterialDtlDto materialDtl, Integer createdBy) {
        MaterialMaster mme = mmr.findById(materialDtl.getMaterialCode())
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Material not found for the provided material code.")));
        Optional<AssetMasterEntity> ameOpt = amr.findByMaterialCodeAndMaterialDescAndMakeNoAndModelNoAndSerialNoAndUomId(
                materialDtl.getMaterialCode(),
                materialDtl.getMaterialDesc(),
                materialDtl.getMakeNo(),
                materialDtl.getModelNo(),
                materialDtl.getSerialNo(),
                materialDtl.getUomId()
        );

        if(ameOpt.isEmpty()) {
            AssetMasterEntity ame = new ModelMapper().map(materialDtl, AssetMasterEntity.class);
            ame.setAssetDesc(materialDtl.getMaterialDesc());
            ame.setCreateDate(LocalDateTime.now());
            ame.setCreatedBy(createdBy);
            ame.setUpdatedDate(LocalDateTime.now());
            ame.setUnitPrice(mme.getUnitPrice());
            amr.save(ame);
            return ame.getAssetId();
        }

        return ameOpt.get().getAssetId();
    }

    @Override
    public void validateGiSubProcessId(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid GI No."));
        }

        Integer subProcessId = Integer.parseInt(processNoSplit[1]);
        if (!gimr.existsById(subProcessId)) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Provided GI No. is not valid."));
        }
    }
}