package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;

import java.util.List;

public interface GprnService {
    String saveGprn(SaveGprnDto req);
    SaveGprnDto getGprnDtls(String processNo);
    void validateGprnSubProcessId(String processNo);

    public List<String> getPendingGprn();
    public void rejectGprn(String processNo);
    public void approveGprn(String processNo);
    public void changeReqGprn(String processNo);
    public void updateGprn(SaveGprnDto updateRequest);
}