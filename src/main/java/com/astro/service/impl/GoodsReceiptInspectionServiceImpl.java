package com.astro.service.impl;

import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionDto;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;
import com.astro.repository.InventoryModule.GoodsReceiptInspectionRepository;
import com.astro.service.GoodsReceiptInspectionService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsReceiptInspectionServiceImpl implements GoodsReceiptInspectionService {

    @Autowired
    private GoodsReceiptInspectionRepository GRIrepository;

    @Override
    public GoodsReceiptInspection createGoodsReceiptInspection(GoodsReceiptInspectionDto dto) {
        GoodsReceiptInspection entity = new GoodsReceiptInspection();
        entity.setReceiptInspectionNo(dto.getReceiptInspectionNo());
        String InstallationDate = dto.getInstallationDate();
        entity.setInstallationDate(CommonUtils.convertStringToDateObject(InstallationDate));
        String CommissioningDate = dto.getCommissioningDate();
        entity.setCommissioningDate(CommonUtils.convertStringToDateObject(CommissioningDate));
        entity.setAssetCode(dto.getAssetCode());
        entity.setAdditionalMaterialDescription(dto.getAdditionalMaterialDescription());
        entity.setLocator(dto.getLocator());
        entity.setPrintLabelOption(dto.isPrintLabelOption());
        entity.setDepreciationRate(dto.getDepreciationRate());
        entity.setAttachComponentPopup(dto.getAttachComponentPopup());
        entity.setUpdatedBy(dto.getUpdatedBy());
        return GRIrepository.save(entity);
    }

    @Override
    public GoodsReceiptInspection updateGoodsReceiptInspection(Long id, GoodsReceiptInspectionDto dto) {
        GoodsReceiptInspection entity = GRIrepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        entity.setReceiptInspectionNo(dto.getReceiptInspectionNo());
        String InstallationDate = dto.getInstallationDate();
        entity.setInstallationDate(CommonUtils.convertStringToDateObject(InstallationDate));
        String CommissioningDate = dto.getCommissioningDate();
        entity.setCommissioningDate(CommonUtils.convertStringToDateObject(CommissioningDate));
        entity.setAssetCode(dto.getAssetCode());
        entity.setAdditionalMaterialDescription(dto.getAdditionalMaterialDescription());
        entity.setLocator(dto.getLocator());
        entity.setPrintLabelOption(dto.isPrintLabelOption());
        entity.setDepreciationRate(dto.getDepreciationRate());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setAttachComponentPopup(dto.getAttachComponentPopup());
        return GRIrepository.save(entity);
    }

    @Override
    public List<GoodsReceiptInspection> getAllGoodsReceiptInspections() {
        return GRIrepository.findAll();
    }

    @Override
    public GoodsReceiptInspection getGoodsReceiptInspectionById(Long id) {
        return GRIrepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
    }

    @Override
    public void deleteGoodsReceiptInspection(Long id) {
        GRIrepository.deleteById(id);
    }


}
