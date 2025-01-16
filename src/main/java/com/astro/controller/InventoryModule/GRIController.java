package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionResponseDto;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;
import com.astro.service.GoodsReceiptInspectionService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-receipt-inspection")
public class GRIController {


    @Autowired
    private GoodsReceiptInspectionService service;

    @PostMapping
    public ResponseEntity<Object> createGoodsReceiptInspection(@RequestBody GoodsReceiptInspectionRequestDto dto) {
        GoodsReceiptInspectionResponseDto created = service.createGoodsReceiptInspection(dto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGoodsReceiptInspection(@PathVariable Long id, @RequestBody GoodsReceiptInspectionRequestDto dto) {
        GoodsReceiptInspectionResponseDto updated = service.updateGoodsReceiptInspection(id, dto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllGoodsReceiptInspections() {
        List<GoodsReceiptInspectionResponseDto> gri = service.getAllGoodsReceiptInspections();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gri), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGoodsReceiptInspectionById(@PathVariable Long id) {
        GoodsReceiptInspectionResponseDto gri = service.getGoodsReceiptInspectionById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gri), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsReceiptInspection(@PathVariable Long id) {
        service.deleteGoodsReceiptInspection(id);
        return ResponseEntity.ok("GoodsReceiptInspection deleted successfully!");
    }


}
