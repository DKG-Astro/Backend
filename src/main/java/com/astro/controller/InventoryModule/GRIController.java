package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsInspectionResponseDto;
import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionRequestDto;
import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionResponseDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderResponseDTO;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;
import com.astro.service.GoodsInspectionService;
import com.astro.service.GoodsReceiptInspectionService;
import com.astro.service.GprnService;
import com.astro.service.PurchaseOrderService;
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
    @Autowired
    private PurchaseOrderService poService;

    @Autowired
    private GprnService gprnService;


    @Autowired
    private GoodsInspectionService goodsInspectionService;
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


    @GetMapping("/purchase-orders/{id}")
    public ResponseEntity<Object> fetchPurchaseOrderDetails(@PathVariable Long id) {
        PurchaseOrderResponseDTO po = poService.getPurchaseOrderById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(po), HttpStatus.OK);
    }

    @GetMapping("/gprn/{gprnId}")
    public ResponseEntity<Object> fetchGprDetails(@PathVariable Long gprnId) {
        GprnResponseDto gprnDetails = gprnService.getGprnById(gprnId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprnDetails), HttpStatus.OK);
    }

    @GetMapping("/goodsInspection/{goodsInspectionNo}")
    public ResponseEntity<Object> fetchGoodsInspectionDetails(@PathVariable Long goodsInspectionNo) {
        GoodsInspectionResponseDto inspection = goodsInspectionService.getGoodsInspectionById(goodsInspectionNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(inspection), HttpStatus.OK);
    }




}
