package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoResponseDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpReportDto;

public interface OgpService {
    public String saveOgp(OgpDto req);

    public OgpDto getOgpDtls(String processNo);

    public List<OgpReportDto> getOgpReport(String startDate, String endDate);

    public String savePoOgp(OgpPoDto request);

    public OgpPoResponseDto getPoOgp(String processNo);
}
