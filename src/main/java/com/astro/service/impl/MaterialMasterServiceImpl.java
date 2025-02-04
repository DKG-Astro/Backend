package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.MaterialMasterRequestDto;
import com.astro.dto.workflow.MaterialMasterResponseDto;
import com.astro.entity.MaterialMaster;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.MaterialMasterRepository;
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
    @Override
    public MaterialMasterResponseDto createMaterialMaster(MaterialMasterRequestDto materialMasterRequestDto,String uploadImageFileName) {
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
        materialMaster.setUploadImageName(uploadImageFileName);
        materialMaster.setIndigenousOrImported(materialMasterRequestDto.getIndigenousOrImported());
        materialMaster.setCreatedBy(materialMasterRequestDto.getCreatedBy());
        materialMaster.setUpdatedBy(materialMasterRequestDto.getUpdatedBy());
        handleFileUpload(materialMaster, materialMasterRequestDto.getUploadImage(),
                materialMaster::setUploadImage);
        materialMasterRepository.save(materialMaster);

        return mapToResponseDTO(materialMaster);
    }


    @Override
    public MaterialMasterResponseDto updateMaterialMaster(String materialCode, MaterialMasterRequestDto materialMasterRequestDto, String uploadImageFileName) {
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
        materialMaster.setStockLevelsMax(materialMasterRequestDto.getStockLevelsMax());
        materialMaster.setStockLevelsMin(materialMasterRequestDto.getStockLevelsMin());
        materialMaster.setReOrderLevel(materialMasterRequestDto.getReOrderLevel());
        materialMaster.setConditionOfGoods(materialMasterRequestDto.getConditionOfGoods());
        materialMaster.setShelfLife(materialMasterRequestDto.getShelfLife());
        materialMaster.setUploadImageName(uploadImageFileName);
        materialMaster.setIndigenousOrImported(materialMasterRequestDto.getIndigenousOrImported());
        materialMaster.setUpdatedBy(materialMasterRequestDto.getUpdatedBy());
        materialMaster.setCreatedBy(materialMasterRequestDto.getCreatedBy());
        handleFileUpload(materialMaster, materialMasterRequestDto.getUploadImage(),
                materialMaster::setUploadImage);
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
        materialMasterResponseDto.setUploadImage(materialMaster.getUploadImageName());
        materialMasterResponseDto.setIndigenousOrImported(materialMaster.getIndigenousOrImported());
        materialMasterResponseDto.setCreatedBy(materialMaster.getCreatedBy());
        materialMasterResponseDto.setUpdatedBy(materialMaster.getUpdatedBy());
        materialMasterResponseDto.setCreatedDate(materialMaster.getCreatedDate());
        materialMasterResponseDto.setUpdatedDate(materialMaster.getUpdatedDate());

        return materialMasterResponseDto;

    }
    public void handleFileUpload(MaterialMaster materialMaster, MultipartFile file, Consumer<byte[]> fileSetter) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileSetter.accept(fileBytes); // Set file content (byte[])

            } catch (IOException e) {
                throw new InvalidInputException(new ErrorDetails(500, 3, "File Processing Error",
                        "Error while processing the uploaded file. Please try again."));
            }
        } else {
            fileSetter.accept(null);  // Handle gracefully if no file is uploaded
        }
    }

}
