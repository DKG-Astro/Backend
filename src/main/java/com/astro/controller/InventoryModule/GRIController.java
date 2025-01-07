package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionDto;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;
import com.astro.service.GoodsReceiptInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-receipt-inspection")
public class GRIController {


    @Autowired
    private GoodsReceiptInspectionService service;

    @PostMapping
    public ResponseEntity<GoodsReceiptInspection> createGoodsReceiptInspection(@RequestBody GoodsReceiptInspectionDto dto) {
        GoodsReceiptInspection created = service.createGoodsReceiptInspection(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoodsReceiptInspection> updateGoodsReceiptInspection(@PathVariable Long id, @RequestBody GoodsReceiptInspectionDto dto) {
        GoodsReceiptInspection updated = service.updateGoodsReceiptInspection(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<GoodsReceiptInspection>> getAllGoodsReceiptInspections() {
        List<GoodsReceiptInspection> gri = service.getAllGoodsReceiptInspections();
        return ResponseEntity.ok(gri);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceiptInspection> getGoodsReceiptInspectionById(@PathVariable Long id) {
        GoodsReceiptInspection gri = service.getGoodsReceiptInspectionById(id);
        return ResponseEntity.ok(gri);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsReceiptInspection(@PathVariable Long id) {
        service.deleteGoodsReceiptInspection(id);
        return ResponseEntity.ok("GoodsReceiptInspection deleted successfully!");
    }


}
