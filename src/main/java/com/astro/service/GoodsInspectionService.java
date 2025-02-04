package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsInspectionResponseDto;
import com.astro.entity.InventoryModule.GoodsInspection;

import java.util.List;

public interface GoodsInspectionService {

    List<GoodsInspectionResponseDto> getAllGoodsInspections();

    GoodsInspectionResponseDto getGoodsInspectionById(String goodsInspectionNo);

    GoodsInspectionResponseDto createGoodsInspection(GoodsInspectionRequestDto goodsInspectionDTO);

    GoodsInspectionResponseDto updateGoodsInspection(String goodsInspectionNo, GoodsInspectionRequestDto goodsInspectionDTO);

    void deleteGoodsInspection(String goodsInspectionNo);

}
