package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsInspectionResponseDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.service.GoodsInspectionService;
import com.astro.service.GprnService;
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

    @Autowired
    private GprnService gprnService;


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



    @GetMapping("/{goodsInspectionNo}")
    public ResponseEntity<Object> getGoodsInspectionById(@PathVariable String goodsInspectionNo) {
        GoodsInspectionResponseDto inspection = goodsInspectionService.getGoodsInspectionById(goodsInspectionNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(inspection), HttpStatus.OK);
    }


    // Update an existing Goods Inspection by ID
    @PutMapping("/{goodsInspectionNo}")
    public ResponseEntity<Object> updateGoodsInspection(@PathVariable String goodsInspectionNo, @RequestBody GoodsInspectionRequestDto goodsInspectionDto) {
        GoodsInspectionResponseDto updatedInspection = goodsInspectionService.updateGoodsInspection(goodsInspectionNo, goodsInspectionDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updatedInspection), HttpStatus.OK);
    }

    // Delete a Goods Inspection by ID
    @DeleteMapping("/{goodsInspectionNo}")
    public ResponseEntity<String> deleteGoodsInspection(@PathVariable String goodsInspectionNo) {
        goodsInspectionService.deleteGoodsInspection(goodsInspectionNo);
        return ResponseEntity.ok("GoodsInspection deleted successfully."+" " +goodsInspectionNo);
    }
    //fetch all details from the gprn
    @GetMapping("/gprn/{gprnId}")
    public ResponseEntity<Object> fetchGprDetails(@PathVariable String gprnId) {
        GprnResponseDto gprnDetails = gprnService.getGprnById(gprnId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprnDetails), HttpStatus.OK);
    }

}
