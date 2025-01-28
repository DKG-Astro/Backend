package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;

import com.astro.service.TenderRequestService;

import com.astro.util.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tender-requests")
public class TenderRequestController {

    private static final Logger log = LoggerFactory.getLogger(TenderRequestController.class);

    @Autowired
    private TenderRequestService TRService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createTenderRequest(
            @RequestPart("tenderRequestDto") TenderRequestDto tenderRequestDto,
            @RequestPart(value = "uploadTenderDocuments") MultipartFile uploadTenderDocuments,
            @RequestPart(value = "uploadGeneralTermsAndConditions") MultipartFile uploadGeneralTermsAndConditions,
            @RequestPart(value = "uploadSpecificTermsAndConditions") MultipartFile uploadSpecificTermsAndConditions
    ) {
        // Set files in DTO
        tenderRequestDto.setUploadTenderDocuments(uploadTenderDocuments);
        tenderRequestDto.setUploadGeneralTermsAndConditions(uploadGeneralTermsAndConditions);
        tenderRequestDto.setUploadSpecificTermsAndConditions(uploadSpecificTermsAndConditions);

        String uploadTenderDocumentsFileName= uploadTenderDocuments.getOriginalFilename();
        String uploadGeneralTermsAndConditionsFileName=uploadGeneralTermsAndConditions.getOriginalFilename();
        String uploadSpecificTermsAndConditionsFileName=uploadSpecificTermsAndConditions.getOriginalFilename();

        TenderResponseDto created = TRService.createTenderRequest(tenderRequestDto,uploadTenderDocumentsFileName,uploadGeneralTermsAndConditionsFileName
        ,uploadSpecificTermsAndConditionsFileName);


        // Return success response
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }


    @PutMapping(value = "/{tenderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateTenderRequest(
            @PathVariable String tenderId,
            @RequestPart("tenderRequestDto") TenderRequestDto tenderRequestDto,
            @RequestPart(value = "uploadTenderDocuments") MultipartFile uploadTenderDocuments,
            @RequestPart(value = "uploadGeneralTermsAndConditions") MultipartFile uploadGeneralTermsAndConditions,
            @RequestPart(value = "uploadSpecificTermsAndConditions") MultipartFile uploadSpecificTermsAndConditions
    ){// Set files in DTO if provided
            tenderRequestDto.setUploadTenderDocuments(uploadTenderDocuments);
            tenderRequestDto.setUploadGeneralTermsAndConditions(uploadGeneralTermsAndConditions);
            tenderRequestDto.setUploadSpecificTermsAndConditions(uploadSpecificTermsAndConditions);
        String uploadTenderDocumentsFileName= uploadTenderDocuments.getOriginalFilename();
        String uploadGeneralTermsAndConditionsFileName=uploadGeneralTermsAndConditions.getOriginalFilename();
        String uploadSpecificTermsAndConditionsFileName=uploadSpecificTermsAndConditions.getOriginalFilename();

        // Call service to update tender request
        TenderResponseDto updated = TRService.updateTenderRequest(tenderId, tenderRequestDto,uploadTenderDocumentsFileName,uploadGeneralTermsAndConditionsFileName
                ,uploadSpecificTermsAndConditionsFileName);

        // Return success response
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
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
