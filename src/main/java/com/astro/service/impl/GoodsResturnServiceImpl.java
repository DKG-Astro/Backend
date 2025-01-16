package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GoodsReturnRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReturnResponseDto;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.GoodsReturnRepository;
import com.astro.service.GoodsReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsResturnServiceImpl implements GoodsReturnService {

    @Autowired
    private GoodsReturnRepository repository;

    @Override
    public GoodsReturnResponseDto createGoodsReturn(GoodsReturnRequestDto goodsReturnDto) {
        GoodsReturn goodsReturn = new GoodsReturn();
        goodsReturn.setGoodsReturnNoteNo(goodsReturnDto.getGoodsReturnNoteNo());
        goodsReturn.setRejectedQuantity(goodsReturnDto.getRejectedQuantity());
        goodsReturn.setReturnQuantity(goodsReturnDto.getReturnQuantity());
        goodsReturn.setTypeOfReturn(goodsReturnDto.getTypeOfReturn());
        goodsReturn.setReasonOfReturn(goodsReturnDto.getReasonOfReturn());
        goodsReturn.setUpdatedBy(goodsReturnDto.getUpdatedBy());
        goodsReturn.setCreatedBy(goodsReturnDto.getCreatedBy());
         repository.save(goodsReturn);
         return mapToResponseDTO(goodsReturn);

    }


    @Override
    public GoodsReturnResponseDto updateGoodsReturn(Long id, GoodsReturnRequestDto goodsReturnDto) {
        GoodsReturn existing = repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Goods return not found for the provided asset ID.")
                ));
        existing.setGoodsReturnNoteNo(goodsReturnDto.getGoodsReturnNoteNo());
        existing.setRejectedQuantity(goodsReturnDto.getRejectedQuantity());
        existing.setReturnQuantity(goodsReturnDto.getReturnQuantity());
        existing.setTypeOfReturn(goodsReturnDto.getTypeOfReturn());
        existing.setReasonOfReturn(goodsReturnDto.getReasonOfReturn());
        existing.setUpdatedBy(goodsReturnDto.getUpdatedBy());
        existing.setCreatedBy(goodsReturnDto.getCreatedBy());
        repository.save(existing);
        return mapToResponseDTO(existing);
    }

    @Override
    public List<GoodsReturnResponseDto> getAllGoodsReturns() {

        List<GoodsReturn> goodsReturns = repository.findAll();
        return goodsReturns.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public GoodsReturnResponseDto getGoodsReturnById(Long id) {
     GoodsReturn goodsReturn= repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Goods Return not found for the provided asset ID.")
                ));
        return mapToResponseDTO(goodsReturn);
    }
    @Override
    public void deleteGoodsReturn(Long id) {

     GoodsReturn  goodsReturn=repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                " Goods return not found for the provided ID."
                        )
                ));
        try {
            repository.delete(goodsReturn);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  Goods return."
                    ),
                    ex
            );
        }
    }

    private GoodsReturnResponseDto mapToResponseDTO(GoodsReturn goodsReturn) {
        GoodsReturnResponseDto goodsReturnResponseDto = new GoodsReturnResponseDto();
        goodsReturnResponseDto.setId(goodsReturn.getId());
        goodsReturnResponseDto .setGoodsReturnNoteNo(goodsReturn.getGoodsReturnNoteNo());
        goodsReturnResponseDto .setRejectedQuantity(goodsReturn.getRejectedQuantity());
        goodsReturnResponseDto .setReturnQuantity(goodsReturn.getReturnQuantity());
        goodsReturnResponseDto .setTypeOfReturn(goodsReturn.getTypeOfReturn());
        goodsReturnResponseDto .setReasonOfReturn(goodsReturn.getReasonOfReturn());
        goodsReturnResponseDto .setUpdatedBy(goodsReturn.getUpdatedBy());
        goodsReturnResponseDto .setCreatedBy(goodsReturn.getCreatedBy());
        goodsReturnResponseDto.setCreatedDate(goodsReturn.getCreatedDate());
        goodsReturnResponseDto.setUpdatedDate(goodsReturn.getUpdatedDate());
        return goodsReturnResponseDto;
    }

}
