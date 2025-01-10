package com.astro.controller.ProcurementModuleController;



import com.astro.dto.workflow.ProcurementDtos.IndentDto.purchaseOrder.PurchaseOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.purchaseOrder.PurchaseOrderResponseDTO;

import com.astro.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrderResponseDTO createdPO = poService.createPurchaseOrder(purchaseOrderRequestDTO);
        return ResponseEntity.ok(createdPO);
    }





    @PutMapping("/{poId}")
    public ResponseEntity< PurchaseOrderResponseDTO> updatePurchaseOrder(
            @PathVariable Long poId,
            @RequestBody @Valid PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrderResponseDTO  updatedPO = poService.updatePurchaseOrder(poId, purchaseOrderRequestDTO);
        return ResponseEntity.ok(updatedPO);
    }
  // Get all POs
    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDTO>> getAllPurchaseOrders() {
        List<PurchaseOrderResponseDTO> poList = poService.getAllPurchaseOrders();
        return ResponseEntity.ok(poList);
    }




    // Get a PO by ID
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDTO> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrderResponseDTO  po = poService.getPurchaseOrderById(id);
        return ResponseEntity.ok(po);
    }

    // Delete a PO by ID
    @DeleteMapping("/{poId}")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable Long poId) {
        poService.deletePurchaseOrder(poId);
        return ResponseEntity.ok("Purchase Order deleted successfully."+" " +poId);
    }




}
