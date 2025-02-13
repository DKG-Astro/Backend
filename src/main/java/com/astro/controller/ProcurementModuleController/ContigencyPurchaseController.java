package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseReportDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.dto.workflow.WorkflowTransitionDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;
import com.astro.entity.UserMaster;
import com.astro.service.ContigencyPurchaseService;
import com.astro.service.UserService;
import com.astro.service.WorkflowService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/contigency-purchase")
public class ContigencyPurchaseController {

    @Autowired
    private ContigencyPurchaseService CPservice;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ObjectMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createContingencyPurchaseRequest(
            @RequestPart("contigencyPurchaseDto") String contigencyPurchaseDto,
            @RequestPart(value = "uploadCopyOfInvoice") MultipartFile uploadCopyOfInvoice
    ) throws JsonProcessingException {
        ContigencyPurchaseRequestDto contigencyPurchaseDTO = mapper.readValue(contigencyPurchaseDto,ContigencyPurchaseRequestDto.class);
        contigencyPurchaseDTO.setUploadCopyOfInvoice(uploadCopyOfInvoice);
       String uploadCopyOfInvoiceFileName =uploadCopyOfInvoice.getOriginalFilename();
        ContigencyPurchaseResponseDto created = CPservice.createContigencyPurchase(contigencyPurchaseDTO,uploadCopyOfInvoiceFileName);

        // Initiateing the workflow after saving the indent
        String requestId = created.getContigencyId(); // Useing the indent ID as the request ID
        String workflowName = "Contingency Purchase Workflow";
        String createdBy = contigencyPurchaseDTO.getCreatedBy();
        Optional<UserMaster> userMaster = userService.getUserMasterByCreatedBy(createdBy);
        Integer userId = userMaster.get().getUserId();


        // Call initiateWorkflow API
        WorkflowTransitionDto workflowTransitionDto = workflowService.initiateWorkflow(requestId, workflowName, userId);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PutMapping(value = "/{contigencyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateContingencyPurchaseRequest(
            @PathVariable String contigencyId,
            @RequestPart("contigencyPurchaseDto") String contigencyPurchaseDto,
            @RequestPart(value = "uploadCopyOfInvoice") MultipartFile uploadCopyOfInvoice
    ) throws JsonProcessingException {
        ContigencyPurchaseRequestDto contigencyPurchaseDTO = mapper.readValue(contigencyPurchaseDto,ContigencyPurchaseRequestDto.class);
            contigencyPurchaseDTO.setUploadCopyOfInvoice(uploadCopyOfInvoice);
        String uploadCopyOfInvoiceFileName =uploadCopyOfInvoice.getOriginalFilename();
        ContigencyPurchaseResponseDto updated = CPservice.updateContigencyPurchase(contigencyId, contigencyPurchaseDTO,uploadCopyOfInvoiceFileName);

        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(updated), HttpStatus.OK);
    }


    @GetMapping("/cp/report")
    public List<ContigencyPurchaseReportDto> getContigencyPurchaseReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return CPservice.getContigencyPurchaseReport(startDate, endDate);
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
