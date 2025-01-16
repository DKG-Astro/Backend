package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.service.TenderRequestService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tender-requests")
public class TenderRequestController {


    @Autowired
    private TenderRequestService TRService;
    @PostMapping
    public ResponseEntity<Object> createTenderRequest(@RequestBody TenderRequestDto tenderRequestDto) {
        TenderResponseDto created = TRService.createTenderRequest(tenderRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTenderRequest(@PathVariable Long id, @RequestBody TenderRequestDto tenderRequestDto) {
        TenderResponseDto updated = TRService.updateTenderRequest(id, tenderRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllTenderRequests() {

        List<TenderResponseDto>  tenderRequest = TRService.getAllTenderRequests();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(tenderRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTenderRequestById(@PathVariable Long id) {

        TenderResponseDto tenderRequest = TRService.getTenderRequestById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(tenderRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTenderRequest(@PathVariable Long id) {
        TRService.deleteTenderRequest(id);
        return ResponseEntity.ok("Tender Request deleted successfully. Id:"+" " +id);
    }



}
