package com.astro.service;


import com.astro.dto.workflow.InventoryModule.GprnDto.GetGprnDtlsDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;

public interface ProcessService {
    public String saveGprn(SaveGprnDto saveGprnDto);

    public Object getSubProcessDtls(String processStage, String processId);
}
