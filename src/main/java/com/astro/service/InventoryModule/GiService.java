package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;
import com.astro.dto.workflow.InventoryModule.gprn.GprnPendingInspectionDto;

import java.util.List;
import java.util.Map;

public interface GiService {
    String saveGi(SaveGiDto req);
    Map<String, Object> getGiDtls(String processNo);
    void validateGiSubProcessId(String processNo);
    public List<GprnPendingInspectionDto> getPendingGi();
}