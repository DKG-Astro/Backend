package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;

import com.astro.service.TenderRequestService;

import com.astro.util.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tender-requests")
public class TenderRequestController {

    private static final Logger log = LoggerFactory.getLogger(TenderRequestController.class);

    @Autowired
    private TenderRequestService TRService;
    @PostMapping
    public ResponseEntity<Object> createTenderRequest(@RequestBody TenderRequestDto tenderRequestDto) {
        TenderResponseDto created = TRService.createTenderRequest(tenderRequestDto);
        log.info("Received Tender Request: {}", created);

        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PutMapping("/{tenderId}")
    public ResponseEntity<Object> updateTenderRequest(@PathVariable String tenderId, @RequestBody TenderRequestDto tenderRequestDto) {
        TenderResponseDto updated = TRService.updateTenderRequest(tenderId, tenderRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllTenderRequests() {

        List<TenderResponseDto>  tenderRequest = TRService.getAllTenderRequests();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(tenderRequest), HttpStatus.OK);
    }

    @GetMapping("/{tenderId}")
    public ResponseEntity<Object> getTenderRequestById(@PathVariable String tenderId) {

        TenderResponseDto tenderRequest = TRService.getTenderRequestById(tenderId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(tenderRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{tenderId}")
    public ResponseEntity<String> deleteTenderRequest(@PathVariable String tenderId) {
        TRService.deleteTenderRequest(tenderId);
        return ResponseEntity.ok("Tender Request deleted successfully. Id:"+" " +tenderId);
    }



}
