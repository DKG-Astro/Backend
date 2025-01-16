package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnResponseDto;

import java.util.List;


public interface GprnService {


    public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto);


    GprnResponseDto updateGprn(Long id, GprnRequestDto gprnRequestDto);
    List<GprnResponseDto> getAllGprn();
    GprnResponseDto getGprnById(Long id);
    void deleteGprn(Long id);


}
