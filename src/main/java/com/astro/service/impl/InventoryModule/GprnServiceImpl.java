package com.astro.service.impl.InventoryModule;

import com.astro.entity.ProcurementModule.PurchaseOrderAttributes;
import com.astro.repository.ProcurementModule.PurchaseOrder.PurchaseOrderAttributesRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
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
    @Autowired
    private PurchaseOrderAttributesRepository poMaterialRepo;

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
        gme.setStatus("AWAITING APPROVAL");

        gme = gmr.save(gme);

        List<GprnMaterialDtlEntity> saveDtlEntityList = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        Boolean errorFound = false;

        for (MaterialDtlDto dtl : req.getMaterialDtlList()) {
            List<GprnMaterialDtlEntity> gmdeList = gmdr.findByPoIdAndMaterialCode(req.getPoId(), dtl.getMaterialCode());
    
            // BigDecimal prevRecQuant = gmdeList.stream()
            //     .map(GprnMaterialDtlEntity::getReceivedQuantity)
            //     .reduce(BigDecimal.ZERO, BigDecimal::add);
            // System.out.println("PREV REC QUANT: " + prevRecQuant);
            // System.out.println("NEW WUANT: " + dtl.getReceivedQuantity());
    
            // BigDecimal totalRecQuant = prevRecQuant.add(dtl.getReceivedQuantity());
            if (dtl.getOrderedQuantity().compareTo(dtl.getReceivedQuantity()) < 0) {
                errorMessage.append("Received quantity for " + dtl.getMaterialCode() + " is more than ordered quantity.");
                errorFound = true;
                continue;
            }
    
            GprnMaterialDtlEntity gmde = new GprnMaterialDtlEntity();
            mapper.map(dtl, gmde);
            gmde.setProcessId(gme.getProcessId());
            gmde.setPoId(gme.getPoId());
            gmde.setSubProcessId(gme.getSubProcessId());
            
            try {
                List<String> imageFileNames = new ArrayList<>();
                for (String base64Image : dtl.getImageBase64()) {
                    String imageFileName = CommonUtils.saveBase64Image(base64Image, basePath);
                    imageFileNames.add(imageFileName);
                }
                gmde.setFileName(String.join(",", imageFileNames));
            } catch (Exception e) {
                throw new InvalidInputException(new ErrorDetails(
                    AppConstant.FILE_UPLOAD_ERROR,
                    AppConstant.USER_INVALID_INPUT,
                    AppConstant.ERROR_TYPE_CORRUPTED,
                    "Error while uploading images."));
            }
    
            saveDtlEntityList.add(gmde);
            Optional<PurchaseOrderAttributes> optionalPoMaterial = poMaterialRepo.findByPoIdAndMaterialCode(gme.getPoId(), dtl.getMaterialCode());

            if (optionalPoMaterial.isPresent()) {
                PurchaseOrderAttributes poMaterial = optionalPoMaterial.get();
                BigDecimal updatedReceivedQty = poMaterial.getReceivedQuantity().add(dtl.getReceivedQuantity());
                poMaterial.setReceivedQuantity(updatedReceivedQty);
                poMaterialRepo.save(poMaterial);
            }

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
                    List<String> imageBase64List = new ArrayList<>();
                    String[] fileNames = gmde.getFileName().split(",");
                    for (String fileName : fileNames) {
                        String imageBase64 = CommonUtils.convertImageToBase64(fileName.trim(), basePath);
                        imageBase64List.add(imageBase64);
                    }
                    mdd.setImageBase64(imageBase64List);
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
        GprnMasterEntity gprnMaster = gmr.findById(subProcessId)
            .orElseThrow(() -> new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Provided GPRN No. is not valid.")));

        if (!"APPROVED".equals(gprnMaster.getStatus())) {
            throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "GPRN must be approved before proceeding."));
        }
    }


    @Override
    @Transactional
    public void rejectGprn(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer inspectionId = Integer.parseInt(processNoSplit[1]);
        
        GprnMasterEntity giMaster = gmr.findById(inspectionId)
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Goods Inspection not found")));

        giMaster.setStatus("REJECTED");
        gmr.save(giMaster);
    }

    @Override
    @Transactional
    public void approveGprn(String processNo) {
        String[] processNoSplit = processNo.split("/");
        if (processNoSplit.length != 2) {
            throw new InvalidInputException(new ErrorDetails(
                AppConstant.USER_INVALID_INPUT,
                AppConstant.ERROR_TYPE_CODE_VALIDATION,
                AppConstant.ERROR_TYPE_VALIDATION,
                "Invalid process number format"));
        }

        Integer inspectionId = Integer.parseInt(processNoSplit[1]);
        
        GprnMasterEntity giMaster = gmr.findById(inspectionId)
            .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Goods Inspection not found")));

        giMaster.setStatus("APPROVED");
        gmr.save(giMaster);
    }
}