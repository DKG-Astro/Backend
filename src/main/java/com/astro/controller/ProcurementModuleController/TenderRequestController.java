package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;

import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.dto.workflow.WorkflowTransitionDto;
import com.astro.service.TenderRequestService;

import com.astro.service.WorkflowService;
import com.astro.util.ResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ObjectMapper mapper;
    @PostMapping
    public ResponseEntity<Object> createTenderRequest(@RequestBody TenderRequestDto tenderRequestDTO) {

        TenderResponseDto created = TRService.createTenderRequest(tenderRequestDTO);

        String requestId = created.getTenderId(); // Useing the indent ID as the request ID
        String workflowName = "Tender Approver Workflow";
        Integer userId = created.getCreatedBy();
        //initiateing Workflow API
        WorkflowTransitionDto workflowTransitionDto = workflowService.initiateWorkflow(requestId, workflowName, userId);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }


    @PutMapping(value = "/{tenderId}")
    public ResponseEntity<Object> updateTenderRequest(@PathVariable String tenderId, @RequestBody TenderRequestDto tenderRequestDTO) {// Set files in DTO if provided

        // Call service to update tender request
        TenderResponseDto updated = TRService.updateTenderRequest(tenderId, tenderRequestDTO);

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

        TenderWithIndentResponseDTO tenderRequest = TRService.getTenderRequestById(tenderId);


        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(tenderRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{tenderId}")
    public ResponseEntity<String> deleteTenderRequest(@PathVariable String tenderId) {
        TRService.deleteTenderRequest(tenderId);
        return ResponseEntity.ok("Tender Request deleted successfully. Id:"+" " +tenderId);
    }



}
