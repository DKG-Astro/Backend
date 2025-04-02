package com.astro.controller;

import com.astro.dto.workflow.*;
import com.astro.service.MaterialMasterUtilService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-master-util")
public class MaterialMasterUtilController {

    @Autowired
    private MaterialMasterUtilService materialMasterUtilService;

    @PostMapping("/register")
    public ResponseEntity<Object> createMaterial(@RequestBody MaterialMasterUtilRequestDto dto) {
       MaterialMasterUtilResponseDto material= materialMasterUtilService.createMaterial(dto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(material), HttpStatus.OK);
    }


    @GetMapping("/material-awaiting-approval")
    public ResponseEntity<Object>  getAllAwaitingApprovalmaterials() {
        List<MaterialMasterUtilResponseDto> materials = materialMasterUtilService.getAllAwaitingApprovalMaterials();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(materials), HttpStatus.OK);

    }

    @GetMapping("/material-changeRequest")
    public ResponseEntity<Object>  getAllChangeRequest() {
        List<MaterialMasterUtilResponseDto> materials = materialMasterUtilService.getAllChangeRequestMaterials();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(materials), HttpStatus.OK);

    }

    @PostMapping("/performActionForMaterial")
    public ResponseEntity<Object> approveOrRejectMaterial(@RequestBody ApprovalAndRejectionRequestDTO request) {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(materialMasterUtilService.performActionForMaterial(request)), HttpStatus.OK);

    }

    @GetMapping("/MaterialTransitionHistory/{materialCode}")
    public ResponseEntity<Object>  getMatrialHistory(String materialCode ) {
        List<MaterialTransitionHistory> materials = materialMasterUtilService.getMaterialStatusByCode(materialCode);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(materials), HttpStatus.OK);

    }

    @PutMapping("/update/{materialCode}")
    public ResponseEntity<Object> updateMaterialMasterUtil(
            @PathVariable String materialCode,@RequestBody MaterialMasterUtilRequestDto dto){
        MaterialMasterUtilResponseDto updated = materialMasterUtilService.updateMaterialMasterUtil(materialCode,dto);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }

    @GetMapping("/{materialCode}")
    public ResponseEntity<Object> getMaterialMasterUtilById(@PathVariable String materialCode) {
        MaterialMasterUtilResponseDto response = materialMasterUtilService.getMaterialMasterUtilById(materialCode);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

}
