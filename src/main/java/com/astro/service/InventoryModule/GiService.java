package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;
import java.util.Map;

public interface GiService {
    String saveGi(SaveGiDto req);
    Map<String, Object> getGiDtls(String processNo);
    void validateGiSubProcessId(String processNo);
}