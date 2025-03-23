package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.igp.IgpDto;

public interface IgpService {
    String saveIgp(IgpDto req);
    IgpDto getIgpDtls(String processNo);
}