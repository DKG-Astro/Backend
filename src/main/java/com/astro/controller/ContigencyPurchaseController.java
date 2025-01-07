package com.astro.controller;

import com.astro.dto.workflow.ContigencyPurchaseDto;
import com.astro.dto.workflow.TenderRequestDto;
import com.astro.entity.ContigencyPurchase;
import com.astro.entity.TenderRequest;
import com.astro.service.ContigencyPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contigency-purchase")
public class ContigencyPurchaseController {

    @Autowired
    private ContigencyPurchaseService CPservice;
    @PostMapping
    public ResponseEntity<ContigencyPurchase> createTenderRequest(@RequestBody ContigencyPurchaseDto contigencyPurchaseDto) {
        ContigencyPurchase created = CPservice.createTenderRequest(contigencyPurchaseDto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{ContigencyId}")
    public ResponseEntity<ContigencyPurchase> updateTenderRequest(@PathVariable Long ContigencyId, @RequestBody ContigencyPurchaseDto contigencyPurchaseDto) {
        ContigencyPurchase updated = CPservice.updateTenderRequest(ContigencyId, contigencyPurchaseDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<ContigencyPurchase> getAllTenderRequests() {

        return CPservice.getAllTenderRequests();
    }

    @GetMapping("/{ContigencyId}")
    public ContigencyPurchase getTenderRequestById(@PathVariable Long ContigencyId) {

        return CPservice.getTenderRequestById(ContigencyId);
    }

    @DeleteMapping("/{ContigencyId}")
    public ResponseEntity<String> deleteTenderRequest(@PathVariable Long ContigencyId) {
        CPservice.deleteTenderRequest(ContigencyId);
        return ResponseEntity.ok("Contigency Purchase deleted successfully. Id:"+" " +ContigencyId);
    }




}
