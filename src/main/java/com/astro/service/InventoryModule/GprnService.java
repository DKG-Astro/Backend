package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;

import java.util.List;

public interface GprnService {
    String saveGprn(SaveGprnDto req);
    SaveGprnDto getGprnDtls(String processNo);
    void validateGprnSubProcessId(String processNo);

    public List<String> getPendingGprn();
}