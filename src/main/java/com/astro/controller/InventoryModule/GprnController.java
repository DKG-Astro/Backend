package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GprnDto.GprnMaterialsRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.GprnResponseDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderResponseDTO;
import com.astro.service.GprnService;
import com.astro.service.PurchaseOrderService;
import com.astro.service.impl.GprnServiceImpl;
import com.astro.util.ResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gprns")
public class GprnController {

    @Autowired
    private GprnService gprnService;

    @Autowired
    private PurchaseOrderService poService;
    @Autowired
    private ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(GprnServiceImpl.class);
/*
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createGprn(
            @RequestPart("gprnRequestDTO") String gprnRequestDTO,
            @RequestPart(value = "provisionalReceiptCertificate") MultipartFile provisionalReceiptCertificate,
            @RequestPart(value = "photographPath") MultipartFile photographPath
    ) throws JsonProcessingException {
        GprnRequestDto gprnRequestDto = mapper.readValue(gprnRequestDTO,GprnRequestDto.class);
        gprnRequestDto.setProvisionalReceiptCertificate(provisionalReceiptCertificate);
        String provisionalReceiptCertificateFileName =provisionalReceiptCertificate.getOriginalFilename();
        String photoFileName = photographPath.getOriginalFilename();
        GprnResponseDto savedGprn = gprnService.createGprnWithMaterialDetails(gprnRequestDto, provisionalReceiptCertificateFileName,photoFileName);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(savedGprn), HttpStatus.OK);
    }


 */

@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Object> createGprn(
        @RequestPart("gprnRequestDTO") String gprnRequestDTO,
        @RequestPart(value = "provisionalReceiptCertificate", required = false) MultipartFile provisionalReceiptCertificate,
        @RequestPart(value = "materialFiles", required = false) List<MultipartFile> materialFiles
) throws IOException {

    GprnRequestDto gprnRequestDto = mapper.readValue(gprnRequestDTO, GprnRequestDto.class);

    // Set GPRN file
    if (provisionalReceiptCertificate != null) {
        gprnRequestDto.setProvisionalReceiptCertificate(provisionalReceiptCertificate);
    }
    System.out.println("Received files: " + materialFiles);

    Map<String, String> materialFileMap = new HashMap<>();
    if (materialFiles != null) {
        for (MultipartFile file : materialFiles) {
            String fileName = file.getOriginalFilename(); // Get filename
            String materialCode = extractMaterialCodeFromFileName(fileName); // Extract materialCode
            String filePath = saveFile(file); // Save file & get path
            materialFileMap.put(materialCode, filePath); // Store filePath with materialCode key
        }
    }

// Assign file paths to materials based on materialCode
    for (GprnMaterialsRequestDto material : gprnRequestDto.getGprnMaterials()) {
        if (materialFileMap.containsKey(material.getMaterialCode())) {
            material.setPhotographPath(materialFileMap.get(material.getMaterialCode())); // Assign file path
        }
    }


    for (GprnMaterialsRequestDto material : gprnRequestDto.getGprnMaterials()) {
        if (materialFileMap.containsKey(material.getMaterialCode())) {
            material.setPhotographPath(materialFileMap.get(material.getMaterialCode()));
        } else {
            log.warn("No file found for material code: " + material.getMaterialCode());
        }
    }


    // Call service layer
    GprnResponseDto savedGprn = gprnService.createGprnWithMaterialDetails(gprnRequestDto);

    return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(savedGprn));
}
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "/uploads/";
        File directory = new File(uploadDir);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Invalid file name");
        }

        String filePath = uploadDir + fileName;
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        return filePath;
    }

    private String extractMaterialCodeFromFileName(String fileName) {
        if (fileName == null || !fileName.contains("_")) {
            throw new IllegalArgumentException("Invalid file name format: " + fileName);
        }
        return fileName.split("_")[0]; // Extract materialCode safely
    }



    @PutMapping("/{gprnId}")
    public ResponseEntity<Object> updateGprnById(
            @PathVariable String gprnId, @RequestPart("gprnRequestDTO") String gprnRequestDTO,
            @RequestPart(value = "provisionalReceiptCertificate") MultipartFile provisionalReceiptCertificate,
            @RequestPart(value = "photographPath") MultipartFile photographPath) throws JsonProcessingException {
        GprnRequestDto gprnRequestDto = mapper.readValue(gprnRequestDTO,GprnRequestDto.class);
        gprnRequestDto.setProvisionalReceiptCertificate(provisionalReceiptCertificate);
        String provisionalReceiptCertificateFileName =provisionalReceiptCertificate.getOriginalFilename();
        String photoFileName = photographPath.getOriginalFilename();
        GprnResponseDto gprn =gprnService.updateGprn(gprnId,gprnRequestDto,provisionalReceiptCertificateFileName,photoFileName);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprn), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Object> getAllGprn() {
        List<GprnResponseDto> gprns=gprnService.getAllGprn();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprns), HttpStatus.OK);
    }

    @GetMapping("/{gprnId}")
    public ResponseEntity<Object> getGprnById(@PathVariable String gprnId) {
        GprnResponseDto gprn = gprnService.getGprnById(gprnId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprn), HttpStatus.OK);
    }

    @DeleteMapping("/{gprnId}")
    public ResponseEntity<String> deleteGprn(@PathVariable String gprnId) {
        gprnService.deleteGprn(gprnId);
        return ResponseEntity.ok("Gprn deleted successfully!");
    }
/*

    @GetMapping("/purchase-orders/{poId}")
    public ResponseEntity<Object> fetchPurchaseOrderDetails(@PathVariable String poId) {
        PurchaseOrderResponseDTO po = poService.getPurchaseOrderById(poId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(po), HttpStatus.OK);
    }


 */


}
