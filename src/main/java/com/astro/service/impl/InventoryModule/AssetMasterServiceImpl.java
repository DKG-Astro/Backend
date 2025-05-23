package com.astro.service.impl.InventoryModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.astro.util.CommonUtils;

import javax.transaction.Transactional;

import com.astro.service.InventoryModule.AssetMasterService;
import com.astro.service.impl.AssetServiceImpl;
import com.astro.util.CommonUtils;
import com.astro.repository.InventoryModule.AssetDisposalDetailRepository;
import com.astro.repository.InventoryModule.AssetDisposalMasterRepository;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.repository.ohq.OhqMasterRepository;
import com.astro.entity.InventoryModule.AssetDisposalDetailEntity;
import com.astro.entity.InventoryModule.AssetDisposalMasterEntity;
import com.astro.entity.InventoryModule.AssetMasterEntity;
import com.astro.entity.InventoryModule.OhqMasterEntity;
import com.astro.dto.workflow.InventoryModule.AssetDisposalDetailDto;
import com.astro.dto.workflow.InventoryModule.AssetDisposalDto;
import com.astro.dto.workflow.InventoryModule.AssetMasterDto;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.constant.AppConstant;

@Service
public class AssetMasterServiceImpl implements AssetMasterService {

    @Value("${filePath}")
    private String bp;

    @Autowired
    private AssetMasterRepository assetMasterRepository;    
    
    @Autowired
    private AssetDisposalMasterRepository disposalMasterRepository;
    
    @Autowired
    private AssetDisposalDetailRepository disposalDetailRepository;
    
    @Autowired
    private OhqMasterRepository ohqMasterRepository;

    private final String basePath;

    public AssetMasterServiceImpl(@Value("${filePath}") String bp) {
        this.basePath = bp + "/INV";
    }

    @Override
    @Transactional
    public String saveAssetMaster(AssetMasterDto request) {
        // Validate duplicate asset
        if (assetMasterRepository.existsByMaterialCodeAndMaterialDescAndMakeNoAndModelNoAndSerialNoAndUomId(
                request.getMaterialCode(),
                request.getMaterialDesc(),
                request.getMakeNo(),
                request.getModelNo(),
                request.getSerialNo(),
                request.getUomId())) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Asset already exists with the provided details"));
        }

        ModelMapper mapper = new ModelMapper();
        AssetMasterEntity asset = mapper.map(request, AssetMasterEntity.class);
        
        // Convert end of life string to LocalDate
        if (request.getEndOfLife() != null && !request.getEndOfLife().trim().isEmpty()) {
            asset.setEndOfLife(CommonUtils.convertStringToDateObject(request.getEndOfLife()));
        }
        
        asset.setCreateDate(LocalDateTime.now());
        asset.setUpdatedDate(LocalDateTime.now());

        asset = assetMasterRepository.save(asset);
        return asset.getAssetId().toString();
    }

    @Override
    @Transactional
    public String updateAssetMaster(AssetMasterDto request) {
        if (request.getAssetId() == null) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Asset ID is required for update"));
        }

        AssetMasterEntity existingAsset = assetMasterRepository.findById(request.getAssetId())
                .orElseThrow(() -> new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Asset not found with ID: " + request.getAssetId())));

        ModelMapper mapper = new ModelMapper();
        mapper.map(request, existingAsset);
        
        // Convert end of life string to LocalDate
        if (request.getEndOfLife() != null && !request.getEndOfLife().trim().isEmpty()) {
            existingAsset.setEndOfLife(CommonUtils.convertStringToDateObject(request.getEndOfLife()));
        }
        
        existingAsset.setUpdatedDate(LocalDateTime.now());
        existingAsset.setDepriciationRate(request.getDepriciationRate());
        
        assetMasterRepository.save(existingAsset);
        return existingAsset.getAssetId().toString();
    }

    @Override
    @Transactional
    public String saveAssetDisposal(AssetDisposalDto request) {
        AssetDisposalMasterEntity disposalMaster = new AssetDisposalMasterEntity();
        
        // Convert string date to LocalDate
        if (request.getDisposalDate() != null && !request.getDisposalDate().trim().isEmpty()) {
            disposalMaster.setDisposalDate(CommonUtils.convertStringToDateObject(request.getDisposalDate()));
        }
        
        disposalMaster.setCreatedBy(request.getCreatedBy());
        disposalMaster.setCreateDate(LocalDateTime.now());
        disposalMaster.setLocationId(request.getLocationId());
        disposalMaster.setVendorId(request.getVendorId());
        
        disposalMaster = disposalMasterRepository.save(disposalMaster);
        
        List<AssetDisposalDetailEntity> disposalDetails = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        boolean errorFound = false;

        // Process each disposal detail
        for (AssetDisposalDetailDto detailDto : request.getMaterialDtlList()) {
            // Validate OHQ stock
            OhqMasterEntity ohq = ohqMasterRepository
                    .findByAssetIdAndLocatorId(detailDto.getAssetId(), detailDto.getLocatorId())
                    .orElseThrow(() -> new BusinessException(new ErrorDetails(
                            AppConstant.ERROR_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_RESOURCE,
                            "No stock found for asset ID: " + detailDto.getAssetId() + 
                            " at locator: " + detailDto.getLocatorId())));

            BigDecimal remainingQuantity = ohq.getQuantity().subtract(detailDto.getDisposalQuantity());
            
            if (remainingQuantity.compareTo(BigDecimal.ZERO) < 0) {
                errorMessage.append("Insufficient quantity for asset ID: ")
                        .append(detailDto.getAssetId())
                        .append(". Available: ")
                        .append(ohq.getQuantity())
                        .append(", Requested: ")
                        .append(detailDto.getDisposalQuantity())
                        .append(". ");
                errorFound = true;
                continue;
            }

            // Create disposal detail
            AssetDisposalDetailEntity detail = new AssetDisposalDetailEntity();
            detail.setDisposalId(disposalMaster.getDisposalId());
            detail.setAssetId(detailDto.getAssetId());
            detail.setAssetDesc(detailDto.getAssetDesc());
            detail.setDisposalQuantity(detailDto.getDisposalQuantity());
            detail.setDisposalCategory(detailDto.getDisposalCategory());
            detail.setDisposalMode(detailDto.getDisposalMode());

            if(Objects.nonNull(detailDto.getSalesNoteFilename())){

                try {
                    String  file = CommonUtils.saveBase64Image(detailDto.getSalesNoteFilename(), basePath);
                    detailDto.setSalesNoteFilename(file);
                } catch (Exception e) {
                    throw new BusinessException(new ErrorDetails(
                        AppConstant.FILE_UPLOAD_ERROR,
                        AppConstant.USER_INVALID_INPUT,
                        AppConstant.ERROR_TYPE_CORRUPTED,
                        "Error while uploading image."));
                }

                 // Update the DTO with the file path
            }
            
            disposalDetails.add(detail);

            // Update OHQ
            ohq.setQuantity(remainingQuantity);
            ohqMasterRepository.save(ohq);
        }

        if (errorFound) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    errorMessage.toString()));
        }

        disposalDetailRepository.saveAll(disposalDetails);
        
        return "INV" + disposalMaster.getDisposalId().toString();
    }

    @Override
    public AssetMasterDto getAssetDetails(Integer assetId) {
        System.out.println("CALLED");
        AssetMasterEntity asset = assetMasterRepository.findById(assetId)
                .orElseThrow(() -> new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Asset not found with ID: " + assetId)));

        // System.out.println("ASEET" + asset);

        AssetMasterDto response = new AssetMasterDto();
        response.setAssetId(asset.getAssetId());
        response.setMaterialCode(asset.getMaterialCode());
        response.setMaterialDesc(asset.getMaterialDesc());
        response.setAssetDesc(asset.getAssetDesc());
        response.setMakeNo(asset.getMakeNo());
        response.setModelNo(asset.getModelNo());
        response.setSerialNo(asset.getSerialNo());
        response.setUomId(asset.getUomId());
        response.setComponentName(asset.getComponentName());
        response.setComponentId(asset.getComponentId());
        response.setInitQuantity(asset.getInitQuantity());
        response.setUnitPrice(asset.getUnitPrice());
        response.setStockLevels(asset.getStockLevels());
        response.setConditionOfGoods(asset.getConditionOfGoods());
        response.setShelfLife(asset.getShelfLife());
        response.setLocatorId(asset.getLocatorId());
        
        if (asset.getEndOfLife() != null) {
            response.setEndOfLife(asset.getEndOfLife().toString());
        }
        
        return response;
    }

    @Override
public List<AssetMasterDto> getAssetReport() {
    List<Object[]> results = assetMasterRepository.getAssetReport();
    
    return results.stream().map(row -> {
        AssetMasterDto dto = new AssetMasterDto();
        dto.setAssetId((Integer) row[0]);
        dto.setMaterialCode((String) row[1]);
        dto.setMaterialDesc((String) row[2]);
        dto.setAssetDesc((String) row[3]);
        dto.setMakeNo((String) row[4]);
        dto.setSerialNo((String) row[5]);
        dto.setModelNo((String) row[6]);
        dto.setInitQuantity((BigDecimal) row[7]);
        dto.setUnitPrice((BigDecimal) row[8]);
        dto.setUomId((String) row[9]);
        dto.setDepriciationRate((BigDecimal) row[10]);
        CommonUtils.convertSqlDateToString((Date) row[11]);
        // dto.setEndOfLife(row[11] != null ? ((Date) row[11]).toLocalDate() : null);
        dto.setStockLevels((BigDecimal) row[12]);
        dto.setConditionOfGoods((String) row[13]);
        dto.setShelfLife((String) row[14]);
        dto.setComponentName((String) row[15]);
        dto.setComponentId((Integer) row[16]);
        // dto.setCreateDate(((Timestamp) row[17]).toLocalDateTime());
        dto.setCreatedBy((Integer) row[18]);
        // dto.setUpdatedDate(((Timestamp) row[19]).toLocalDateTime());
        dto.setUpdatedBy((Integer) row[20]);
        return dto;
    }).collect(Collectors.toList());
}

@Override
public List<Integer> getAllAssetIds() {
    return assetMasterRepository.findAllAssetIds();
}
}