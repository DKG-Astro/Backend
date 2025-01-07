package com.astro.service.impl;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionDto;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.repository.InventoryModule.GoodsInspectionRepository;
import com.astro.service.GoodsInspectionService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsInspectionServiceImpl implements GoodsInspectionService {

    @Autowired
    private GoodsInspectionRepository repository;



    @Override
    public GoodsInspection createGoodsInspection(GoodsInspectionDto goodsInspectionDTO) {
         GoodsInspection goodsInspection = new GoodsInspection();
         goodsInspection.setGoodsInspectionNo(goodsInspectionDTO.getGoodsInspectionNo());
        String InstallationDate=goodsInspectionDTO.getInstallationDate();
        goodsInspection.setInstallationDate(CommonUtils.convertStringToDateObject(InstallationDate));
        String CommissioningDate= goodsInspectionDTO.getCommissioningDate();
        goodsInspection.setCommissioningDate(CommonUtils.convertStringToDateObject(CommissioningDate));
        goodsInspection.setUploadInstallationReport(goodsInspectionDTO.getUploadInstallationReport());
        goodsInspection.setAcceptedQuantity(goodsInspectionDTO.getAcceptedQuantity());
        goodsInspection.setRejectedQuantity(goodsInspectionDTO.getRejectedQuantity());
        goodsInspection.setUpdatedBy(goodsInspectionDTO.getUpdatedBy());
        GoodsInspection saved = repository.save(goodsInspection);
        return saved;
    }

    @Override
    public GoodsInspection updateGoodsInspection(Long id, GoodsInspectionDto goodsInspectionDTO) {
        GoodsInspection existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Goods Inspection not found"));
        existing.setGoodsInspectionNo(goodsInspectionDTO.getGoodsInspectionNo());
        String InstallationDate=goodsInspectionDTO.getInstallationDate();
        existing.setInstallationDate(CommonUtils.convertStringToDateObject(InstallationDate));
        String CommissioningDate= goodsInspectionDTO.getCommissioningDate();
        existing.setCommissioningDate(CommonUtils.convertStringToDateObject(CommissioningDate));
        existing.setUploadInstallationReport(goodsInspectionDTO.getUploadInstallationReport());
        existing.setAcceptedQuantity(goodsInspectionDTO.getAcceptedQuantity());
        existing.setRejectedQuantity(goodsInspectionDTO.getRejectedQuantity());
        existing.setUpdatedBy(goodsInspectionDTO.getUpdatedBy());
        GoodsInspection updated = repository.save(existing);
        return updated;
    }

    @Override
    public List<GoodsInspection> getAllGoodsInspections() {
        return repository.findAll();
    }

    @Override
    public GoodsInspection getGoodsInspectionById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("PO not found!"));
    }
    @Override
    public void deleteGoodsInspection(Long id) {
        repository.deleteById(id);
    }



}
