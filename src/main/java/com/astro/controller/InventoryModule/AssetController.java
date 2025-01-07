package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.AssetDTO;
import com.astro.entity.InventoryModule.Asset;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;
import com.astro.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;


    // Create new asset
    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody AssetDTO assetDTO) {
       Asset asset = assetService.createAsset(assetDTO);
        return ResponseEntity.ok(asset);
    }

    // Update asset
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody AssetDTO assetDTO) {
       Asset asset = assetService.updateAsset(id, assetDTO);
        return ResponseEntity.ok(asset);

    }

    // Get all assets
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        List<Asset> assets= assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    // Get asset by id
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        Asset asset = assetService.getAssetById(id);
        return ResponseEntity.ok(asset);
    }

    // Delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Goods Return deleted successfully!");
    }



}
