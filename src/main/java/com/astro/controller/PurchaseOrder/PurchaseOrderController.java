package com.astro.controller.PurchaseOrder;


import com.astro.dto.workflow.purchaseOrder.PurchaseOrderDto;
import com.astro.entity.PurchaseOrder;
import com.astro.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService poService;

    // Create a new PO
    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderDto poDto) {
        PurchaseOrder createdPO = poService.createPurchaseOrder(poDto);
        return ResponseEntity.ok(createdPO);
    }

    // Get all POs
    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        List<PurchaseOrder> poList = poService.getAllPurchaseOrders();
        return ResponseEntity.ok(poList);
    }

    // Update a PO by ID
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
            @PathVariable Long id,
            @RequestBody PurchaseOrderDto poDto) {
        PurchaseOrder updatedPO = poService.updatePurchaseOrder(id, poDto);
        return ResponseEntity.ok(updatedPO);
    }

    // Get a PO by ID
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrder po = poService.getPurchaseOrderById(id);
        return ResponseEntity.ok(po);
    }

    // Delete a PO by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable Long id) {
        poService.deletePurchaseOrder(id);
        return ResponseEntity.ok("Purchase Order deleted successfully."+" " +id);
    }


}
