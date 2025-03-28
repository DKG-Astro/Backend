package com.astro.controller;

import com.astro.dto.workflow.VendorContractReportDTO;
import com.astro.dto.workflow.VendorMasterRequestDto;
import com.astro.dto.workflow.VendorMasterResponseDto;
import com.astro.service.VendorMasterService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-master")
public class VendorMasterController {

    @Autowired
    private VendorMasterService vendorMasterService;


    @PostMapping
    public ResponseEntity<Object> createVendorMaster(@RequestBody VendorMasterRequestDto requestDTO) {
       VendorMasterResponseDto material = vendorMasterService.createVendorMaster(requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(material), HttpStatus.OK);
    }

    @PutMapping("/{vendorId}")
    public ResponseEntity<Object> updateVendorMaster(@PathVariable String vendorId,
                                                       @RequestBody VendorMasterRequestDto requestDTO) {
        VendorMasterResponseDto response = vendorMasterService.updateVendorMaster(vendorId, requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllVendorMaster() {
        List<VendorMasterResponseDto> response = vendorMasterService.getAllVendorMasters();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<Object> getVendorMasterById(@PathVariable String vendorId) {
        VendorMasterResponseDto responseDTO = vendorMasterService.getVendorMasterById(vendorId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<String> deleteVendorMaster(@PathVariable String vendorId) {
        vendorMasterService.deleteVendorMaster(vendorId);
        return ResponseEntity.ok("material master deleted successfully. materialCode:"+" " +vendorId);
    }
/*
    @GetMapping("/not-approved")
    public ResponseEntity<Object> getAllNotApprovedVendors() {
        List<VendorMasterResponseDto> response = vendorMasterService.getAllNotApprovedVendors();
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(response));
    }

 */





}
