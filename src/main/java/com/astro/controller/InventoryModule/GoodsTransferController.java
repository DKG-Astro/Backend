package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsReturnRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReturnResponseDto;
import com.astro.dto.workflow.InventoryModule.GoodsTransferRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsTransferResponseDto;
import com.astro.repository.InventoryModule.GoodsTransferRepository;
import com.astro.service.GoodsTransferService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-transfer")
public class GoodsTransferController {
    @Autowired
    private GoodsTransferService  goodsTransferService;

    @PostMapping
    public ResponseEntity<Object> createGoodsTransfer(@RequestBody GoodsTransferRequestDto goodsTransferRequestDto) {
        GoodsTransferResponseDto goodsTransfer= goodsTransferService.createGoodsTransfer(goodsTransferRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsTransfer), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllGoodsTransfers() {
        List<GoodsTransferResponseDto> goodsTransfer=goodsTransferService.getAllGoodsTransfer();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsTransfer), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGoodsTransferById(@PathVariable Long id) {
        GoodsTransferResponseDto goodsTransfer =goodsTransferService.getGoodsTransferById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsTransfer), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGoodsTransfer(
            @PathVariable Long id, @RequestBody GoodsTransferRequestDto goodsTransferRequestDto) {
        GoodsTransferResponseDto goodsTransfer =goodsTransferService.updateGoodsTransfer(id, goodsTransferRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsTransfer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsTransfer(@PathVariable Long id) {
        goodsTransferService.deleteGoodsTransfer(id);
        return ResponseEntity.ok("Goods transfer deleted successfully!");
    }


}
