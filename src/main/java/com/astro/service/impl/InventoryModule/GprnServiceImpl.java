package com.astro.service.impl.InventoryModule;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.astro.service.InventoryModule.GprnService;
import com.astro.repository.InventoryModule.GprnRepository.GprnMasterRepository;
import com.astro.repository.InventoryModule.GprnRepository.GprnMaterialDtlRepository;
import com.astro.repository.VendorMasterRepository;
import com.astro.entity.InventoryModule.GprnMasterEntity;
import com.astro.entity.InventoryModule.GprnMaterialDtlEntity;
import com.astro.entity.VendorMaster;
import com.astro.dto.workflow.InventoryModule.GprnDto.*;
import com.astro.exception.*;
import com.astro.constant.AppConstant;
import com.astro.util.CommonUtils;
import org.modelmapper.ModelMapper;

@Service
public class GprnServiceImpl implements GprnService {
    @Value("${filePath}")
    private String bp;
    
    @Autowired
    private GprnMasterRepository gmr;
    
    @Autowired
    private GprnMaterialDtlRepository gmdr;
    
    @Autowired
    private VendorMasterRepository vmr;

    private final String basePath;

    public GprnServiceImpl(@Value("${filePath}") String bp) {
        this.basePath = bp + "/INV";
    }

    @Override
    @Transactional
    public String saveGprn(SaveGprnDto req) {
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
        gme.setCreatedBy(req.getCreatedBy());

        gme = gmr.save(gme);

        List<GprnMaterialDtlEntity> saveDtlEntityList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        for (MaterialDtlDto dtl : req.getMaterialDtlList()) {
            List<GprnMaterialDtlEntity> gmdeList = gmdr.findByPoIdAndMaterialCode(req.getPoId(), dtl.getMaterialCode());

            BigDecimal prevRecQuant = gmdeList.stream()
                .map(GprnMaterialDtlEntity::getReceivedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalRecQuant = prevRecQuant.add(dtl.getReceivedQuantity());
            if (totalRecQuant.compareTo(dtl.getOrderedQuantity()) > 0) {
                errorMessage.append("Total received quantity for " + dtl.getMaterialCode() + " is more than ordered quantity.");
                errorFound = true;
                continue;
            }

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
        return "INV" + gme.getProcessId() + "/" + gme.getSubProcessId();
    }

    @Override
    public SaveGprnDto getGprnDtls(String processNo) {
        ModelMapper mapper = new ModelMapper();
        String[] processNoSplit = processNo.split("/");
        
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process ID"));
        }

        Integer subProcessId = Integer.parseInt(processNoSplit[1]);
        GprnMasterEntity gme = gmr.findById(subProcessId)
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Process not found for the provided process ID.")));

        List<GprnMaterialDtlEntity> gmdeList = gmdr.findBySubProcessId(gme.getSubProcessId());
        List<MaterialDtlDto> materialDtlListRes = gmdeList.stream()
            .map(gmde -> {
                MaterialDtlDto mdd = mapper.map(gmde, MaterialDtlDto.class);
                try {
                    String imageBase64 = CommonUtils.convertImageToBase64(gmde.getFileName(), basePath);
                    mdd.setImageBase64(imageBase64);
                } catch (Exception e) {
                    // Log exception
                }
                return mdd;
            })
            .collect(Collectors.toList());

        VendorMaster vm = vmr.findById(gme.getVendorId())
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Vendor not found for the provided vendor ID.")));

        SaveGprnDto gprnRes = mapper.map(gme, SaveGprnDto.class);
        gprnRes.setProcessId(processNo);
        gprnRes.setVendorEmail(vm.getEmailAddress());
        gprnRes.setVendorName(vm.getVendorName());
        gprnRes.setVendorContact(vm.getContactNo());
        gprnRes.setDate(CommonUtils.convertDateToString(gme.getDate()));
        gprnRes.setSupplyExpectedDate(CommonUtils.convertDateToString(gme.getSupplyExpectedDate()));
        gprnRes.setDeliveryDate(CommonUtils.convertDateToString(gme.getDeliveryDate()));
        gprnRes.setMaterialDtlList(materialDtlListRes);

        return gprnRes;
    }

    @Override
    public List<String> getPendingGprn() {
        List<String> pendingGprnList = gmr.findPoIdsWithIncompleteGprn();
        return pendingGprnList;
    }

    @Override
    public void validateGprnSubProcessId(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
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
}