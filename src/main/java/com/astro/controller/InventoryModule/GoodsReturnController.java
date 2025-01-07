package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsReturnDto;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.service.GoodsReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-returns")
public class GoodsReturnController {

    @Autowired
    private GoodsReturnService service;


    @PostMapping
    public ResponseEntity<GoodsReturn> createGoodsReturn(@RequestBody GoodsReturnDto goodsReturnDto) {
       GoodsReturn goodsReturn= service.createGoodsReturn(goodsReturnDto);
        return ResponseEntity.ok(goodsReturn);
    }
    @GetMapping
    public ResponseEntity<List<GoodsReturn>> getAllGoodsReturns() {
        List<GoodsReturn> goodsReturn=service.getAllGoodsReturns();
        return ResponseEntity.ok(goodsReturn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReturn> getGoodsReturnById(@PathVariable Long id) {
        GoodsReturn goodsReturn =service.getGoodsReturnById(id);
        return ResponseEntity.ok(goodsReturn);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoodsReturn> updateGoodsReturn(
            @PathVariable Long id, @RequestBody GoodsReturnDto goodsReturnDto) {
        GoodsReturn goodsReturn =service.updateGoodsReturn(id, goodsReturnDto);
        return ResponseEntity.ok(goodsReturn);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsReturn(@PathVariable Long id) {
        service.deleteGoodsReturn(id);
        return ResponseEntity.ok("Goods Return deleted successfully!");
    }


}
