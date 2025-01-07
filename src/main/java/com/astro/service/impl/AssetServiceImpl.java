package com.astro.service.impl;

import com.astro.dto.workflow.InventoryModule.AssetDTO;
import com.astro.entity.InventoryModule.Asset;
import com.astro.repository.InventoryModule.AssetRepository;
import com.astro.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;



    public Asset createAsset(AssetDTO assetDTO) {
        Asset asset = new Asset();
        asset.setAssetCode(assetDTO.getAssetCode());
        asset.setMaterialCode(assetDTO.getMaterialCode());
        asset.setDescription(assetDTO.getDescription());
        asset.setUom(assetDTO.getUom());
        asset.setMakeNo(assetDTO.getMakeNo());
        asset.setModelNo(assetDTO.getModelNo());
        asset.setSerialNo(assetDTO.getSerialNo());
        asset.setComponentName(assetDTO.getComponentName());
        asset.setComponentCode(assetDTO.getComponentCode());
        asset.setQuantity(assetDTO.getQuantity());
        asset.setLocator(assetDTO.getLocator());
        asset.setTransactionHistory(assetDTO.getTransactionHistory());
        asset.setCurrentCondition(assetDTO.getCurrentCondition());

        return assetRepository.save(asset);
    }

    public Asset updateAsset(Long id, AssetDTO assetDTO) {
        Asset asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));

        asset.setAssetCode(assetDTO.getAssetCode());
        asset.setMaterialCode(assetDTO.getMaterialCode());
        asset.setDescription(assetDTO.getDescription());
        asset.setUom(assetDTO.getUom());
        asset.setMakeNo(assetDTO.getMakeNo());
        asset.setModelNo(assetDTO.getModelNo());
        asset.setSerialNo(assetDTO.getSerialNo());
        asset.setComponentName(assetDTO.getComponentName());
        asset.setComponentCode(assetDTO.getComponentCode());
        asset.setQuantity(assetDTO.getQuantity());
        asset.setLocator(assetDTO.getLocator());
        asset.setTransactionHistory(assetDTO.getTransactionHistory());
        asset.setCurrentCondition(assetDTO.getCurrentCondition());

        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

}
