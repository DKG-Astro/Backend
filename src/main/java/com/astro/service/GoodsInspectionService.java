package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionDto;
import com.astro.entity.InventoryModule.GoodsInspection;

import java.util.List;

public interface GoodsInspectionService {

    List<GoodsInspection> getAllGoodsInspections();

    GoodsInspection getGoodsInspectionById(Long id);

    GoodsInspection createGoodsInspection(GoodsInspectionDto goodsInspectionDTO);

    GoodsInspection updateGoodsInspection(Long id, GoodsInspectionDto goodsInspectionDTO);

    void deleteGoodsInspection(Long id);

}
