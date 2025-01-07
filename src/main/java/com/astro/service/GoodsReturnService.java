package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsReturnDto;
import com.astro.entity.InventoryModule.GoodsReturn;

import java.util.List;

public interface GoodsReturnService {


    List<GoodsReturn> getAllGoodsReturns();
    GoodsReturn getGoodsReturnById(Long id);
    GoodsReturn createGoodsReturn(GoodsReturnDto goodsReturnDto);
    GoodsReturn updateGoodsReturn(Long id, GoodsReturnDto goodsReturnDto);
    void deleteGoodsReturn(Long id);

}
