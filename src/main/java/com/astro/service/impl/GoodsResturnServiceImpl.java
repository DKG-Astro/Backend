package com.astro.service.impl;

import com.astro.dto.workflow.InventoryModule.GoodsReturnDto;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.repository.InventoryModule.GoodsReturnRepository;
import com.astro.service.GoodsReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsResturnServiceImpl implements GoodsReturnService {

    @Autowired
    private GoodsReturnRepository repository;

    @Override
    public GoodsReturn createGoodsReturn(GoodsReturnDto goodsReturnDto) {
        GoodsReturn goodsReturn = new GoodsReturn();
        goodsReturn.setGoodsReturnNoteNo(goodsReturnDto.getGoodsReturnNoteNo());
        goodsReturn.setRejectedQuantity(goodsReturnDto.getRejectedQuantity());
        goodsReturn.setReturnQuantity(goodsReturnDto.getReturnQuantity());
        goodsReturn.setTypeOfReturn(goodsReturnDto.getTypeOfReturn());
        goodsReturn.setReasonOfReturn(goodsReturnDto.getReasonOfReturn());
        return repository.save(goodsReturn);
    }

    @Override
    public GoodsReturn updateGoodsReturn(Long id, GoodsReturnDto goodsReturnDto) {
        GoodsReturn existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods Return not found!"));

        existing.setGoodsReturnNoteNo(goodsReturnDto.getGoodsReturnNoteNo());
        existing.setRejectedQuantity(goodsReturnDto.getRejectedQuantity());
        existing.setReturnQuantity(goodsReturnDto.getReturnQuantity());
        existing.setTypeOfReturn(goodsReturnDto.getTypeOfReturn());
        existing.setReasonOfReturn(goodsReturnDto.getReasonOfReturn());

        return repository.save(existing);
    }

    @Override
    public List<GoodsReturn> getAllGoodsReturns() {

        return repository.findAll();
    }

    @Override
    public GoodsReturn getGoodsReturnById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods Return not found!"));
    }
    @Override
    public void deleteGoodsReturn(Long id) {

        repository.deleteById(id);
    }
}
