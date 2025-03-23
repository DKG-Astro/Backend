package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;

public interface OgpService {
    public String saveOgp(OgpDto req);

    public OgpDto getOgpDtls(String processNo);
}
