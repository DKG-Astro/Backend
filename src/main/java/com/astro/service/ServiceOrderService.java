package com.astro.service;

import com.astro.dto.workflow.SreviceOrderDto.ServiceOrderRequestDTO;
import com.astro.dto.workflow.SreviceOrderDto.ServiceOrderResponseDTO;

import java.util.List;

public interface ServiceOrderService {

    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO serviceOrderRequestDTO);


    public ServiceOrderResponseDTO updateServiceOrder(Long id, ServiceOrderRequestDTO serviceOrderRequestDTO);
    public List<ServiceOrderResponseDTO> getAllServiceOrders();
    public ServiceOrderResponseDTO getServiceOrderById(Long id);
    public void deleteServiceOrder(Long id);

}
