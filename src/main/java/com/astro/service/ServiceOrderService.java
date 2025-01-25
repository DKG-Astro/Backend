package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderResponseDTO;

import java.util.List;

public interface ServiceOrderService {

    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO serviceOrderRequestDTO);


    public ServiceOrderResponseDTO updateServiceOrder(String soId, ServiceOrderRequestDTO serviceOrderRequestDTO);
    public List<ServiceOrderResponseDTO> getAllServiceOrders();
    public ServiceOrderResponseDTO getServiceOrderById(String soId);
    public void deleteServiceOrder(String soId);

}
