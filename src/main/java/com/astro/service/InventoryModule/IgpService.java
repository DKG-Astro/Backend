package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.igp.IgpDetailReportDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpReportDto;

public interface IgpService {
    String saveIgp(IgpDto req);
    IgpDto getIgpDtls(String processNo);
    List<IgpReportDto> getIgpReport(String startDate, String endDate);
    List<IgpDetailReportDto> getIgpDetails();
}