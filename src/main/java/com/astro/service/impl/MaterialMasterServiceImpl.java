package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.MaterialMasterRequestDto;
import com.astro.dto.workflow.MaterialMasterResponseDto;
import com.astro.entity.MaterialMaster;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.MaterialMasterRepository;
import com.astro.service.MaterialMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialMasterServiceImpl implements MaterialMasterService {

    @Autowired
    private MaterialMasterRepository materialMasterRepository;
    @Override
    public MaterialMasterResponseDto createMaterialMaster(MaterialMasterRequestDto materialMasterRequestDto) {
        MaterialMaster materialMaster= new MaterialMaster();
        materialMaster.setMaterialCode(materialMasterRequestDto.getMaterialCode());
        materialMaster.setCategory(materialMasterRequestDto.getCategory());
        materialMaster.setSubCategory(materialMasterRequestDto.getSubCategory());
        materialMaster.setDescription(materialMasterRequestDto.getDescription());
        materialMaster.setUom(materialMasterRequestDto.getUom());
        materialMaster.setEndOfLife(materialMasterRequestDto.getEndOfLife());
        materialMaster.setModeOfProcurement(materialMasterRequestDto.getModeOfProcurement());
        materialMaster.setDepreciationRate(materialMasterRequestDto.getDepreciationRate());
        materialMaster.setStockLevelsMax(materialMasterRequestDto.getStockLevelsMax());
        materialMaster.setStockLevelsMin(materialMasterRequestDto.getStockLevelsMin());
        materialMaster.setReOrderLevel(materialMasterRequestDto.getReOrderLevel());
        materialMaster.setConditionOfGoods(materialMasterRequestDto.getConditionOfGoods());
        materialMaster.setShelfLife(materialMasterRequestDto.getShelfLife());
        materialMaster.setUploadImage(materialMasterRequestDto.getUploadImage());
        materialMaster.setIndigenousOrImported(materialMasterRequestDto.getIndigenousOrImported());
        materialMaster.setCreatedBy(materialMasterRequestDto.getCreatedBy());
        materialMaster.setUpdatedBy(materialMasterRequestDto.getUpdatedBy());
        materialMasterRepository.save(materialMaster);

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

        materialMaster.setMaterialCode(materialMasterRequestDto.getMaterialCode());
        materialMaster.setCategory(materialMasterRequestDto.getCategory());
        materialMaster.setSubCategory(materialMasterRequestDto.getSubCategory());
        materialMaster.setDescription(materialMasterRequestDto.getDescription());
        materialMaster.setUom(materialMasterRequestDto.getUom());
        materialMaster.setEndOfLife(materialMasterRequestDto.getEndOfLife());
        materialMaster.setModeOfProcurement(materialMasterRequestDto.getModeOfProcurement());
        materialMaster.setDepreciationRate(materialMasterRequestDto.getDepreciationRate());
        materialMaster.setStockLevelsMax(materialMasterRequestDto.getStockLevelsMax());
        materialMaster.setStockLevelsMin(materialMasterRequestDto.getStockLevelsMin());
        materialMaster.setReOrderLevel(materialMasterRequestDto.getReOrderLevel());
        materialMaster.setConditionOfGoods(materialMasterRequestDto.getConditionOfGoods());
        materialMaster.setShelfLife(materialMasterRequestDto.getShelfLife());
        materialMaster.setUploadImage(materialMasterRequestDto.getUploadImage());
        materialMaster.setIndigenousOrImported(materialMasterRequestDto.getIndigenousOrImported());
        materialMaster.setUpdatedBy(materialMasterRequestDto.getUpdatedBy());
        materialMaster.setCreatedBy(materialMasterRequestDto.getCreatedBy());
        materialMasterRepository.save(materialMaster);

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
        materialMasterResponseDto.setStockLevelsMax(materialMaster.getStockLevelsMax());
        materialMasterResponseDto.setStockLevelsMin(materialMaster.getStockLevelsMin());
        materialMasterResponseDto.setReOrderLevel(materialMaster.getReOrderLevel());
        materialMasterResponseDto.setConditionOfGoods(materialMaster.getConditionOfGoods());
        materialMasterResponseDto.setShelfLife(materialMaster.getShelfLife());
        materialMasterResponseDto.setUploadImage(materialMaster.getUploadImage());
        materialMasterResponseDto.setIndigenousOrImported(materialMaster.getIndigenousOrImported());
        materialMasterResponseDto.setCreatedBy(materialMaster.getCreatedBy());
        materialMasterResponseDto.setUpdatedBy(materialMaster.getUpdatedBy());
        materialMasterResponseDto.setCreatedDate(materialMaster.getCreatedDate());
        materialMasterResponseDto.setUpdatedDate(materialMaster.getUpdatedDate());

        return materialMasterResponseDto;

    }

}
