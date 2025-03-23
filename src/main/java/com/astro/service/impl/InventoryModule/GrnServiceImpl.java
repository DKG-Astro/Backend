package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.GrnService;
import com.astro.service.InventoryModule.GiService;
import com.astro.repository.InventoryModule.grn.*;
import com.astro.repository.InventoryModule.GiRepository.GiMaterialDtlRepository;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.repository.ohq.OhqMasterRepository;
import com.astro.entity.InventoryModule.*;
import com.astro.dto.workflow.InventoryModule.grn.*;
import com.astro.exception.*;
import com.astro.constant.AppConstant;
import com.astro.util.CommonUtils;
import org.modelmapper.ModelMapper;

@Service
public class GrnServiceImpl implements GrnService {
    @Autowired
    private GrnMasterRepository grnmr;
    
    @Autowired
    private GrnMaterialDtlRepository grnmdr;
    
    @Autowired
    private GiService giService;
    
    @Autowired
    private GiMaterialDtlRepository gimdr;
    
    @Autowired
    private AssetMasterRepository amr;
    
    @Autowired
    private OhqMasterRepository ohqmr;

    @Override
    @Transactional
    public String saveGrn(GrnDto req) {
        giService.validateGiSubProcessId(req.getGiNo());

        ModelMapper mapper = new ModelMapper();

        GrnMasterEntity grnMaster = new GrnMasterEntity();
        grnMaster.setGrnDate(CommonUtils.convertStringToDateObject(req.getGrnDate()));
        grnMaster.setInstallationDate(CommonUtils.convertStringToDateObject(req.getInstallationDate()));
        grnMaster.setCommissioningDate(CommonUtils.convertStringToDateObject(req.getCommissioningDate()));
        grnMaster.setCreatedBy(req.getCreatedBy());
        grnMaster.setSystemCreatedBy(Integer.parseInt(req.getCreatedBy()));
        grnMaster.setCreateDate(LocalDateTime.now());
        grnMaster.setLocationId(req.getLocationId());
        grnMaster.setGiProcessId(req.getGiNo().split("/")[0].substring(3));
        grnMaster.setGiSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
        grnMaster.setGrnProcessId(req.getGiNo().split("/")[0].substring(3));

        grnMaster = grnmr.save(grnMaster);

        List<GrnMaterialDtlEntity> grnMaterialDtlList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        List<GiMaterialDtlEntity> giMaterialList = gimdr.findByInspectionSubProcessId(
                Integer.parseInt(req.getGiNo().split("/")[1]));

        for (GrnMaterialDtlDto materialDtl : req.getMaterialDtlList()) {
            Optional<GiMaterialDtlEntity> giMaterial = giMaterialList.stream()
                    .filter(gi -> gi.getAssetId().equals(materialDtl.getAssetId()))
                    .findFirst();

            if (giMaterial.isEmpty()) {
                errorMessage.append("Asset ID " + materialDtl.getAssetId() + " not found in GI. ");
                errorFound = true;
                continue;
            }

            BigDecimal previouslyReceivedQty = grnmdr.findByGiSubProcessIdAndAssetId(
                    Integer.parseInt(req.getGiNo().split("/")[1]),
                    materialDtl.getAssetId())
                    .stream()
                    .map(GrnMaterialDtlEntity::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalReceivedQty = previouslyReceivedQty.add(materialDtl.getAcceptedQuantity());

            if (totalReceivedQty.compareTo(giMaterial.get().getAcceptedQuantity()) > 0) {
                errorMessage.append("Total received quantity for Asset ID " + materialDtl.getAssetId() +
                        " exceeds accepted quantity in GI. ");
                errorFound = true;
                continue;
            }

            GrnMaterialDtlEntity grnMaterialDtl = new GrnMaterialDtlEntity();
            mapper.map(materialDtl, grnMaterialDtl);
            grnMaterialDtl.setQuantity(materialDtl.getReceivedQuantity());
            grnMaterialDtl.setGrnProcessId(grnMaster.getGrnProcessId());
            grnMaterialDtl.setGrnSubProcessId(grnMaster.getGrnSubProcessId());
            grnMaterialDtl.setGiSubProcessId(grnMaster.getGiSubProcessId());

            grnMaterialDtlList.add(grnMaterialDtl);

            updateAssetAndOhq(materialDtl);
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }

        grnmdr.saveAll(grnMaterialDtlList);

        return "INV" + grnMaster.getGrnProcessId() + "/" + grnMaster.getGrnSubProcessId();
    }

    private void updateAssetAndOhq(GrnMaterialDtlDto materialDtl) {
        AssetMasterEntity asset = amr.findById(materialDtl.getAssetId())
                .orElseThrow(() -> new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Asset not found with ID: " + materialDtl.getAssetId())));

        if (asset.getInitQuantity() == null || asset.getInitQuantity().compareTo(BigDecimal.ZERO) == 0) {
            asset.setInitQuantity(materialDtl.getReceivedQuantity());
            amr.save(asset);
        }

        Optional<OhqMasterEntity> existingOhq = ohqmr.findByAssetIdAndLocatorId(
                materialDtl.getAssetId(), 
                materialDtl.getLocatorId());

        OhqMasterEntity ohq;
        if (existingOhq.isPresent()) {
            ohq = existingOhq.get();
            ohq.setQuantity(ohq.getQuantity().add(materialDtl.getReceivedQuantity()));
        } else {
            ohq = new OhqMasterEntity();
            ohq.setAssetId(materialDtl.getAssetId());
            ohq.setLocatorId(materialDtl.getLocatorId());
            ohq.setQuantity(materialDtl.getReceivedQuantity());
            ohq.setBookValue(materialDtl.getBookValue());
            ohq.setDepriciationRate(materialDtl.getDepriciationRate());
            ohq.setUnitPrice(materialDtl.getBookValue());
        }
        ohqmr.save(ohq);
    }

    @Override
    public Map<String, Object> getGrnDtls(String processNo) {
        ModelMapper mapper = new ModelMapper();
        String[] processNoSplit = processNo.split("/");
        
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer grnSubProcessId = Integer.parseInt(processNoSplit[1]);

        GrnMasterEntity grnMaster = grnmr.findById(grnSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "GRN not found for the provided process ID.")));

        List<GrnMaterialDtlEntity> grnMaterialList = grnmdr.findByGrnSubProcessId(grnMaster.getGrnSubProcessId());

        List<GrnMaterialDtlDto> materialDtlListRes = grnMaterialList.stream()
                .map(material -> mapper.map(material, GrnMaterialDtlDto.class))
                .collect(Collectors.toList());

        GrnDto grnRes = new GrnDto();
        grnRes.setGrnNo(processNo);
        grnRes.setGiNo("INV" + grnMaster.getGiProcessId() + "/" + grnMaster.getGiSubProcessId());
        grnRes.setGrnDate(CommonUtils.convertDateToString(grnMaster.getGrnDate()));
        grnRes.setInstallationDate(CommonUtils.convertDateToString(grnMaster.getInstallationDate()));
        grnRes.setCommissioningDate(CommonUtils.convertDateToString(grnMaster.getCommissioningDate()));
        grnRes.setCreatedBy(grnMaster.getCreatedBy());
        grnRes.setSystemCreatedBy(grnMaster.getSystemCreatedBy());
        grnRes.setLocationId(grnMaster.getLocationId());
        grnRes.setMaterialDtlList(materialDtlListRes);

        Map<String, Object> giDetails = giService.getGiDtls("INV" + grnMaster.getGiProcessId() + "/" + 
                grnMaster.getGiSubProcessId());
        
        Map<String, Object> combinedRes = new HashMap<>();
        combinedRes.put("grnDtls", grnRes);
        combinedRes.put("giDtls", giDetails.get("giDtls"));
        combinedRes.put("gprnDtls", giDetails.get("gprnDtls"));

        return combinedRes;
    }
}