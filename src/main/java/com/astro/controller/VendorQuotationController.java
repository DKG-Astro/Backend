package com.astro.controller;

import com.astro.dto.workflow.VendorMasterResponseDto;
import com.astro.dto.workflow.VendorQuotationAgainstTenderDto;
import com.astro.dto.workflow.VendorStatusDto;
import com.astro.service.VendorQuotationAgainstTenderService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-quotation")
public class VendorQuotationController {

    @Autowired
    private VendorQuotationAgainstTenderService vqService;
    @PostMapping
    public ResponseEntity<Object> createVendorQuotation(@RequestBody VendorQuotationAgainstTenderDto requestDTO) {
        VendorQuotationAgainstTenderDto vq = vqService.saveQuotation(requestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(vq), HttpStatus.OK);
    }

    @GetMapping("/{tenderId}")
    public ResponseEntity<Object> getVendorQuotationByTenderId(@PathVariable String tenderId) {
        List<VendorQuotationAgainstTenderDto> responseDTO = vqService.getQuotationsByTenderId(tenderId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }
    @GetMapping("NotSubmitVendors/{tenderId}")
    public ResponseEntity<Object> getVendorNotSubmittedQuotationByTenderId(@PathVariable String tenderId) {
        List<String> responseDTO = vqService.getVendorsWhoDidNotSubmitQuotation(tenderId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    @GetMapping("VendorStatus/{vendorId}")
    public ResponseEntity<Object> getVendorStatus(@PathVariable String vendorId) {
        VendorStatusDto responseDTO = vqService.getVendorStatus(vendorId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }



}
