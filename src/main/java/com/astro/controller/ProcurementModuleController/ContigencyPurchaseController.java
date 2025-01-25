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

    @PutMapping("/{contigencyId}")
    public ResponseEntity<Object> updateContigencyPurchase(@PathVariable String contigencyId, @RequestBody ContigencyPurchaseRequestDto contigencyPurchaseDto) {
        ContigencyPurchaseResponseDto updated = CPservice.updateContigencyPurchase(contigencyId, contigencyPurchaseDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllContigencyPurchase() {

     List<ContigencyPurchaseResponseDto> contigencyPurchase= CPservice.getAllContigencyPurchase();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(contigencyPurchase), HttpStatus.OK);

    }

    @GetMapping("/{contigencyId}")
    public ResponseEntity<Object> getContigencyPurchaseById(@PathVariable String contigencyId) {

       ContigencyPurchaseResponseDto contigencyPurchase= CPservice.getContigencyPurchaseById(contigencyId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(contigencyPurchase), HttpStatus.OK);
    }

    @DeleteMapping("/{contigencyId}")
    public ResponseEntity<String> deleteContigencyPurchase(@PathVariable String contigencyId) {
        CPservice.deleteContigencyPurchase(contigencyId);
        return ResponseEntity.ok("Contigency Purchase deleted successfully. Id:"+" " +contigencyId);
    }




}
