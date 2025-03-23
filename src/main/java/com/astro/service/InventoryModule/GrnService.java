package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import java.util.Map;

public interface GrnService {
    String saveGrn(GrnDto req);
    Map<String, Object> getGrnDtls(String processNo);
}