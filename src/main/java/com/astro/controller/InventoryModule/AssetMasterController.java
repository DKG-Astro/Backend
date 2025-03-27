package com.astro.controller.InventoryModule;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.astro.service.InventoryModule.AssetMasterService;
import com.astro.util.ResponseBuilder;
import com.astro.dto.workflow.InventoryModule.AssetDisposalDto;
import com.astro.dto.workflow.InventoryModule.AssetMasterDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/asset")
public class AssetMasterController {

    @Autowired
    private AssetMasterService assetMasterService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveAssetMaster(@RequestBody AssetMasterDto request) {
        String res = assetMasterService.saveAssetMaster(request);
        Map<String, Integer> response = new HashMap<>();
        response.put("processNo", Integer.parseInt(res));
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity<Object> updateAssetMaster(@RequestBody AssetMasterDto request) {
        String res = assetMasterService.updateAssetMaster(request);
        Map<String, Integer> response = new HashMap<>();
        response.put("processNo", Integer.parseInt(res));
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    // @PutMapping("/update")
    // public String updateAssetMaster(@RequestBody AssetMasterDto request) {
    //     return assetMasterService.updateAssetMaster(request);
    // }

    @PostMapping("/dispose")
    public ResponseEntity<Object> dispose(@RequestBody AssetDisposalDto req) {
        String res = assetMasterService.saveAssetDisposal(req);
        Map<String, String> response = new HashMap<>();
        response.put("processNo", res);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
        
        
    }
    
    @GetMapping("/getAssetDtl")
    public ResponseEntity<Object> getAssetDetails(@RequestParam("assetId") Integer assetId) {
        AssetMasterDto response = assetMasterService.getAssetDetails(assetId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
}