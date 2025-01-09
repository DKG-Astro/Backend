package com.astro.controller;


import com.astro.dto.workflow.SreviceOrderDto.ServiceOrderRequestDTO;
import com.astro.dto.workflow.SreviceOrderDto.ServiceOrderResponseDTO;
import com.astro.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {


    @Autowired
    private ServiceOrderService serviceOrder;
    @PostMapping
    public ResponseEntity<ServiceOrderResponseDTO> createServiceOrder(@RequestBody ServiceOrderRequestDTO requestDTO) {
        ServiceOrderResponseDTO responseDTO = serviceOrder.createServiceOrder(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> updateServiceOrder(@PathVariable Long id,
                                                                  @RequestBody ServiceOrderRequestDTO requestDTO) {
        ServiceOrderResponseDTO responseDTO = serviceOrder.updateServiceOrder(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    @GetMapping
    public ResponseEntity<List<ServiceOrderResponseDTO>> getAllServiceOrders() {
        List<ServiceOrderResponseDTO> responseDTOList = serviceOrder.getAllServiceOrders();
        return ResponseEntity.ok(responseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> getServiceOrderById(@PathVariable Long id) {
        ServiceOrderResponseDTO responseDTO = serviceOrder.getServiceOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceOrder(@PathVariable Long id) {
        serviceOrder.deleteServiceOrder(id);
        return ResponseEntity.ok("Service Order deleted successfully. Id:"+" " +id);
    }


}
