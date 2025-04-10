package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpDto;
import com.astro.dto.workflow.InventoryModule.isn.IsnDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;
import com.astro.dto.workflow.InventoryModule.ohq.OhqReportDto;
import com.astro.entity.InventoryModule.IsnAssetOhqDtlsDto;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;

public interface ProcessService {
    public String saveGprn(SaveGprnDto saveGprnDto);

    public String saveGi(SaveGiDto saveGiDto);

    public String saveGrv(GrvDto req);

    public String saveGrn(GrnDto req);

    public String saveIsn(IsnDto req);

    public String saveOgp(OgpDto req);

    public String saveIgp(IgpDto req);

    public List<IsnAssetOhqDtlsDto> getIsnAssetOhqDtls();

    public Object getSubProcessDtls(String processStage, String processId);

public List<OhqReportDto> getOhqReport();
    public List<String> getPendingGprn();
}
