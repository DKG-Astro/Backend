package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GprnDto.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderResponseDTO;
import com.astro.service.GprnService;
import com.astro.service.PurchaseOrderService;
import com.astro.service.impl.GprnServiceImpl;
import com.astro.util.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gprns")
public class GprnController {

    @Autowired
    private GprnService gprnService;

    @Autowired
    private PurchaseOrderService poService;
    private static final Logger log = LoggerFactory.getLogger(GprnServiceImpl.class);
    @PostMapping
    public ResponseEntity<Object> createGprn(@RequestBody GprnRequestDto gprnRequestDto) {
        GprnResponseDto savedGprn = gprnService.createGprnWithMaterialDetails(gprnRequestDto);
      //  log.info("Received GPRN Request: {}", gprnRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(savedGprn), HttpStatus.OK);
    }

    @PutMapping("/{gprnId}")
    public ResponseEntity<Object> updateGprnById(
            @PathVariable Long gprnId, @RequestBody GprnRequestDto gprnRequestDto) {
        GprnResponseDto gprn =gprnService.updateGprn(gprnId,gprnRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprn), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Object> getAllGprn() {
        List<GprnResponseDto> gprns=gprnService.getAllGprn();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprns), HttpStatus.OK);
    }

    @GetMapping("/{gprnId}")
    public ResponseEntity<Object> getGprnById(@PathVariable Long gprnId) {
        GprnResponseDto gprn = gprnService.getGprnById(gprnId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprn), HttpStatus.OK);
    }

    @DeleteMapping("/{gprnId}")
    public ResponseEntity<String> deleteGprn(@PathVariable Long gprnId) {
        gprnService.deleteGprn(gprnId);
        return ResponseEntity.ok("Gprn deleted successfully!");
    }


    @GetMapping("/purchase-orders/{poId}")
    public ResponseEntity<Object> fetchPurchaseOrderDetails(@PathVariable String poId) {
        PurchaseOrderResponseDTO po = poService.getPurchaseOrderById(poId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(po), HttpStatus.OK);
    }



}
