package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.GoodsTransfer.GtMasterDto;
import com.astro.dto.workflow.InventoryModule.GtMasterResponseDto;

public interface GtService {
    public String createGt(GtMasterDto gtMasterDto);
    public List<GtMasterDto> getPendingGt();
    public void approveGt(String gtId);
    public void rejectGt(String gtId);
    public GtMasterResponseDto getGtById(String gtId);
    public GtMasterDto getGtDtls(String gtId);
    public void approveGtFromOgp(String gtId);
}
