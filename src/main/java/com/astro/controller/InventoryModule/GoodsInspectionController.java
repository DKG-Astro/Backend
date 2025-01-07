package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionDto;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.service.GoodsInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goods-inspections")
public class GoodsInspectionController {

    @Autowired
    private GoodsInspectionService goodsInspectionService;



    // Create a new Goods Inspection
    @PostMapping
    public ResponseEntity<GoodsInspection> createGoodsInspection(@RequestBody GoodsInspectionDto goodsInspectionDto) {
        GoodsInspection createdInspection = goodsInspectionService.createGoodsInspection(goodsInspectionDto);
        return ResponseEntity.ok(createdInspection);
    }
    // Get all Goods Inspections
    @GetMapping
    public ResponseEntity<List<GoodsInspection>> getAllGoodsInspections() {
        List<GoodsInspection> inspections = goodsInspectionService.getAllGoodsInspections();
        return ResponseEntity.ok(inspections);
    }

    // Get a single Goods Inspection by ID
    @GetMapping("/{id}")
    public ResponseEntity<GoodsInspection> getGoodsInspectionById(@PathVariable Long id) {
        GoodsInspection inspection = goodsInspectionService.getGoodsInspectionById(id);
        return ResponseEntity.ok(inspection);
    }


    // Update an existing Goods Inspection by ID
    @PutMapping("/{id}")
    public ResponseEntity<GoodsInspection> updateGoodsInspection(@PathVariable Long id, @RequestBody GoodsInspectionDto goodsInspectionDto) {
        GoodsInspection updatedInspection = goodsInspectionService.updateGoodsInspection(id, goodsInspectionDto);
        return ResponseEntity.ok(updatedInspection);
    }

    // Delete a Goods Inspection by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsInspection(@PathVariable Long id) {
        goodsInspectionService.deleteGoodsInspection(id);
        return ResponseEntity.ok("Purchase Order deleted successfully."+" " +id);
    }
}
