package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GprnDto.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;

import java.util.List;


public interface GprnService {


   //public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto, String provisionalReceiptCertificateFileName,String photoFileName);
 public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto);
    GprnResponseDto updateGprn(String gprnId, GprnRequestDto gprnRequestDto,String provisionalReceiptCertificateFileName, String photoFileName);


    List<GprnResponseDto> getAllGprn();
    GprnResponseDto getGprnById(String gprnId);
    void deleteGprn(String gprnId);




}
