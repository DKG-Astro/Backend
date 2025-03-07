package com.astro.service;


import com.astro.dto.workflow.InventoryModule.GprnDto.getGprnDtlsDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.saveGprnDto;

public interface ProcessService {


    saveGprnDto saveGprn(saveGprnDto saveGprnDto);

    getGprnDtlsDto getSubProcessDtls(String processStage, String processId);
}
