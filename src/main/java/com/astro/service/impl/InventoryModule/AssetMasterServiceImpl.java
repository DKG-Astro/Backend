package com.astro.service.impl.InventoryModule;

import com.astro.dto.workflow.InventoryModule.*;
import com.astro.dto.workflow.InventoryModule.asset.AssetMasterReportDto;
import com.astro.dto.workflow.InventoryModule.asset.AssetOhqDisposalDto;
import com.astro.entity.InventoryModule.*;
import com.astro.repository.InventoryModule.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.internal.metadata.aggregated.rule.OverridingMethodMustNotAlterParameterConstraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import com.astro.repository.ohq.OhqMasterRepository;
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

    @Autowired
    private OhqMasterConsumableRepository ohqMasterConsumableRepository;
    @Autowired
    private OhqConsumableStoreStockRepository ohqStoreStockRepo;

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
            existingAsset.setEndOfLife(CommonUtils.convertIsoDateStringToDateObject(request.getEndOfLife()));
        }else{
            existingAsset.setEndOfLife(null);
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
      //  disposalMaster.setVendorId(request.getVendorId());
        disposalMaster.setCustodianId(request.getCustodianId());
        disposalMaster.setStatus("For Disposal");
        disposalMaster.setAction("Awaiting For Approval");
        disposalMaster = disposalMasterRepository.save(disposalMaster);
        
        List<AssetDisposalDetailEntity> disposalDetails = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        boolean errorFound = false;

        // Process each disposal detail
        for (AssetDisposalDetailDto detailDto : request.getMaterialDtlList()) {
            // Validate OHQ stock
            OhqMasterEntity ohq = ohqMasterRepository
                    .findByAssetIdAndLocatorIdAndCustodianId(detailDto.getAssetId(), detailDto.getLocatorId(), detailDto.getCustodianId())
                    .orElseThrow(() -> new BusinessException(new ErrorDetails(
                            AppConstant.ERROR_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_RESOURCE,
                            "No stock found for asset ID: " + detailDto.getAssetId() + 
                            " at locator: " + detailDto.getLocatorId())));

            BigDecimal remainingQuantity = ohq.getQuantity().subtract(detailDto.getQuantity());
            
            if (remainingQuantity.compareTo(BigDecimal.ZERO) < 0) {
                errorMessage.append("Insufficient quantity for asset ID: ")
                        .append(detailDto.getAssetId())
                        .append(". Available: ")
                        .append(ohq.getQuantity())
                        .append(", Requested: ")
                        .append(detailDto.getQuantity())
                        .append(". ");
                errorFound = true;
                continue;
            }

            // Create disposal detail
            AssetDisposalDetailEntity detail = new AssetDisposalDetailEntity();
            detail.setDisposalId(disposalMaster.getDisposalId());
            detail.setAssetId(detailDto.getAssetId());
            detail.setAssetDesc(detailDto.getAssetDesc());
            detail.setDisposalQuantity(detailDto.getQuantity());
            detail.setDisposalCategory(detailDto.getDisposalCategory());
            detail.setDisposalMode(detailDto.getDisposalMode());

            detail.setOhqId(detailDto.getOhqId());
            detail.setLocatorId(detailDto.getLocatorId());
            detail.setBookValue(detailDto.getBookValue());
            detail.setDepriciationRate(detailDto.getDepriciationRate());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setCustodianId(detailDto.getCustodianId());
            detail.setPoValue(detailDto.getPoValue());
            detail.setReasonForDisposal(detailDto.getReasonForDisposal());
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
        
        return "INV/" + disposalMaster.getDisposalId().toString();
    }
    @Override
    public List<AssetDisposalDto> getAllAssetDisposalAwaitingForApproval() {
        List<AssetDisposalMasterEntity> masters = disposalMasterRepository.findAllAwaitingForApproval();
        List<AssetDisposalDto> result = new ArrayList<>();

        for (AssetDisposalMasterEntity master : masters) {
            AssetDisposalDto dto = new AssetDisposalDto();
            dto.setDisposalId(master.getDisposalId());
            dto.setDisposalDate(master.getDisposalDate() != null ? master.getDisposalDate().toString() : null);
            dto.setCreatedBy(master.getCreatedBy());
            dto.setLocationId(master.getLocationId());
            dto.setCustodianId(master.getCustodianId());

            List<AssetDisposalDetailEntity> details = disposalDetailRepository.findByDisposalId(master.getDisposalId());
            List<AssetDisposalDetailDto> detailDtos = new ArrayList<>();

            for (AssetDisposalDetailEntity detail : details) {
                AssetDisposalDetailDto dDto = new AssetDisposalDetailDto();
                dDto.setAssetId(detail.getAssetId());
                dDto.setAssetDesc(detail.getAssetDesc());
                dDto.setQuantity(detail.getDisposalQuantity());
                dDto.setDisposalCategory(detail.getDisposalCategory());
                dDto.setDisposalMode(detail.getDisposalMode());
                dDto.setSalesNoteFilename(detail.getSalesNoteFilename());
                dDto.setLocatorId(detail.getLocatorId());
                dDto.setOhqId(detail.getOhqId());
                dDto.setBookValue(detail.getBookValue());
                dDto.setDepriciationRate(detail.getDepriciationRate());
                dDto.setUnitPrice(detail.getUnitPrice());
                dDto.setCustodianId(detail.getCustodianId());
                dDto.setPoValue(detail.getPoValue());
                dDto.setReasonForDisposal(detail.getReasonForDisposal());
                detailDtos.add(dDto);
            }

            dto.setMaterialDtlList(detailDtos);
            result.add(dto);
        }

        return result;
    }
    @Override
    public List<AssetDisposalDto> getAllApprovedAssetDisposalReport() {
        List<AssetDisposalMasterEntity> masters = disposalMasterRepository.findAllApprovedAssetDisposals();
        List<AssetDisposalDto> result = new ArrayList<>();

        for (AssetDisposalMasterEntity master : masters) {
            AssetDisposalDto dto = new AssetDisposalDto();
            dto.setDisposalId(master.getDisposalId());
            dto.setDisposalDate(master.getDisposalDate() != null ? master.getDisposalDate().toString() : null);
            dto.setCreatedBy(master.getCreatedBy());
            dto.setLocationId(master.getLocationId());
            dto.setCustodianId(master.getCustodianId());

            dto.setStatus(master.getStatus());
            dto.setAction(master.getAction());
            List<AssetDisposalDetailEntity> details = disposalDetailRepository.findByDisposalId(master.getDisposalId());
            List<AssetDisposalDetailDto> detailDtos = new ArrayList<>();

            for (AssetDisposalDetailEntity detail : details) {
                AssetDisposalDetailDto dDto = new AssetDisposalDetailDto();
                dDto.setAssetId(detail.getAssetId());
                dDto.setAssetDesc(detail.getAssetDesc());
                dDto.setQuantity(detail.getDisposalQuantity());
                dDto.setDisposalCategory(detail.getDisposalCategory());
                dDto.setDisposalMode(detail.getDisposalMode());
                dDto.setSalesNoteFilename(detail.getSalesNoteFilename());
                dDto.setLocatorId(detail.getLocatorId());
                dDto.setOhqId(detail.getOhqId());
                dDto.setBookValue(detail.getBookValue());
                dDto.setDepriciationRate(detail.getDepriciationRate());
                dDto.setUnitPrice(detail.getUnitPrice());
                dDto.setCustodianId(detail.getCustodianId());
                dDto.setPoValue(detail.getPoValue());
                dDto.setReasonForDisposal(detail.getReasonForDisposal());
                detailDtos.add(dDto);
            }

            dto.setMaterialDtlList(detailDtos);
            result.add(dto);
        }

        return result;
    }
    @Transactional
    public void approveDisposal(String disposalIdStr) {
      //  Integer disposalId = Integer.valueOf(disposalIdStr.split("/")[1]); // Extract ID from string if needed
        Integer disposalId = Integer.valueOf(disposalIdStr);
        AssetDisposalMasterEntity disposalMaster = disposalMasterRepository.findById(disposalId)
                .orElseThrow(() -> new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Asset Disposal not found for ID: " + disposalId)));
        disposalMaster.setAction("Approved");
        disposalMasterRepository.save(disposalMaster);
    }

    @Transactional
    public void rejectDisposal(String disposalIdStr) {
      //  Integer disposalId = Integer.valueOf(disposalIdStr.split("/")[1]);
        Integer disposalId = Integer.valueOf(disposalIdStr);
        AssetDisposalMasterEntity disposalMaster = disposalMasterRepository.findById(disposalId)
                .orElseThrow(() -> new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Asset Disposal not found for ID: " + disposalId)));

        List<AssetDisposalDetailEntity> disposalDetails = disposalDetailRepository.findByDisposalId(disposalId);

        for (AssetDisposalDetailEntity detail : disposalDetails) {
            // Find OHQ by ohqId, locatorId, and custodianId
            OhqMasterEntity ohq = ohqMasterRepository
                    .findByAssetIdAndLocatorIdAndCustodianId(detail.getAssetId(), detail.getLocatorId(), detail.getCustodianId())
                    .orElseThrow(() -> new BusinessException(new ErrorDetails(
                            AppConstant.ERROR_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_RESOURCE,
                            "No stock found for asset ID: " + detail.getAssetId() +
                                    " at locator: " + detail.getLocatorId())));


            // Add back quantity
            ohq.setQuantity(ohq.getQuantity().add(detail.getDisposalQuantity()));
            ohqMasterRepository.save(ohq);
        }
        disposalMaster.setAction("Rejected");
        disposalMasterRepository.save(disposalMaster);
    }

    public AssetDisposalDto getAssetDisposalById(String disposalIdStr) {
        // Extract numeric ID from "INV12"
        Integer disposalId = Integer.valueOf(disposalIdStr.split("/")[1]);

        AssetDisposalMasterEntity master = disposalMasterRepository.findById(disposalId)
                .orElseThrow(() -> new RuntimeException("Asset Disposal not found: " + disposalIdStr));

        List<AssetDisposalDetailEntity> details = disposalDetailRepository.findByDisposalId(disposalId);

        AssetDisposalDto dto = new AssetDisposalDto();
        dto.setDisposalId(master.getDisposalId());
       // dto.setDisposalDate(master.getDisposalDate() != null ? master.getDisposalDate().toString() : null);
        dto.setDisposalDate(CommonUtils.convertDateToString(master.getDisposalDate()));
        dto.setLocationId(master.getLocationId());
        dto.setCustodianId(master.getCustodianId());
        dto.setCreatedBy(master.getCreatedBy());
        dto.setAction(master.getAction());
        dto.setStatus(master.getStatus());
        dto.setAuctionId(master.getAuctionId());

        dto.setAuctionDate(CommonUtils.convertDateToString(master.getAuctionDate()));
        dto.setAuctionPrice(master.getAuctionPrice());
        dto.setReservePrice(master.getReservePrice());
        dto.setVendorName(master.getVendorName());


        dto.setMaterialDtlList(details.stream().map(this::mapToDetailDto).collect(Collectors.toList()));

        return dto;
    }

    private AssetDisposalDetailDto mapToDetailDto(AssetDisposalDetailEntity detail) {
        AssetDisposalDetailDto dto = new AssetDisposalDetailDto();
        dto.setAssetId(detail.getAssetId());
        dto.setAssetDesc(detail.getAssetDesc());
        dto.setQuantity(detail.getDisposalQuantity());
        dto.setDisposalCategory(detail.getDisposalCategory());
        dto.setDisposalMode(detail.getDisposalMode());
        dto.setSalesNoteFilename(detail.getSalesNoteFilename());
        dto.setLocatorId(detail.getLocatorId());
        dto.setOhqId(detail.getOhqId());
        dto.setBookValue(detail.getBookValue());
        dto.setDepriciationRate(detail.getDepriciationRate());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setCustodianId(detail.getCustodianId());
        dto.setPoValue(detail.getPoValue());
        dto.setReasonForDisposal(detail.getReasonForDisposal());
        return dto;
    }

    @Override
    @Transactional
    public String updateAssetDisposal(AssetDisposalDto request) {
      //  Integer disposalId = Integer.valueOf(request.getDisposalId().split("/")[1]);
        AssetDisposalMasterEntity disposalMaster = disposalMasterRepository
                .findById(Integer.valueOf(request.getDisposalId()))
                .orElseThrow(() -> new RuntimeException("Disposal not found: " + request.getDisposalId()));

        String status = request.getStatus();

        if ("Disposed".equalsIgnoreCase(status)) {
            // Update disposal master fields
            disposalMaster.setStatus("Disposed");
            disposalMaster.setAuctionId(request.getAuctionId());
            if (request.getAuctionDate() != null && !request.getAuctionDate().trim().isEmpty()) {
                disposalMaster.setAuctionDate(CommonUtils.convertStringToDateObject(request.getAuctionDate()));
            }
          //  disposalMaster.setAuctionDate(request.getAuctionDate());
            disposalMaster.setReservePrice(request.getReservePrice());
            disposalMaster.setAuctionPrice(request.getAuctionPrice());
            disposalMaster.setVendorName(request.getVendorName());

            disposalMasterRepository.save(disposalMaster);

            // No OHQ update needed for Disposed
        }
        else if ("Removal of Disposal".equalsIgnoreCase(status)) {
            // Revert stock in OHQ
            List<AssetDisposalDetailEntity> disposalDetails = disposalDetailRepository
                    .findByDisposalId(disposalMaster.getDisposalId());

            for (AssetDisposalDetailEntity detail : disposalDetails) {
                // Find OHQ by ohqId, locatorId, and custodianId
                OhqMasterEntity ohq = ohqMasterRepository
                        .findByAssetIdAndLocatorIdAndCustodianId(detail.getAssetId(), detail.getLocatorId(), detail.getCustodianId())
                        .orElseThrow(() -> new BusinessException(new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "No stock found for asset ID: " + detail.getAssetId() +
                                        " at locator: " + detail.getLocatorId())));


                // Add back quantity
                ohq.setQuantity(ohq.getQuantity().add(detail.getDisposalQuantity()));
                ohqMasterRepository.save(ohq);
            }

            // Update disposal status
            disposalMaster.setStatus("Removed");
            disposalMasterRepository.save(disposalMaster);
        }
        else {
            throw new RuntimeException("Invalid status: " + status);
        }

        return "INV/" + disposalMaster.getDisposalId();
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
public List<AssetMasterReportDto> getAssetReport() {
    List<Object[]> results = assetMasterRepository.getAssetReport();
    
    return results.stream().map(row -> {
        AssetMasterReportDto dto = new AssetMasterReportDto();
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
        dto.setPoId((String) row[21]); // po_id
        dto.setPoValue((BigDecimal) row[22]); // total_value_of_po
        dto.setVendorId((String) row[23]);
        return dto;
    }).collect(Collectors.toList());
}

@Override
public List<Integer> getAllAssetIds() {
    return assetMasterRepository.findAllAssetIds();
}

@Override
public List<OhqMasterEntity> getAssetOhqList() {
    return ohqMasterRepository.findAll();
}

@Override
public List<OhqMasterConsumableEntity> getAssetOhqConsumableList() {
    return ohqMasterConsumableRepository.findAll();
}
@Override
public List<OhqConsumableStoreStockEntity> getStoreStockOhqConsumableList(){
        return ohqStoreStockRepo.findAll();
}
   @Override
   public List<AssetOhqDisposalDto> getAllAssetsForDisposal() {
       List<Object[]> rows = ohqMasterRepository.getAllAssetOhqDisposalsNative();
       List<AssetOhqDisposalDto> dtos = new ArrayList<>();

       for (Object[] r : rows) {
           AssetOhqDisposalDto dto = new AssetOhqDisposalDto();
           dto.setOhqId((Integer) r[0]);
           dto.setAssetId((Integer) r[1]);
           dto.setAseetDescription((String) r[2]);
           dto.setLocatorId((Integer) r[3]);
           dto.setBookValue((BigDecimal) r[4]);
           dto.setDepriciationRate((BigDecimal) r[5]);
           dto.setUnitPrice((BigDecimal) r[6]);
           dto.setQuantity((BigDecimal) r[7]);
           dto.setCustodianId((String) r[8]);
           dto.setPoValue((BigDecimal) r[9]);
           dtos.add(dto);
       }

       return dtos;
   }

    @Override
    public List<AssetDisposalReportDto> getAssetDisposalReport(String startDate, String endDate) {
        List<LocalDateTime> range = CommonUtils.getDateRenge(startDate, endDate);
        LocalDateTime start = range.get(0);
        LocalDateTime end = range.get(1);

        List<Object[]> rows = disposalMasterRepository.getDisposedAssetDisposalReport(start, end);
        ObjectMapper mapper = new ObjectMapper();
        List<AssetDisposalReportDto> reports = new ArrayList<>();

        for (Object[] row : rows) {
            AssetDisposalReportDto dto = new AssetDisposalReportDto();
            dto.setId(((Number) row[0]).longValue());
            dto.setLocationId((String) row[1]);
            dto.setStatus((String) row[2]);
            dto.setCustodianId(row[3] != null ? Integer.valueOf(row[3].toString()) : null);
            dto.setCreateDate(((Timestamp) row[4]).toLocalDateTime());
            dto.setDisposalDate(row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null);
            dto.setCreatedBy(row[6] != null ? Integer.valueOf(row[6].toString()) : null);
            dto.setAction(row[7] != null ? row[7].toString() : null);
            dto.setAuctionId(row[8] != null ? row[8].toString() : null);
            dto.setAuctionDate(row[9] != null ? ((Date) row[9]).toLocalDate() : null);
            dto.setReservePrice(row[10] != null ? new BigDecimal(row[10].toString()) : null);
            dto.setAuctionPrice(row[11] != null ? new BigDecimal(row[11].toString()) : null);
            dto.setVendorName(row[12] != null ? row[12].toString() : null);
            try {
                String materialsJson = (String) row[13];
                if (materialsJson != null && !materialsJson.isEmpty()) {
                    List<AssetDisposalMaterialDto> materials = mapper.readValue(
                            materialsJson, new TypeReference<List<AssetDisposalMaterialDto>>() {}
                    );
                    dto.setMaterialDtos(materials);
                } else {
                    dto.setMaterialDtos(new ArrayList<>());
                }
            } catch (Exception e) {
                dto.setMaterialDtos(new ArrayList<>());
            }
            reports.add(dto);
        }

        return reports;
    }

}