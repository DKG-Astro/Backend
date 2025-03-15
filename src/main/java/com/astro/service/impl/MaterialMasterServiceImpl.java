package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.MaterialMasterRequestDto;
import com.astro.dto.workflow.MaterialMasterResponseDto;
import com.astro.entity.MaterialMaster;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.entity.VendorNamesForJobWorkMaterial;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.MaterialMasterRepository;
import com.astro.repository.VendorNamesForJobWorkMaterialRepository;
import com.astro.service.MaterialMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class MaterialMasterServiceImpl implements MaterialMasterService {

    @Autowired
    private MaterialMasterRepository materialMasterRepository;
    @Autowired
    private VendorNamesForJobWorkMaterialRepository vendorNameRepository;
    @Override
    public MaterialMasterResponseDto createMaterialMaster(MaterialMasterRequestDto materialMasterRequestDto) {
        // Check if the indentorId already exists
      /*  if (materialMasterRepository.existsById(materialMasterRequestDto.getMaterialCode())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Material code", "Material  code " + materialMasterRequestDto.getMaterialCode() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

       */
        String materialCode = "M" + System.currentTimeMillis();
        MaterialMaster materialMaster= new MaterialMaster();
        materialMaster.setMaterialCode(materialCode);
        materialMaster.setCategory(materialMasterRequestDto.getCategory());
        materialMaster.setSubCategory(materialMasterRequestDto.getSubCategory());
        materialMaster.setDescription(materialMasterRequestDto.getDescription());
        materialMaster.setUom(materialMasterRequestDto.getUom());
        materialMaster.setEndOfLife(materialMasterRequestDto.getEndOfLife());
        materialMaster.setModeOfProcurement(materialMasterRequestDto.getModeOfProcurement());
        materialMaster.setDepreciationRate(materialMasterRequestDto.getDepreciationRate());
        materialMaster.setStockLevels(materialMasterRequestDto.getStockLevels());
        materialMaster.setConditionOfGoods(materialMasterRequestDto.getConditionOfGoods());
        materialMaster.setShelfLife(materialMasterRequestDto.getShelfLife());
        materialMaster.setUploadImageName(materialMasterRequestDto.getUploadImageFileName());
        materialMaster.setIndigenousOrImported(materialMasterRequestDto.getIndigenousOrImported());
        materialMaster.setEstimatedPriceWithCcy(materialMasterRequestDto.getEstimatedPriceWithCcy());
        materialMaster.setCreatedBy(materialMasterRequestDto.getCreatedBy());
        materialMaster.setUpdatedBy(materialMasterRequestDto.getUpdatedBy());
        materialMasterRepository.save(materialMaster);
        // Saveing Vendornames in different table
        if (materialMasterRequestDto.getVendorNames() != null && !materialMasterRequestDto.getVendorNames().isEmpty()) {
            List<VendorNamesForJobWorkMaterial> vendors = materialMasterRequestDto.getVendorNames().stream().map(vendorName -> {
                VendorNamesForJobWorkMaterial vendor = new VendorNamesForJobWorkMaterial();
                vendor.setVendorName(vendorName);
                vendor.setMaterialCode(materialCode);
                return vendor;
            }).collect(Collectors.toList());

            vendorNameRepository.saveAll(vendors);
        }
        return mapToResponseDTO(materialMaster);
    }


    @Override
    public MaterialMasterResponseDto updateMaterialMaster(String materialCode, MaterialMasterRequestDto materialMasterRequestDto) {
        MaterialMaster materialMaster = materialMasterRepository.findById(materialCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Materail master not found for the provided Material master ID.")
                ));

      //  materialMaster.setMaterialCode(materialMasterRequestDto.getMaterialCode());
        materialMaster.setCategory(materialMasterRequestDto.getCategory());
        materialMaster.setSubCategory(materialMasterRequestDto.getSubCategory());
        materialMaster.setDescription(materialMasterRequestDto.getDescription());
        materialMaster.setUom(materialMasterRequestDto.getUom());
        materialMaster.setEndOfLife(materialMasterRequestDto.getEndOfLife());
        materialMaster.setModeOfProcurement(materialMasterRequestDto.getModeOfProcurement());
        materialMaster.setDepreciationRate(materialMasterRequestDto.getDepreciationRate());
        materialMaster.setStockLevels(materialMasterRequestDto.getStockLevels());
        materialMaster.setConditionOfGoods(materialMasterRequestDto.getConditionOfGoods());
        materialMaster.setShelfLife(materialMasterRequestDto.getShelfLife());
        materialMaster.setUploadImageName(materialMasterRequestDto.getUploadImageFileName());
        materialMaster.setIndigenousOrImported(materialMasterRequestDto.getIndigenousOrImported());
        materialMaster.setEstimatedPriceWithCcy(materialMasterRequestDto.getEstimatedPriceWithCcy());
        materialMaster.setUpdatedBy(materialMasterRequestDto.getUpdatedBy());
        materialMaster.setCreatedBy(materialMasterRequestDto.getCreatedBy());
        materialMasterRepository.save(materialMaster);
        // Saveing Vendornames in different table
        if (materialMasterRequestDto.getVendorNames() != null && !materialMasterRequestDto.getVendorNames().isEmpty()) {
            List<VendorNamesForJobWorkMaterial> vendors = materialMasterRequestDto.getVendorNames().stream().map(vendorName -> {
                VendorNamesForJobWorkMaterial vendor = new VendorNamesForJobWorkMaterial();
                vendor.setVendorName(vendorName);
                vendor.setMaterialCode(materialCode);
                return vendor;
            }).collect(Collectors.toList());

            vendorNameRepository.saveAll(vendors);
        }

        return mapToResponseDTO(materialMaster);

    }

    @Override
    public List<MaterialMasterResponseDto> getAllMaterialMasters() {
        List<MaterialMaster> materialMasters= materialMasterRepository.findAll();
        return materialMasters.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public MaterialMasterResponseDto getMaterialMasterById(String materialCode) {
        MaterialMaster materialMaster= materialMasterRepository.findById(materialCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "material master not found for the provided materialcode.")
                ));
        return mapToResponseDTO(materialMaster);
    }

    @Override
    public void deleteMaterialMaster(String materialCode) {


        MaterialMaster materialMaster=materialMasterRepository.findById(materialCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Material master not found for the provided material code."
                        )
                ));
        try {
            materialMasterRepository.delete(materialMaster);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the material master."
                    ),
                    ex
            );
        }


    }
    private MaterialMasterResponseDto mapToResponseDTO(MaterialMaster materialMaster) {

        MaterialMasterResponseDto materialMasterResponseDto = new MaterialMasterResponseDto();
        materialMasterResponseDto.setMaterialCode(materialMaster.getMaterialCode());
        materialMasterResponseDto.setCategory(materialMaster.getCategory());
        materialMasterResponseDto.setSubCategory(materialMaster.getSubCategory());
        materialMasterResponseDto.setDescription(materialMaster.getDescription());
        materialMasterResponseDto.setUom(materialMaster.getUom());
        materialMasterResponseDto.setEndOfLife(materialMaster.getEndOfLife());
        materialMasterResponseDto.setModeOfProcurement(materialMaster.getModeOfProcurement());
        materialMasterResponseDto.setDepreciationRate(materialMaster.getDepreciationRate());
        materialMasterResponseDto.setStockLevels(materialMaster.getStockLevels());
        materialMasterResponseDto.setConditionOfGoods(materialMaster.getConditionOfGoods());
        materialMasterResponseDto.setShelfLife(materialMaster.getShelfLife());
        materialMasterResponseDto.setUploadImageFileName(materialMaster.getUploadImageName());
        materialMasterResponseDto.setIndigenousOrImported(materialMaster.getIndigenousOrImported());
        materialMasterResponseDto.setEstimatedPriceWithCcy(materialMaster.getEstimatedPriceWithCcy());
        materialMasterResponseDto.setCreatedBy(materialMaster.getCreatedBy());
        materialMasterResponseDto.setUpdatedBy(materialMaster.getUpdatedBy());
        materialMasterResponseDto.setCreatedDate(materialMaster.getCreatedDate());
        materialMasterResponseDto.setUpdatedDate(materialMaster.getUpdatedDate());

        List<String> vendorNames= vendorNameRepository.findByMaterialCode(materialMaster.getMaterialCode())
                .stream()
                .map(VendorNamesForJobWorkMaterial::getVendorName)
                .collect(Collectors.toList());
        materialMasterResponseDto.setVendorNames(vendorNames);
        return materialMasterResponseDto;

    }


}
