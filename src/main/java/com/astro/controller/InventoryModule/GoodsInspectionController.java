package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsInspectionResponseDto;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.service.GoodsInspectionService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-inspections")
public class GoodsInspectionController {

    @Autowired
    private GoodsInspectionService goodsInspectionService;



    // Create a new Goods Inspection
    @PostMapping
    public ResponseEntity<Object> createGoodsInspection(@RequestBody GoodsInspectionRequestDto goodsInspectionDto) {
        GoodsInspectionResponseDto createdInspection = goodsInspectionService.createGoodsInspection(goodsInspectionDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(createdInspection), HttpStatus.OK);
    }
    // Get all Goods Inspections
    @GetMapping
    public ResponseEntity<Object> getAllGoodsInspections() {
        List<GoodsInspectionResponseDto> inspections = goodsInspectionService.getAllGoodsInspections();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(inspections), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getGoodsInspectionById(@PathVariable Long id) {
        GoodsInspectionResponseDto inspection = goodsInspectionService.getGoodsInspectionById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(inspection), HttpStatus.OK);
    }


    // Update an existing Goods Inspection by ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGoodsInspection(@PathVariable Long id, @RequestBody GoodsInspectionRequestDto goodsInspectionDto) {
        GoodsInspectionResponseDto updatedInspection = goodsInspectionService.updateGoodsInspection(id, goodsInspectionDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updatedInspection), HttpStatus.OK);
    }

    // Delete a Goods Inspection by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsInspection(@PathVariable Long id) {
        goodsInspectionService.deleteGoodsInspection(id);
        return ResponseEntity.ok("GoodsInspection deleted successfully."+" " +id);
    }
}
