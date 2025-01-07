package com.astro.service;

import com.astro.dto.workflow.InventoryModule.AssetDTO;
import com.astro.entity.InventoryModule.Asset;

import java.util.List;

public interface AssetService {

    public Asset createAsset(AssetDTO assetDTO);
    public Asset updateAsset(Long id, AssetDTO assetDTO);
    public List<Asset> getAllAssets();

    public Asset getAssetById(Long id);


    public void deleteAsset(Long id);

}
