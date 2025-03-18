package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.controller.InventoryModule.GRIController;
import com.astro.dto.workflow.InventoryModule.GprnDto.GetGprnDtlsDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.MaterialDtlDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnMaterialDtlDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvMaterialDtlDto;
import com.astro.entity.InventoryModule.AssetMasterEntity;
import com.astro.entity.InventoryModule.GiMasterEntity;
import com.astro.entity.InventoryModule.GiMaterialDtlEntity;
import com.astro.entity.InventoryModule.GprnMasterEntity;
import com.astro.entity.InventoryModule.GprnMaterialDtlEntity;
import com.astro.entity.InventoryModule.GrnMasterEntity;
import com.astro.entity.InventoryModule.GrnMaterialDtlEntity;
import com.astro.entity.InventoryModule.GrvMasterEntity;
import com.astro.entity.InventoryModule.GrvMaterialDtlEntity;
import com.astro.entity.InventoryModule.OhqMasterEntity;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.repository.InventoryModule.GiRepository.GiMasterRepository;
import com.astro.repository.InventoryModule.GiRepository.GiMaterialDtlRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnMasterRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialDtlRepository;
import com.astro.repository.InventoryModule.grn.GrnMasterRepository;
import com.astro.repository.InventoryModule.grn.GrnMaterialDtlRepository;
import com.astro.repository.InventoryModule.grv.GrvMasterRepository;
import com.astro.repository.InventoryModule.grv.GrvMaterialDtlRepository;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderRepository;
import com.astro.repository.ohq.OhqMasterRepository;
import com.astro.service.ProcessService;
import com.astro.util.CommonUtils;

import springfox.documentation.schema.Model;

import com.astro.exception.InvalidInputException;
import com.astro.dto.workflow.InventoryModule.GiDto.GiMaterialDtlDto;
import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Value("${filePath}")
    private String bp;

    @Autowired
    private GprnMasterRepository gmr;

    @Autowired
    private PurchaseOrderRepository por;

    @Autowired
    private GprnMaterialDtlRepository gmdr;

    @Autowired
    private GiMasterRepository gimr;

    @Autowired
    private GiMaterialDtlRepository gimdr;

    @Autowired
    private GrvMasterRepository grvMasterRepository;

    @Autowired
    private GrvMaterialDtlRepository grvMaterialDtlRepository;

    @Autowired
    private GrnMasterRepository grnmr;

    @Autowired
    private GrnMaterialDtlRepository grnmdr;

    @Autowired
    private OhqMasterRepository ohqmr;

    @Autowired
    private AssetMasterRepository amr;

    private final String basePath = bp + "/INV";

    private static final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);

    private void validatePoId(String poId) {
        if (!por.existsById(poId)) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Provided PO ID is not valid."));
        }
    }

    @Override
    @Transactional
    public String saveGprn(SaveGprnDto req) {
        // validatePoId(req.getPoId());

        GprnMasterEntity gme = new GprnMasterEntity();

        ModelMapper mapper = new ModelMapper();
        mapper.map(req, gme);
        gme.setDate(CommonUtils.convertStringToDateObject(req.getDate()));
        gme.setCreateDate(LocalDateTime.now());
        gme.setDeliveryDate(CommonUtils.convertStringToDateObject(req.getDeliveryDate()));
        gme.setSupplyExpectedDate(CommonUtils.convertStringToDateObject(req.getSupplyExpectedDate()));
        gme.setUpdateDate(LocalDateTime.now());
        gme.setProcessId(req.getPoId().substring(2));
        gme.setLocationId(req.getLocationId());

        gme = gmr.save(gme);

        List<GprnMaterialDtlEntity> saveDtlEntityList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder(); // to add error messages and throw at once.
        Boolean errorFound = false;

        // fetch material details based on poId and material code
        // add the received quantities to get the previous received quantity
        for (MaterialDtlDto dtl : req.getMaterialDtlList()) {
            List<GprnMaterialDtlEntity> gmdeList = gmdr.findByPoIdAndMaterialCode(req.getPoId(), dtl.getMaterialCode());

            // previously received quantity
            BigDecimal prevRecQuant = gmdeList.stream().map(gmde -> {
                return gmde.getReceivedQuantity();
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            // total received quantity
            BigDecimal totalRecQuant = prevRecQuant.add(dtl.getReceivedQuantity());
            if (totalRecQuant.compareTo(dtl.getOrderedQuantity()) > 0) {
                errorMessage.append(
                        "Total received quantity for " + dtl.getMaterialCode() + " is more than ordered quantity.");
                errorFound = true;
                continue;
            } else if (errorFound)
                continue;

            // create new entity
            GprnMaterialDtlEntity gmde = new GprnMaterialDtlEntity();
            mapper.map(dtl, gmde);
            gmde.setProcessId(gme.getProcessId());
            gmde.setPoId(gme.getPoId());
            gmde.setSubProcessId(gme.getSubProcessId());
            try {
                String imageFileName = CommonUtils.saveBase64Image(dtl.getImageBase64(), basePath);
                gmde.setFileName(imageFileName);
            } catch (Exception e) {
                throw new InvalidInputException(new ErrorDetails(
                        AppConstant.FILE_UPLOAD_ERROR,
                        AppConstant.USER_INVALID_INPUT,
                        AppConstant.ERROR_TYPE_CORRUPTED,
                        "Error while uploading image."));
            }

            saveDtlEntityList.add(gmde);
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }
        gmdr.saveAll(saveDtlEntityList);

        // the process no. that we pass to user
        return "INV" + gme.getProcessId() + "/" + gme.getSubProcessId();
    }

    @Override
    public Object getSubProcessDtls(String processStage, String processNo) {
        if (Objects.isNull(processNo) || processNo.isEmpty() || Objects.isNull(processStage)
                || processStage.isEmpty()) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Process Id or Process Stage is empty."));
        }

        switch (processStage) {
            case "GPRN":
                return getGprnDtls(processNo);
            case "GI":
                return getGiDtlsDto(processNo);
            case "GRV":
                return getGrvDtls(processNo);
            case "GRN":
                return getGrnDtls(processNo);
            default:
                throw new BusinessException(
                        new ErrorDetails(AppConstant.ERROR_TYPE_CODE_DB,
                                AppConstant.ERROR_TYPE_CODE_DB,
                                AppConstant.ERROR_TYPE_ERROR,
                                "Invalid process stage."));
        }

    }

    private SaveGprnDto getGprnDtls(String processNo) {
        ModelMapper mapper = new ModelMapper();
        // processNo that we provide user is of the form INV[po number]/[sub_process_id]
        // fetch just subProcessId
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            System.out.println("EXCEPTION GPRN: " + processNo);
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer subProcessId = Integer.parseInt(processNoSplit[1]);

        // fetch the processId from subProcessId
        GprnMasterEntity gme = gmr.findById(subProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Process not found for the provided process ID.")));

        // fetch material details using subprocess id

        List<GprnMaterialDtlEntity> gmdeList = gmdr.findBySubProcessId(gme.getSubProcessId());

        // map the entity to dto
        List<MaterialDtlDto> materialDtlListRes = gmdeList.stream()
                .map(gmde -> {
                    MaterialDtlDto mdd = mapper.map(gmde, MaterialDtlDto.class);
                    try {
                        String imageBase64 = CommonUtils.convertImageToBase64(gmde.getFileName(), basePath);
                        mdd.setImageBase64(imageBase64);
                    } catch (Exception e) {
                        System.out.println("EXCEPTION: " + e.getMessage());
                    }

                    return mdd;

                })
                .collect(Collectors.toList());

        SaveGprnDto gprnRes = mapper.map(gme, SaveGprnDto.class);
        gprnRes.setDate(CommonUtils.convertDateToString(gme.getDate()));
        gprnRes.setSupplyExpectedDate(CommonUtils.convertDateToString(gme.getSupplyExpectedDate()));
        gprnRes.setDeliveryDate(CommonUtils.convertDateToString(gme.getDeliveryDate()));
        gprnRes.setMaterialDtlList(materialDtlListRes);

        return gprnRes;

    }

    @Override
    @Transactional
    public String saveGi(SaveGiDto req) {
        validateGprnSubProcessId(req.getGprnNo());

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
        StringBuilder errorMessage = new StringBuilder(); // to add error messages and throw at once.
        Boolean errorFound = false;

        for (GiMaterialDtlDto gmdd : req.getMaterialDtlList()) {
            // for sub process and material code, if inspection already done
            // throw error
            Optional<GiMaterialDtlEntity> gimdeOpt = gimdr.findByGprnSubProcessIdAndMaterialCode(
                    Integer.parseInt(req.getGprnNo().split("/")[1]), gmdd.getMaterialCode());
            if (gimdeOpt.isPresent()) {
                errorMessage.append("Inspection already done for the provided GPRN No. " + req.getGprnNo()
                        + " and Material Code " + gmdd.getMaterialCode());
                errorFound = true;
                continue;
                // throw new InvalidInputException(new ErrorDetails(
                // AppConstant.USER_INVALID_INPUT,
                // AppConstant.ERROR_TYPE_CODE_VALIDATION,
                // AppConstant.ERROR_TYPE_VALIDATION,
                // "Inspection already done for the provided GPRN No. " + req.getProcessNo() + "
                // and Material Code " + gmdd.getMaterialCode()));
            } else if (!gimdeOpt.isPresent()
                    && (gmdd.getReceivedQuantity()
                            .compareTo(gmdd.getAcceptedQuantity().add(gmdd.getRejectedQuantity())) != 0)) {
                errorMessage.append("Total received quantity for " + gmdd.getMaterialCode()
                        + " is not equal to accepted quantity + rejected quantity.");
                errorFound = true;
                continue;
            } else if (errorFound)
                continue;

            Integer assetId = null;
            if(gmdd.getAcceptedQuantity().compareTo(BigDecimal.ZERO) > 0){
                assetId = createNewAsset(gmdd);
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
    @Transactional
    public String saveGrv(GrvDto req) {
        validateGiSubProcessId(req.getGiNo());

        ModelMapper mapper = new ModelMapper();

        GrvMasterEntity grvMaster = new GrvMasterEntity();
        grvMaster.setDate(CommonUtils.convertStringToDateObject(req.getDate()));
        grvMaster.setCreatedBy(req.getCreatedBy());
        grvMaster.setCreateDate(LocalDateTime.now());
        grvMaster.setGiProcessId(req.getGiNo().split("/")[0].substring(3));
        grvMaster.setGiSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
        grvMaster.setGrvProcessId(req.getGiNo().split("/")[0].substring(3));
        grvMaster.setLocationId(req.getLocationId());

        grvMaster = grvMasterRepository.save(grvMaster);

        List<GrvMaterialDtlEntity> grvMaterialDtlList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        for (GrvMaterialDtlDto materialDtl : req.getMaterialDtlList()) {
            Optional<GrvMaterialDtlEntity> existingGrv = grvMaterialDtlRepository
                    .findByGiSubProcessIdAndMaterialCode(Integer.parseInt(req.getGiNo().split("/")[1]),
                            materialDtl.getMaterialCode());

            if (existingGrv.isPresent()) {
                errorMessage.append("GRV already exists for GI No. " + req.getGiNo() +
                        " and Material Code " + materialDtl.getMaterialCode() + ". ");
                errorFound = true;
                continue;
            }

            if (materialDtl.getRejectedQuantity().compareTo(materialDtl.getReturnQuantity()) < 0) {
                errorMessage.append("Return quantity cannot be greater than rejected quantity for material " +
                        materialDtl.getMaterialCode() + ". ");
                errorFound = true;
                continue;
            }

            GrvMaterialDtlEntity grvMaterialDtl = new GrvMaterialDtlEntity();
            mapper.map(materialDtl, grvMaterialDtl);
            grvMaterialDtl.setGrvProcessId(grvMaster.getGrvProcessId());
            grvMaterialDtl.setGrvSubProcessId(grvMaster.getGrvSubProcessId());
            grvMaterialDtl.setMaterialCode(materialDtl.getMaterialCode());
            grvMaterialDtl.setMaterialDesc(materialDtl.getMaterialDesc());
            grvMaterialDtl.setReturnType(materialDtl.getReturnType());
            grvMaterialDtl.setRejectReason(materialDtl.getRejectReason());
            grvMaterialDtl.setUomId(materialDtl.getUomId());
            grvMaterialDtl.setGiSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));

            grvMaterialDtlList.add(grvMaterialDtl);
        }

        if (errorFound) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }

        grvMaterialDtlRepository.saveAll(grvMaterialDtlList);

        return "INV" + grvMaster.getGrvProcessId() + "/" + grvMaster.getGrvSubProcessId();
    }

    private void validateGiSubProcessId(String processNo) {
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

    private void validateGprnSubProcessId(String processNo) {
        // processNo that we provide user is of the form INV[po number]/[sub_process_id]
        // fetch just subProcessId
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            System.out.println("EXCEPTION GPRN subprocess: " + processNo);
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer subProcessId = Integer.parseInt(processNoSplit[1]);
        if (!gmr.existsById(subProcessId)) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Provided GPRN No. is not valid."));
        }
    }

    private Map<String, Object> getGiDtlsDto(String processNo) {
        ModelMapper mapper = new ModelMapper();
        // processNo that we provide user is of the form INV[po number]/[inspection_id]
        // fetch just subProcessId
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            System.out.println("EXCEPTION GI: " + processNo);
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer inspectionId = Integer.parseInt(processNoSplit[1]);

        // fetch the GI master entity from inspectionId
        GiMasterEntity gime = gimr.findById(inspectionId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Goods Inspection not found for the provided process ID.")));

        // fetch material details using inspection id
        List<GiMaterialDtlEntity> gimdeList = gimdr.findByInspectionSubProcessId(gime.getInspectionSubProcessId());

        // map the entity to dto
        List<GiMaterialDtlDto> materialDtlListRes = gimdeList.stream()
                .map(gimde -> {
                    GiMaterialDtlDto gmdd = mapper.map(gimde, GiMaterialDtlDto.class);
                    try {
                        String imageBase64 = CommonUtils.convertImageToBase64(gimde.getInstallationReportFileName(),
                                basePath);
                        gmdd.setInstallationReportBase64(imageBase64);
                    } catch (Exception e) {
                        System.out.println("EXCEPTION: " + e.getMessage());
                    }
                    return gmdd;
                })
                .collect(Collectors.toList());

        SaveGiDto giRes = new SaveGiDto();
        giRes.setInspectionNo(gime.getInspectionSubProcessId());
        giRes.setGprnNo(processNo);
        giRes.setInstallationDate(CommonUtils.convertDateToString(gime.getInstallationDate()));
        giRes.setCommissioningDate(CommonUtils.convertDateToString(gime.getCommissioningDate()));
        giRes.setMaterialDtlList(materialDtlListRes);

        SaveGprnDto gprnRes = getGprnDtls(processNo.split("/")[0] + "/" + gime.getGprnSubProcessId());

        Map<String, Object> combinedRes = new HashMap<>();

        combinedRes.put("giDtls", giRes);
        combinedRes.put("gprnDtls", gprnRes);
        return combinedRes;
    }

    private Map<String, Object> getGrvDtls(String processNo) {
        System.out.println("processNo: " + processNo);
        ModelMapper mapper = new ModelMapper();
        String[] processNoSplit = processNo.split("/");
        System.out.println("processNoSplit length: " + processNoSplit.length);
        for (int i = 0; i < processNoSplit.length; i++) {
            System.out.println("processNoSplit[" + i + "]: " + processNoSplit[i]);
        }
        if (processNoSplit.length != 2) {
            System.out.println("INSIDE length: " + processNoSplit.length + " isNotTwo: " + (processNoSplit.length != 2));
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid process ID"));
        }

        Integer grvSubProcessId = Integer.parseInt(processNoSplit[1]);

        GrvMasterEntity grvMaster = grvMasterRepository.findById(grvSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "GRV not found for the provided process ID.")));

        List<GrvMaterialDtlEntity> grvMaterialList = grvMaterialDtlRepository.findByGrvSubProcessId(grvMaster.getGrvSubProcessId());

        List<GrvMaterialDtlDto> materialDtlListRes = grvMaterialList.stream()
                .map(material -> mapper.map(material, GrvMaterialDtlDto.class))
                .collect(Collectors.toList());

        GrvDto grvRes = new GrvDto();
        grvRes.setGrvNo(processNo);
        grvRes.setGiNo("INV" + grvMaster.getGiProcessId() + "/" + grvMaster.getGiSubProcessId());
        grvRes.setDate(CommonUtils.convertDateToString(grvMaster.getDate()));
        grvRes.setCreatedBy(grvMaster.getCreatedBy());
        grvRes.setMaterialDtlList(materialDtlListRes);

        // Get GI details
        Map<String, Object> giDetails = getGiDtlsDto("INV" + grvMaster.getGiProcessId() + "/" + grvMaster.getGiSubProcessId());
        
        Map<String, Object> combinedRes = new HashMap<>();
        combinedRes.put("grvDtls", grvRes);
        combinedRes.put("giDtls", giDetails.get("giDtls"));
        combinedRes.put("gprnDtls", giDetails.get("gprnDtls"));

        return combinedRes;
    }

    private Integer createNewAsset(GiMaterialDtlDto materialDtl) {
        // Check if the asset already exists
        Optional<AssetMasterEntity> ameOpt = amr.findByMaterialCodeAndMaterialDescAndMakeNoAndModelNoAndSerialNoAndUomId(
                materialDtl.getMaterialCode(),
                materialDtl.getMaterialDesc(),
                materialDtl.getMakeNo(),
                materialDtl.getModelNo(),
                materialDtl.getSerialNo(),
                materialDtl.getUomId()
        );

        if(ameOpt.isEmpty()){
            AssetMasterEntity ame = new ModelMapper().map(materialDtl, AssetMasterEntity.class);
            ame.setAssetDesc(materialDtl.getMaterialDesc());
            amr.save(ame);

            return ame.getAssetId();
        }

        return ameOpt.get().getAssetId();
    }

    @Override
    public String saveGrn(GrnDto req) {
        validateGiSubProcessId(req.getGiNo());

        ModelMapper mapper = new ModelMapper();

        // Create GRN Master
        GrnMasterEntity grnMaster = new GrnMasterEntity();
        grnMaster.setGrnDate(CommonUtils.convertStringToDateObject(req.getGrnDate()));
        grnMaster.setInstallationDate(CommonUtils.convertStringToDateObject(req.getInstallationDate()));
        grnMaster.setCommissioningDate(CommonUtils.convertStringToDateObject(req.getCommissioningDate()));
        grnMaster.setCreatedBy(req.getCreatedBy());
        grnMaster.setSystemCreatedBy(req.getSystemCreatedBy());
        grnMaster.setCreateDate(LocalDateTime.now());
        grnMaster.setLocationId(req.getLocationId());
        grnMaster.setGiProcessId(req.getGiNo().split("/")[0].substring(3));
        grnMaster.setGiSubProcessId(Integer.parseInt(req.getGiNo().split("/")[1]));
        grnMaster.setGrnProcessId(req.getGiNo().split("/")[0].substring(3));

        grnMaster = grnmr.save(grnMaster);

        List<GrnMaterialDtlEntity> grnMaterialDtlList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        // Get GI Material Details
        List<GiMaterialDtlEntity> giMaterialList = gimdr.findByInspectionSubProcessId(
        Integer.parseInt(req.getGiNo().split("/")[1]));


        for (GrnMaterialDtlDto materialDtl : req.getMaterialDtlList()) {
            // Find corresponding GI Material Detail
            Optional<GiMaterialDtlEntity> giMaterial = giMaterialList.stream()
                    .filter(gi -> gi.getAssetId().equals(materialDtl.getAssetId()))
                    .findFirst();

            if (giMaterial.isEmpty()) {
                errorMessage.append("Asset ID " + materialDtl.getAssetId() + " not found in GI. ");
                errorFound = true;
                continue;
            }

            // Get previously received quantity for this asset
            BigDecimal previouslyReceivedQty = grnmdr.findByGiSubProcessIdAndAssetId(
                    Integer.parseInt(req.getGiNo().split("/")[1]),
                    materialDtl.getAssetId())
                    .stream()
                    .map(GrnMaterialDtlEntity::getQuantity)  // Changed from getBookValue to getQuantity
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Total received quantity including current
            BigDecimal totalReceivedQty = previouslyReceivedQty.add(materialDtl.getReceivedQuantity());

            // Check if total received quantity exceeds accepted quantity
            if (totalReceivedQty.compareTo(giMaterial.get().getAcceptedQuantity()) > 0) {
                errorMessage.append("Total received quantity for Asset ID " + materialDtl.getAssetId() +
                        " exceeds accepted quantity in GI. ");
                errorFound = true;
                continue;
            }

            GrnMaterialDtlEntity grnMaterialDtl = new GrnMaterialDtlEntity();
            mapper.map(materialDtl, grnMaterialDtl);
            grnMaterialDtl.setGrnProcessId(grnMaster.getGrnProcessId());
            grnMaterialDtl.setGrnSubProcessId(grnMaster.getGrnSubProcessId());
            grnMaterialDtl.setGiSubProcessId(grnMaster.getGiSubProcessId());

            grnMaterialDtlList.add(grnMaterialDtl);

            // Update asset master init quantity if it's 0
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

            // Create or update OHQ entry
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

    private Map<String, Object> getGrnDtls(String processNo) {
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

        // Fetch GRN master
        GrnMasterEntity grnMaster = grnmr.findById(grnSubProcessId)
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "GRN not found for the provided process ID.")));

        // Fetch GRN material details
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

        // Get GI details which includes GPRN details
        Map<String, Object> giDetails = getGiDtlsDto("INV" + grnMaster.getGiProcessId() + "/" + grnMaster.getGiSubProcessId());
        
        Map<String, Object> combinedRes = new HashMap<>();
        combinedRes.put("grnDtls", grnRes);
        combinedRes.put("giDtls", giDetails.get("giDtls"));
        combinedRes.put("gprnDtls", giDetails.get("gprnDtls"));

        return combinedRes;
    }
}