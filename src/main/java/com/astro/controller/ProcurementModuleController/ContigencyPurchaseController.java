package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;
import com.astro.service.ContigencyPurchaseService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contigency-purchase")
public class ContigencyPurchaseController {

    @Autowired
    private ContigencyPurchaseService CPservice;
    @PostMapping
    public ResponseEntity<Object> createTenderRequest(@RequestBody ContigencyPurchaseRequestDto contigencyPurchaseDto) {
        ContigencyPurchaseResponseDto created = CPservice.createContigencyPurchase(contigencyPurchaseDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PutMapping("/{ContigencyId}")
    public ResponseEntity<Object> updateContigencyPurchase(@PathVariable Long ContigencyId, @RequestBody ContigencyPurchaseRequestDto contigencyPurchaseDto) {
        ContigencyPurchaseResponseDto updated = CPservice.updateContigencyPurchase(ContigencyId, contigencyPurchaseDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllContigencyPurchase() {

     List<ContigencyPurchaseResponseDto> contigencyPurchase= CPservice.getAllContigencyPurchase();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(contigencyPurchase), HttpStatus.OK);

    }

    @GetMapping("/{ContigencyId}")
    public ResponseEntity<Object> getContigencyPurchaseById(@PathVariable Long ContigencyId) {

       ContigencyPurchaseResponseDto contigencyPurchase= CPservice.getContigencyPurchaseById(ContigencyId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(contigencyPurchase), HttpStatus.OK);
    }

    @DeleteMapping("/{ContigencyId}")
    public ResponseEntity<String> deleteContigencyPurchase(@PathVariable Long ContigencyId) {
        CPservice.deleteContigencyPurchase(ContigencyId);
        return ResponseEntity.ok("Contigency Purchase deleted successfully. Id:"+" " +ContigencyId);
    }




}
