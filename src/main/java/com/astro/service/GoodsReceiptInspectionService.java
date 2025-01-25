package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionResponseDto;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;

import java.util.List;

public interface GoodsReceiptInspectionService {

    GoodsReceiptInspectionResponseDto createGoodsReceiptInspection(GoodsReceiptInspectionRequestDto dto);
    GoodsReceiptInspectionResponseDto updateGoodsReceiptInspection(Long id, GoodsReceiptInspectionRequestDto dto);
    List<GoodsReceiptInspectionResponseDto> getAllGoodsReceiptInspections();
    GoodsReceiptInspectionResponseDto getGoodsReceiptInspectionById(Long id);
    void deleteGoodsReceiptInspection(Long id);
}
