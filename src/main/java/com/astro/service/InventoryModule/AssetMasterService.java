package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.AssetDisposalDto;
import com.astro.dto.workflow.InventoryModule.AssetMasterDto;

public interface AssetMasterService {
    String saveAssetMaster(AssetMasterDto request);
    String updateAssetMaster(AssetMasterDto request);
    public String saveAssetDisposal(AssetDisposalDto request);
    AssetMasterDto getAssetDetails(Integer assetId);
    public List<AssetMasterDto> getAssetReport();
    List<Integer> getAllAssetIds();
}