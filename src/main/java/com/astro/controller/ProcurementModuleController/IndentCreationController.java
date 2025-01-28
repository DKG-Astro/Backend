package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;

import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.service.IndentCreationService;

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
import java.util.Map;

@RestController
@RequestMapping("/api/indents")
public class IndentCreationController {

    @Autowired
    private IndentCreationService indentCreationService;

    @Autowired
    private IndentCreationRepository indentCreationRepository;
    private static final Logger log = LoggerFactory.getLogger(IndentCreationController.class);


 @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<Object> createIndent(
         @RequestPart("indentRequestDTO") IndentCreationRequestDTO indentRequestDTO,
        @RequestPart(value = "uploadingPriorApprovals") MultipartFile uploadingPriorApprovals,
        @RequestPart(value = "uploadTenderDocuments") MultipartFile uploadTenderDocuments,
        @RequestPart(value = "uploadGOIOrRFP") MultipartFile uploadGOIOrRFP,
        @RequestPart(value = "uploadPACOrBrandPAC") MultipartFile uploadPACOrBrandPAC
){
    // Set files in DTO
    indentRequestDTO.setUploadingPriorApprovals(uploadingPriorApprovals);
    indentRequestDTO.setUploadTenderDocuments(uploadTenderDocuments);
    indentRequestDTO.setUploadGOIOrRFP(uploadGOIOrRFP);
    indentRequestDTO.setUploadPACOrBrandPAC(uploadPACOrBrandPAC);
     String uploadingPriorApprovalsFileName = uploadingPriorApprovals.getOriginalFilename();
     String uploadTenderDocumentsFileName = uploadTenderDocuments.getOriginalFilename();
     String uploadGOIOrRFPFileName = uploadGOIOrRFP.getOriginalFilename();
     String uploadPACOrBrandPACFileName = uploadPACOrBrandPAC.getOriginalFilename();
     IndentCreationResponseDTO responseDTO = indentCreationService.createIndent(indentRequestDTO,uploadingPriorApprovalsFileName,
          uploadTenderDocumentsFileName,uploadGOIOrRFPFileName,uploadPACOrBrandPACFileName );
    return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
}


    @PutMapping(value = "/{indentorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateIndent(
            @PathVariable String indentorId,
            @RequestPart("indentRequestDTO") IndentCreationRequestDTO indentRequestDTO,
            @RequestPart(value = "uploadingPriorApprovals") MultipartFile uploadingPriorApprovals,
            @RequestPart(value = "uploadTenderDocuments") MultipartFile uploadTenderDocuments,
            @RequestPart(value = "uploadGOIOrRFP") MultipartFile uploadGOIOrRFP,
            @RequestPart(value = "uploadPACOrBrandPAC") MultipartFile uploadPACOrBrandPAC
    ){
        // Set files in DTO if present
            indentRequestDTO.setUploadingPriorApprovals(uploadingPriorApprovals);
            indentRequestDTO.setUploadTenderDocuments(uploadTenderDocuments);
            indentRequestDTO.setUploadGOIOrRFP(uploadGOIOrRFP);
            indentRequestDTO.setUploadPACOrBrandPAC(uploadPACOrBrandPAC);
        String uploadingPriorApprovalsFileName = uploadingPriorApprovals.getOriginalFilename();
        String uploadTenderDocumentsFileName = uploadTenderDocuments.getOriginalFilename();
        String uploadGOIOrRFPFileName = uploadGOIOrRFP.getOriginalFilename();
        String uploadPACOrBrandPACFileName = uploadPACOrBrandPAC.getOriginalFilename();

        // Call the service to update the indent
        IndentCreationResponseDTO responseDTO = indentCreationService.updateIndent(indentorId, indentRequestDTO,uploadingPriorApprovalsFileName,
                uploadTenderDocumentsFileName,uploadGOIOrRFPFileName,uploadPACOrBrandPACFileName );

        // Return the success response
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }


    // Get Indent by ID
    @GetMapping("/{indentorId}")
    public ResponseEntity<Object> getIndentById(@PathVariable String indentorId) {
        IndentCreationResponseDTO responseDTO = indentCreationService.getIndentById(indentorId);
        // Set filenames for the uploaded files in the response DTO
        responseDTO.setUploadingPriorApprovalsFileName(responseDTO.getUploadingPriorApprovalsFileName());
        responseDTO.setUploadTenderDocumentsFileName(responseDTO.getUploadTenderDocumentsFileName());
        responseDTO.setUploadGOIOrRFPFileName(responseDTO.getUploadGOIOrRFPFileName());
        responseDTO.setUploadPACOrBrandPACFileName(responseDTO.getUploadPACOrBrandPACFileName());
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    // Get All Indents
    @GetMapping
    public ResponseEntity<Object> getAllIndents() {
        List<IndentCreationResponseDTO> responseDTOs = indentCreationService.getAllIndents();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTOs), HttpStatus.OK);
    }

    @DeleteMapping("/{indentorId}")
    public ResponseEntity<String> deleteIndent(@PathVariable String indentorId) {
        indentCreationService.deleteIndent(indentorId);
        return ResponseEntity.ok("indent  deleted successfully. Id:"+" " +indentorId);
    }




}
