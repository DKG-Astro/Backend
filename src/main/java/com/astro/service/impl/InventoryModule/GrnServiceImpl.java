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
import com.astro.repository.InventoryModule.igp.IgpMasterRepository;
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

    @Autowired
    private IgpMasterRepository igpMasterRepository;

    @Override
    @Transactional
    public String saveGrn(GrnDto req) {
        if ("GI".equalsIgnoreCase(req.getGrnType())) {
            giService.validateGiSubProcessId(req.getGiNo());
        } else {
            validateIgp(req.getGiNo());
        }

        ModelMapper mapper = new ModelMapper();
        GrnMasterEntity grnMaster = new GrnMasterEntity();

        // Handle dates with null checks
        if (req.getGrnDate() != null && !req.getGrnDate().trim().isEmpty()) {
            grnMaster.setGrnDate(CommonUtils.convertStringToDateObject(req.getGrnDate()));
        }

        grnMaster.setCreatedBy(req.getCreatedBy());
        grnMaster.setSystemCreatedBy(Integer.parseInt(req.getCreatedBy()));
        grnMaster.setCreateDate(LocalDateTime.now());
        grnMaster.setLocationId(req.getLocationId());
        grnMaster.setGrnType(req.getGrnType());
        grnMaster.setGrnProcessId(req.getGiNo().split("/")[0].substring(3));

        if ("GI".equalsIgnoreCase(req.getGrnType())) {
            if (req.getInstallationDate() != null && !req.getInstallationDate().trim().isEmpty()) {
                grnMaster.setInstallationDate(CommonUtils.convertStringToDateObject(req.getInstallationDate()));
            }
            if (req.getCommissioningDate() != null && !req.getCommissioningDate().trim().isEmpty()) {
                grnMaster.setCommissioningDate(CommonUtils.convertStringToDateObject(req.getCommissioningDate()));
            }
            grnMaster.setGiProcessId(req.getGiNo().split("/")[0].substring(3));
            grnMaster.setGiSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
        } else {
            grnMaster.setIgpProcessId(req.getGiNo().split("/")[0].substring(3));
            grnMaster.setIgpSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
        }

        grnMaster = grnmr.save(grnMaster);

        List<GrnMaterialDtlEntity> grnMaterialDtlList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        if ("GI".equalsIgnoreCase(req.getGrnType())) {
            // GI validation logic
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
                grnMaterialDtl.setQuantity(materialDtl.getAcceptedQuantity());
                grnMaterialDtl.setGrnProcessId(grnMaster.getGrnProcessId());
                grnMaterialDtl.setGiSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
                grnMaterialDtl.setGrnSubProcessId(grnMaster.getGrnSubProcessId());
                grnMaterialDtlList.add(grnMaterialDtl);

                updateAssetAndOhq(materialDtl);
            }
        } else {
            // IGP validation logic - skip GI-specific validations
            for (GrnMaterialDtlDto materialDtl : req.getMaterialDtlList()) {
                GrnMaterialDtlEntity grnMaterialDtl = new GrnMaterialDtlEntity();
                mapper.map(materialDtl, grnMaterialDtl);
                grnMaterialDtl.setQuantity(materialDtl.getAcceptedQuantity());
                grnMaterialDtl.setGrnProcessId(grnMaster.getGrnProcessId());
                grnMaterialDtl.setGrnSubProcessId(grnMaster.getGrnSubProcessId());
                grnMaterialDtl.setIgpSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
                
                // Copy financial values from existing OHQ if available
                Optional<OhqMasterEntity> existingOhq = ohqmr.findByAssetIdAndLocatorId(
                    materialDtl.getAssetId(),
                    materialDtl.getLocatorId());

                if (existingOhq.isPresent()) {
                    OhqMasterEntity ohq = existingOhq.get();
                    grnMaterialDtl.setBookValue(ohq.getBookValue());
                    grnMaterialDtl.setDepriciationRate(ohq.getDepriciationRate());
                } else {
                    // If no existing OHQ, use values from materialDtl
                    grnMaterialDtl.setBookValue(materialDtl.getBookValue());
                    grnMaterialDtl.setDepriciationRate(materialDtl.getDepriciationRate());
                }
                
                grnMaterialDtlList.add(grnMaterialDtl);
                updateAssetAndOhq(materialDtl);
            }
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
        System.out.println("UPDATE CALLED");
        AssetMasterEntity asset = amr.findById(materialDtl.getAssetId())
                .orElseThrow(() -> new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Asset not found with ID: " + materialDtl.getAssetId())));

        if (asset.getInitQuantity() == null || asset.getInitQuantity().compareTo(BigDecimal.ZERO) == 0) {
            asset.setInitQuantity(materialDtl.getAcceptedQuantity());
            amr.save(asset);
        }

        List<OhqMasterEntity> ohqList = ohqmr.findByAssetId(asset.getAssetId());
        
        Optional<OhqMasterEntity> existingOhq = ohqmr.findByAssetIdAndLocatorId(
                materialDtl.getAssetId(),
                materialDtl.getLocatorId());

        System.out.println("EXISTUNGOHQ CALLED");

        OhqMasterEntity ohq;
        if (existingOhq.isPresent()) {
            System.out.println("EXISTING OHQ PRESENT");
            ohq = existingOhq.get();
            BigDecimal currentQty = ohq.getQuantity() != null ? ohq.getQuantity() : BigDecimal.ZERO;
            ohq.setQuantity(currentQty.add(materialDtl.getAcceptedQuantity()));
        } else {
            System.out.println("EXISTING OHQ NOT PRESENT");
            ohq = new OhqMasterEntity();
            ohq.setAssetId(materialDtl.getAssetId());
            ohq.setLocatorId(materialDtl.getLocatorId());
            ohq.setQuantity(materialDtl.getAcceptedQuantity());
            
            if (!ohqList.isEmpty()) {
                System.out.println("NOT EMPTY");
                OhqMasterEntity existingOhqRecord = ohqList.get(0);
                ohq.setBookValue(existingOhqRecord.getBookValue());
                ohq.setDepriciationRate(existingOhqRecord.getDepriciationRate());
                ohq.setUnitPrice(existingOhqRecord.getUnitPrice());
            } else {
                System.out.println("EMTOTY");
                ohq.setBookValue(materialDtl.getBookValue());
                ohq.setDepriciationRate(materialDtl.getDepriciationRate());
                ohq.setUnitPrice(materialDtl.getBookValue());
            }
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

    private void validateIgp(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2 || !processNoSplit[0].startsWith("INV")) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid IGP No. Format should be IGP{number}/{number}"));
        }

        try {
            Integer subProcessId = Integer.parseInt(processNoSplit[1]);
            if (!igpMasterRepository.existsById(subProcessId)) {
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "IGP not found with the provided ID"));
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid IGP number format"));
        }
    }
}