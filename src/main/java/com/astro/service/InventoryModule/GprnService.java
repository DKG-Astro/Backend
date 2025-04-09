package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;

public interface GprnService {
    String saveGprn(SaveGprnDto req);
    SaveGprnDto getGprnDtls(String processNo);
    void validateGprnSubProcessId(String processNo);
    public List<String> getPendingGprn();
}