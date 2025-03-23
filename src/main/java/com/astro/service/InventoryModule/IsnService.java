package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.isn.IsnDto;
import com.astro.entity.InventoryModule.IsnAssetOhqDtlsDto;

public interface IsnService {
    String saveIsn(IsnDto req);
    IsnDto getIsnDtls(String processNo);
    List<IsnAssetOhqDtlsDto> getAssetMasterAndOhqDtls();
}