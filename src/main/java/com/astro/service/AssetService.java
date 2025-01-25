package com.astro.service;

import com.astro.dto.workflow.InventoryModule.AssetRequestDTO;
import com.astro.dto.workflow.InventoryModule.AssetResponseDto;
import com.astro.entity.InventoryModule.Asset;

import java.util.List;

public interface AssetService {

    public AssetResponseDto createAsset(AssetRequestDTO assetDTO);
    public AssetResponseDto updateAsset(Long id, AssetRequestDTO assetDTO);
    public List<AssetResponseDto> getAllAssets();

    public AssetResponseDto getAssetById(Long id);


    public void deleteAsset(Long id);

}
