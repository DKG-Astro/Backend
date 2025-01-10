package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderResponseDTO;
import com.astro.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrder;
    @PostMapping
    public ResponseEntity<WorkOrderResponseDTO> createWorkOrder(@RequestBody WorkOrderRequestDTO requestDTO) {
        WorkOrderResponseDTO responseDTO = workOrder.createWorkOrder(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDTO> updateWorkOrder(@PathVariable Long id,
                                                                      @RequestBody WorkOrderRequestDTO requestDTO) {
        WorkOrderResponseDTO response = workOrder.updateWorkOrder(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<WorkOrderResponseDTO>> getAllWorkOrders() {
        List<WorkOrderResponseDTO> response = workOrder.getAllWorkOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDTO> getWorkOrderById(@PathVariable Long id) {
        WorkOrderResponseDTO responseDTO = workOrder.getWorkOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkOrder(@PathVariable Long id) {
        workOrder.deleteWorkOrder(id);
        return ResponseEntity.ok("Work Order deleted successfully. Id:"+" " +id);
    }


}
