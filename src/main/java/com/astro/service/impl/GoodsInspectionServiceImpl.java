package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GoodsInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsInspectionResponseDto;
import com.astro.entity.InventoryModule.Asset;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.GoodsInspectionRepository;
import com.astro.service.GoodsInspectionService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsInspectionServiceImpl implements GoodsInspectionService {

    @Autowired
    private GoodsInspectionRepository repository;



    @Override
    public GoodsInspectionResponseDto createGoodsInspection(GoodsInspectionRequestDto goodsInspectionDTO) {
         GoodsInspection goodsInspection = new GoodsInspection();
        String InstallationDate=goodsInspectionDTO.getInstallationDate();
        goodsInspection.setInstallationDate(CommonUtils.convertStringToDateObject(InstallationDate));
        String CommissioningDate= goodsInspectionDTO.getCommissioningDate();
        goodsInspection.setCommissioningDate(CommonUtils.convertStringToDateObject(CommissioningDate));
        goodsInspection.setUploadInstallationReport(goodsInspectionDTO.getUploadInstallationReport());
        goodsInspection.setAcceptedQuantity(goodsInspectionDTO.getAcceptedQuantity());
        goodsInspection.setRejectedQuantity(goodsInspectionDTO.getRejectedQuantity());
        goodsInspection.setGoodsReturnPermamentOrReplacement(goodsInspectionDTO.getGoodsReturnPermamentOrReplacement());
        goodsInspection.setGoodsReturnFullOrPartial(goodsInspectionDTO.getGoodsReturnFullOrPartial());
        goodsInspection.setGoodsReturnReason(goodsInspectionDTO.getGoodsReturnReason());
        goodsInspection.setMaterialRejectionAdviceSent(goodsInspectionDTO.getMaterialRejectionAdviceSent());
        goodsInspection.setPoAmendmentNotified(goodsInspectionDTO.getPoAmendmentNotified());
        goodsInspection.setUpdatedBy(goodsInspectionDTO.getUpdatedBy());
        goodsInspection.setCreatedBy(goodsInspectionDTO.getCreatedBy());
        GoodsInspection saved = repository.save(goodsInspection);
        return mapToResponseDTO(saved);
    }

    private GoodsInspectionResponseDto mapToResponseDTO(GoodsInspection saved) {
        GoodsInspectionResponseDto  goodsInspectionResponseDto = new GoodsInspectionResponseDto();

        goodsInspectionResponseDto.setGoodsInspectionNo(saved.getGoodsInspectionNo());
      LocalDate InstallationDate=saved.getInstallationDate();
        goodsInspectionResponseDto.setInstallationDate(CommonUtils.convertDateToString(InstallationDate));
       LocalDate CommissioningDate= saved.getCommissioningDate();
        goodsInspectionResponseDto.setCommissioningDate(CommonUtils.convertDateToString(CommissioningDate));
        goodsInspectionResponseDto.setUploadInstallationReport(saved.getUploadInstallationReport());
        goodsInspectionResponseDto.setAcceptedQuantity(saved.getAcceptedQuantity());
        goodsInspectionResponseDto.setRejectedQuantity(saved.getRejectedQuantity());
        goodsInspectionResponseDto.setGoodsReturnPermamentOrReplacement(saved.getGoodsReturnPermamentOrReplacement());
        goodsInspectionResponseDto.setGoodsReturnFullOrPartial(saved.getGoodsReturnFullOrPartial());
        goodsInspectionResponseDto.setGoodsReturnReason(saved.getGoodsReturnReason());
        goodsInspectionResponseDto.setMaterialRejectionAdviceSent(saved.getMaterialRejectionAdviceSent());
        goodsInspectionResponseDto.setPoAmendmentNotified(saved.getPoAmendmentNotified());
        goodsInspectionResponseDto.setUpdatedBy(saved.getUpdatedBy());
        goodsInspectionResponseDto.setCreatedBy(saved.getCreatedBy());
        goodsInspectionResponseDto.setCreatedDate(saved.getCreatedDate());
        goodsInspectionResponseDto.setUpdatedDate(saved.getUpdatedDate());
        return goodsInspectionResponseDto;
    }

    @Override
    public GoodsInspectionResponseDto updateGoodsInspection(Long goodsInspectionNo, GoodsInspectionRequestDto goodsInspectionDTO) {
        GoodsInspection existing = repository.findById(goodsInspectionNo)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Goods Inspection not found for the provided goodsInspectionNo.")
                ));

        String InstallationDate=goodsInspectionDTO.getInstallationDate();
        existing.setInstallationDate(CommonUtils.convertStringToDateObject(InstallationDate));
        String CommissioningDate= goodsInspectionDTO.getCommissioningDate();
        existing.setCommissioningDate(CommonUtils.convertStringToDateObject(CommissioningDate));
        existing.setUploadInstallationReport(goodsInspectionDTO.getUploadInstallationReport());
        existing.setAcceptedQuantity(goodsInspectionDTO.getAcceptedQuantity());
        existing.setRejectedQuantity(goodsInspectionDTO.getRejectedQuantity());
        existing.setGoodsReturnPermamentOrReplacement(goodsInspectionDTO.getGoodsReturnPermamentOrReplacement());
        existing.setGoodsReturnFullOrPartial(goodsInspectionDTO.getGoodsReturnFullOrPartial());
        existing.setGoodsReturnReason(goodsInspectionDTO.getGoodsReturnReason());
        existing.setPoAmendmentNotified(goodsInspectionDTO.getPoAmendmentNotified());
        existing.setMaterialRejectionAdviceSent(goodsInspectionDTO.getMaterialRejectionAdviceSent());
        existing.setUpdatedBy(goodsInspectionDTO.getUpdatedBy());
        existing.setCreatedBy(goodsInspectionDTO.getCreatedBy());
        GoodsInspection updated = repository.save(existing);
        return mapToResponseDTO(updated);
    }

    @Override
    public List<GoodsInspectionResponseDto> getAllGoodsInspections() {

        List<GoodsInspection> goodsInspections = repository.findAll();
        return goodsInspections.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public GoodsInspectionResponseDto getGoodsInspectionById(Long goodsInspectionNo) {
       GoodsInspection inspection = repository.findById(goodsInspectionNo)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Goods Inspection not found for the provided goodsInspectionNo.")
                ));
        return mapToResponseDTO(inspection);
    }
    @Override
    public void deleteGoodsInspection(Long goodsInspectionNo) {

     GoodsInspection inspection =repository.findById(goodsInspectionNo)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                " Goods Inspection not found for the provided ID."
                        )
                ));
        try {
           repository.delete(inspection);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  Goods Inspection."
                    ),
                    ex
            );
        }
    }



}
