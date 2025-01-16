package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsReturnRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReturnResponseDto;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.service.GoodsReturnService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-returns")
public class GoodsReturnController {

    @Autowired
    private GoodsReturnService service;


    @PostMapping
    public ResponseEntity<Object> createGoodsReturn(@RequestBody GoodsReturnRequestDto goodsReturnDto) {
       GoodsReturnResponseDto goodsReturn= service.createGoodsReturn(goodsReturnDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsReturn), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllGoodsReturns() {
        List<GoodsReturnResponseDto> goodsReturn=service.getAllGoodsReturns();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsReturn), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGoodsReturnById(@PathVariable Long id) {
        GoodsReturnResponseDto goodsReturn =service.getGoodsReturnById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsReturn), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGoodsReturn(
            @PathVariable Long id, @RequestBody GoodsReturnRequestDto goodsReturnDto) {
        GoodsReturnResponseDto goodsReturn =service.updateGoodsReturn(id, goodsReturnDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(goodsReturn), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsReturn(@PathVariable Long id) {
        service.deleteGoodsReturn(id);
        return ResponseEntity.ok("Goods Return deleted successfully!");
    }


}
