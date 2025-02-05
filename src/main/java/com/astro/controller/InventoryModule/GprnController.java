package com.astro.controller.InventoryModule;

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

import java.util.List;

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
