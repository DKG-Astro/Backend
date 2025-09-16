package com.astro.service.InventoryModule;

import java.util.List;

import com.astro.dto.workflow.InventoryModule.AssetDisposalDto;
import com.astro.dto.workflow.InventoryModule.AssetDisposalReportDto;
import com.astro.dto.workflow.InventoryModule.AssetMasterDto;
import com.astro.dto.workflow.InventoryModule.asset.AssetMasterReportDto;
import com.astro.dto.workflow.InventoryModule.asset.AssetOhqDisposalDto;
import com.astro.entity.InventoryModule.AssetDisposalDetailEntity;
import com.astro.entity.InventoryModule.OhqConsumableStoreStockEntity;
import com.astro.entity.InventoryModule.OhqMasterConsumableEntity;
import com.astro.entity.InventoryModule.OhqMasterEntity;

public interface AssetMasterService {
    String saveAssetMaster(AssetMasterDto request);
    String updateAssetMaster(AssetMasterDto request);
    public String saveAssetDisposal(AssetDisposalDto request);
    AssetMasterDto getAssetDetails(Integer assetId);
    public List<AssetMasterReportDto> getAssetReport();
    List<Integer> getAllAssetIds();
    public List<OhqMasterEntity> getAssetOhqList();
    public List<OhqMasterConsumableEntity> getAssetOhqConsumableList();
    public List<OhqConsumableStoreStockEntity> getStoreStockOhqConsumableList();
    public List<AssetOhqDisposalDto> getAllAssetsForDisposal();
    public List<AssetDisposalDto> getAllAssetDisposalAwaitingForApproval();
    public void approveDisposal(String disposalIdStr);
    public void rejectDisposal(String disposalIdStr);
    public AssetDisposalDto getAssetDisposalById(String disposalIdStr);
    public String updateAssetDisposal(AssetDisposalDto request);
    public List<AssetDisposalDto> getAllApprovedAssetDisposalReport();
    public List<AssetDisposalReportDto> getAssetDisposalReport(String startDate, String endDate);
}