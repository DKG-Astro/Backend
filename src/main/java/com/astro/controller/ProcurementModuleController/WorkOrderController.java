package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.WorkOrderDto.WorkOrderResponseDTO;
import com.astro.service.WorkOrderService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrder;
    @PostMapping
    public ResponseEntity<Object> createWorkOrder(@RequestBody WorkOrderRequestDTO requestDTO) {
        WorkOrderResponseDTO responseDTO = workOrder.createWorkOrder(requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @PutMapping("/{woId}")
    public ResponseEntity<Object> updateWorkOrder(@PathVariable String woId,
                                                                      @RequestBody WorkOrderRequestDTO requestDTO) {
        WorkOrderResponseDTO response = workOrder.updateWorkOrder(woId, requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllWorkOrders() {
        List<WorkOrderResponseDTO> response = workOrder.getAllWorkOrders();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("/{woId}")
    public ResponseEntity<Object> getWorkOrderById(@PathVariable String woId) {
        WorkOrderResponseDTO responseDTO = workOrder.getWorkOrderById(woId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{woId}")
    public ResponseEntity<String> deleteWorkOrder(@PathVariable String woId) {
        workOrder.deleteWorkOrder(woId);
        return ResponseEntity.ok("Work Order deleted successfully. Id:"+" " +woId);
    }


}
