package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderResponseDTO;
import com.astro.service.ServiceOrderService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {


    @Autowired
    private ServiceOrderService serviceOrder;
    @PostMapping
    public ResponseEntity<Object> createServiceOrder(@RequestBody ServiceOrderRequestDTO requestDTO) {
        ServiceOrderResponseDTO responseDTO = serviceOrder.createServiceOrder(requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateServiceOrder(@PathVariable Long id,
                                                                  @RequestBody ServiceOrderRequestDTO requestDTO) {
        ServiceOrderResponseDTO responseDTO = serviceOrder.updateServiceOrder(id, requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllServiceOrders() {
        List<ServiceOrderResponseDTO> responseDTOList = serviceOrder.getAllServiceOrders();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTOList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getServiceOrderById(@PathVariable Long id) {
        ServiceOrderResponseDTO responseDTO = serviceOrder.getServiceOrderById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceOrder(@PathVariable Long id) {
        serviceOrder.deleteServiceOrder(id);
        return ResponseEntity.ok("Service Order deleted successfully. Id:"+" " +id);
    }


}
