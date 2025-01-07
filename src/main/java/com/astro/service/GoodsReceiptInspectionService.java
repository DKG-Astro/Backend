package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionDto;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;

import java.util.List;

public interface GoodsReceiptInspectionService {

    GoodsReceiptInspection createGoodsReceiptInspection(GoodsReceiptInspectionDto dto);
    GoodsReceiptInspection updateGoodsReceiptInspection(Long id, GoodsReceiptInspectionDto dto);
    List<GoodsReceiptInspection> getAllGoodsReceiptInspections();
    GoodsReceiptInspection getGoodsReceiptInspectionById(Long id);
    void deleteGoodsReceiptInspection(Long id);
}
