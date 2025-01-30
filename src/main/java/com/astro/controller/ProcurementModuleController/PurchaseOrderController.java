package com.astro.controller.ProcurementModuleController;



import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderResponseDTO;

import com.astro.service.PurchaseOrderService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService poService;

    // Create a new PO
    @PostMapping
    public ResponseEntity<Object> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrderResponseDTO createdPO = poService.createPurchaseOrder(purchaseOrderRequestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(createdPO), HttpStatus.OK);
    }

    @PutMapping("/{poId}")
    public ResponseEntity<Object> updatePurchaseOrder(
            @PathVariable String poId,
            @RequestBody @Valid PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrderResponseDTO  updatedPO = poService.updatePurchaseOrder(poId, purchaseOrderRequestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updatedPO), HttpStatus.OK);
    }
  // Get all POs
    @GetMapping
    public ResponseEntity<Object> getAllPurchaseOrders() {
        List<PurchaseOrderResponseDTO> poList = poService.getAllPurchaseOrders();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(poList), HttpStatus.OK);
    }




    // Get a PO by ID
    @GetMapping("/{poId}")
    public ResponseEntity<Object> getPurchaseOrderById(@PathVariable String poId) {
        PurchaseOrderResponseDTO  po = poService.getPurchaseOrderById(poId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(po), HttpStatus.OK);
    }

    // Delete a PO by ID
    @DeleteMapping("/{poId}")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable String poId) {
        poService.deletePurchaseOrder(poId);
        return ResponseEntity.ok("Purchase Order deleted successfully."+" " +poId);
    }




}
