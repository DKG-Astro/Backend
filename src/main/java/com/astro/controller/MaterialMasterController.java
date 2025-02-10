package com.astro.controller;



import com.astro.dto.workflow.MaterialMasterRequestDto;
import com.astro.dto.workflow.MaterialMasterResponseDto;
import com.astro.service.MaterialMasterService;
import com.astro.util.ResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/material-master")
public class MaterialMasterController {

    @Autowired
    private MaterialMasterService materialMasterService;

    @Autowired
    private ObjectMapper mapper;
    @PostMapping
    public ResponseEntity<Object> createMaterialMaster(
        @RequestPart("materialMasterRequestDTO") String materialMasterRequestDTO,
        @RequestPart(value = "uploadImage") MultipartFile uploadImage) throws JsonProcessingException {

        MaterialMasterRequestDto materialMasterRequestDto = mapper.readValue(materialMasterRequestDTO, MaterialMasterRequestDto.class);
    materialMasterRequestDto.setUploadImage(uploadImage);

    String uploadImageFileName = uploadImage.getOriginalFilename();

    MaterialMasterResponseDto created = materialMasterService.createMaterialMaster(
            materialMasterRequestDto, uploadImageFileName);

    // Return success response
    return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
}


    @PutMapping("/{materialCode}")
    public ResponseEntity<Object> updateMaterialMaster(
            @PathVariable String materialCode,
            @RequestPart("materialMasterRequestDTO") String materialMasterRequestDTO,
            @RequestPart(value = "uploadImage") MultipartFile uploadImage) throws JsonProcessingException {
        MaterialMasterRequestDto materialMasterRequestDto = mapper.readValue(materialMasterRequestDTO,MaterialMasterRequestDto.class);
            materialMasterRequestDto.setUploadImage(uploadImage);

        String uploadImageFileName = uploadImage != null ? uploadImage.getOriginalFilename() : null;

        MaterialMasterResponseDto updated = materialMasterService.updateMaterialMaster(
                materialCode, materialMasterRequestDto, uploadImageFileName);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
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
        return ResponseEntity.ok("material master deleted successfully. materialCode:"+" " +materialCode);
    }

}
