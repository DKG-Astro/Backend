package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsReturnRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReturnResponseDto;
import com.astro.entity.InventoryModule.GoodsReturn;

import java.util.List;

public interface GoodsReturnService {


    List<GoodsReturnResponseDto> getAllGoodsReturns();
    GoodsReturnResponseDto getGoodsReturnById(Long id);
    GoodsReturnResponseDto createGoodsReturn(GoodsReturnRequestDto goodsReturnDto);
    GoodsReturnResponseDto updateGoodsReturn(Long id, GoodsReturnRequestDto goodsReturnDto);
    void deleteGoodsReturn(Long id);

}
