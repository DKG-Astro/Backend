package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;
import com.astro.service.ContigencyPurchaseService;
import com.astro.util.ResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contigency-purchase")
public class ContigencyPurchaseController {

    @Autowired
    private ContigencyPurchaseService CPservice;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createContingencyPurchaseRequest(
            @RequestPart("contigencyPurchaseDto") ContigencyPurchaseRequestDto contigencyPurchaseDto,
            @RequestPart(value = "uploadCopyOfInvoice") MultipartFile uploadCopyOfInvoice
    ) {
        contigencyPurchaseDto.setUploadCopyOfInvoice(uploadCopyOfInvoice);
       String uploadCopyOfInvoiceFileName =uploadCopyOfInvoice.getOriginalFilename();
        ContigencyPurchaseResponseDto created = CPservice.createContigencyPurchase(contigencyPurchaseDto,uploadCopyOfInvoiceFileName);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PutMapping(value = "/{contigencyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateContingencyPurchaseRequest(
            @PathVariable String contigencyId,
            @RequestPart("contigencyPurchaseDto") ContigencyPurchaseRequestDto contigencyPurchaseDto,
            @RequestPart(value = "uploadCopyOfInvoice") MultipartFile uploadCopyOfInvoice
    ) {

            contigencyPurchaseDto.setUploadCopyOfInvoice(uploadCopyOfInvoice);
        String uploadCopyOfInvoiceFileName =uploadCopyOfInvoice.getOriginalFilename();
        ContigencyPurchaseResponseDto updated = CPservice.updateContigencyPurchase(contigencyId, contigencyPurchaseDto,uploadCopyOfInvoiceFileName);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
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
