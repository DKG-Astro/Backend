package com.astro.controller;

import com.astro.dto.workflow.ApprovalAndRejectionRequestDTO;
import com.astro.dto.workflow.VendorMasterResponseDto;
import com.astro.dto.workflow.VendorRegistrationRequestDTO;
import com.astro.dto.workflow.VendorRegistrationResponseDTO;
import com.astro.service.VendorMasterUtilService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-master-util")
public class VendorMasterUtilController {

    @Autowired
    private VendorMasterUtilService vendorMasterUtil;

    @PostMapping("/register")
    public ResponseEntity<Object> registerVendor(@RequestBody VendorRegistrationRequestDTO dto) {
        VendorRegistrationResponseDTO vendor = vendorMasterUtil.registerVendor(dto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(vendor), HttpStatus.OK);
    }


    @GetMapping("/awaiting-approval")
    public ResponseEntity<Object>  getAllAwaitingApprovalVendors() {
        List<VendorRegistrationResponseDTO> vendors = vendorMasterUtil.getAllAwaitingApprovalVendors();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(vendors), HttpStatus.OK);

    }

    @PostMapping("/performAction")
    public ResponseEntity<Object> approveOrRejectVendor(@RequestBody ApprovalAndRejectionRequestDTO request) {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(vendorMasterUtil.performAction(request)), HttpStatus.OK);

    }

    @PostMapping("/performBulkAction")
    public ResponseEntity<Object> approveOrRejectBulkVendor(@RequestBody List<ApprovalAndRejectionRequestDTO> request) {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(vendorMasterUtil.performAllAction(request)), HttpStatus.OK);

    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<Object> getVendorMasterUtilById(@PathVariable String vendorId) {
        VendorRegistrationResponseDTO responseDTO = vendorMasterUtil.getVendorMasterUtilById(vendorId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }


}
