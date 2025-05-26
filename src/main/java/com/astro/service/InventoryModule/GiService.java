package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;
import com.astro.dto.workflow.InventoryModule.gprn.GprnPendingInspectionDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiService {
    String saveGi(SaveGiDto req);
    Map<String, Object> getGiDtls(String processNo);
    void validateGiSubProcessId(String processNo);
    public List<GprnPendingInspectionDto> getGiStatusWise(String status, Optional<String> createdBy);
    // public void approveGi(String processNo);
    // public void rejectGi(String processNo);
}