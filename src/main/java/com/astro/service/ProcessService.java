package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvDto;
import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;

public interface ProcessService {
    public String saveGprn(SaveGprnDto saveGprnDto);

    public String saveGi(SaveGiDto saveGiDto);

    public String saveGrv(GrvDto req);

    public String saveGrn(GrnDto req);

    public Object getSubProcessDtls(String processStage, String processId);
}
