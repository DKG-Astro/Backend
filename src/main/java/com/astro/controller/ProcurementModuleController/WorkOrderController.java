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

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateWorkOrder(@PathVariable Long id,
                                                                      @RequestBody WorkOrderRequestDTO requestDTO) {
        WorkOrderResponseDTO response = workOrder.updateWorkOrder(id, requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllWorkOrders() {
        List<WorkOrderResponseDTO> response = workOrder.getAllWorkOrders();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getWorkOrderById(@PathVariable Long id) {
        WorkOrderResponseDTO responseDTO = workOrder.getWorkOrderById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkOrder(@PathVariable Long id) {
        workOrder.deleteWorkOrder(id);
        return ResponseEntity.ok("Work Order deleted successfully. Id:"+" " +id);
    }


}
