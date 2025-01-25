package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.AssetRequestDTO;
import com.astro.dto.workflow.InventoryModule.AssetResponseDto;
import com.astro.entity.InventoryModule.Asset;
import com.astro.service.AssetService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> createAsset(@RequestBody AssetRequestDTO assetDTO) {
       AssetResponseDto asset = assetService.createAsset(assetDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(asset), HttpStatus.OK);
    }

    // Update asset
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAsset(@PathVariable Long id, @RequestBody AssetRequestDTO assetDTO) {
       AssetResponseDto asset = assetService.updateAsset(id, assetDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(asset), HttpStatus.OK);

    }

    // Get all assets
    @GetMapping
    public ResponseEntity<Object> getAllAssets() {
        List<AssetResponseDto> assets= assetService.getAllAssets();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(assets), HttpStatus.OK);
    }

    // Get asset by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAssetById(@PathVariable Long id) {
        AssetResponseDto asset = assetService.getAssetById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(asset), HttpStatus.OK);
    }

    // Delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Asset deleted successfully!");
    }



}
