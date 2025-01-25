package com.astro.service;

import com.astro.dto.workflow.MaterialMasterRequestDto;
import com.astro.dto.workflow.MaterialMasterResponseDto;

import java.util.List;

public interface MaterialMasterService {

    public MaterialMasterResponseDto createMaterialMaster(MaterialMasterRequestDto materialMasterRequestDto);
    public MaterialMasterResponseDto updateMaterialMaster(String materialCode, MaterialMasterRequestDto materialMasterRequestDto);
    public List<MaterialMasterResponseDto> getAllMaterialMasters();

    public MaterialMasterResponseDto getMaterialMasterById(String materialCode);
    public void deleteMaterialMaster(String materialCode);
}
