package com.astro.controller;



import com.astro.dto.workflow.MaterialMasterRequestDto;
import com.astro.dto.workflow.MaterialMasterResponseDto;
import com.astro.service.MaterialMasterService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-master")
public class MaterialMasterController {

    @Autowired
    private MaterialMasterService materialMasterService;


    @PostMapping
    public ResponseEntity<Object> createMaterialMaster(@RequestBody MaterialMasterRequestDto requestDTO) {
        MaterialMasterResponseDto material = materialMasterService.createMaterialMaster(requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(material), HttpStatus.OK);
    }

    @PutMapping("/{materialCode}")
    public ResponseEntity<Object> updateMaterialMaster(@PathVariable String materialCode,
                                                 @RequestBody MaterialMasterRequestDto requestDTO) {
        MaterialMasterResponseDto response = materialMasterService.updateMaterialMaster(materialCode, requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllMaterialMaster() {
        List<MaterialMasterResponseDto> response = materialMasterService.getAllMaterialMasters();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("/{materialCode}")
    public ResponseEntity<Object> getMaterialMasterById(@PathVariable String materialCode) {
        MaterialMasterResponseDto responseDTO = materialMasterService.getMaterialMasterById(materialCode);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{materialCode}")
    public ResponseEntity<String> deleteMaterialMaster(@PathVariable String materialCode) {
       materialMasterService.deleteMaterialMaster(materialCode);
        return ResponseEntity.ok("Work Order deleted successfully. materialCode:"+" " +materialCode);
    }

}
