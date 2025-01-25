package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GprnDto.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;

import java.util.List;


public interface GprnService {


    public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto);
    GprnResponseDto updateGprn(Long gprnId, GprnRequestDto gprnRequestDto);


    List<GprnResponseDto> getAllGprn();
    GprnResponseDto getGprnById(Long gprnId);
    void deleteGprn(Long gprnId);




}
