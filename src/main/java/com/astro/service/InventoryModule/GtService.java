package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.GoodsTransfer.GtMasterDto;

public interface GtService {
    public String createGt(GtMasterDto gtMasterDto);
    public List<GtMasterDto> getPendingGt();
    public void approveGt(String gtId);
    public void rejectGt(String gtId);
}
