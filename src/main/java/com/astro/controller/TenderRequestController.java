package com.astro.controller;

import com.astro.dto.workflow.TenderRequestDto;
import com.astro.entity.TenderRequest;
import com.astro.service.TenderRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tender-requests")
public class TenderRequestController {


    @Autowired
    private TenderRequestService TRService;
    @PostMapping
    public ResponseEntity<TenderRequest> createTenderRequest(@RequestBody TenderRequestDto tenderRequestDto) {
        TenderRequest created = TRService.createTenderRequest(tenderRequestDto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenderRequest> updateTenderRequest(@PathVariable Long id, @RequestBody TenderRequestDto tenderRequestDto) {
        TenderRequest updated = TRService.updateTenderRequest(id, tenderRequestDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<TenderRequest> getAllTenderRequests() {
        return TRService.getAllTenderRequests();
    }

    @GetMapping("/{id}")
    public TenderRequest getTenderRequestById(@PathVariable Long id) {
        return TRService.getTenderRequestById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTenderRequest(@PathVariable Long id) {
        TRService.deleteTenderRequest(id);
        return ResponseEntity.ok("Tender Request deleted successfully. Id:"+" " +id);
    }



}
