package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;

import com.astro.dto.workflow.WorkflowTransitionDto;
import com.astro.entity.UserMaster;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.repository.UserMasterRepository;
import com.astro.service.IndentCreationService;

import com.astro.service.UserService;
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


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/indents")
public class IndentCreationController {

    @Autowired
    private IndentCreationService indentCreationService;

    @Autowired
    private IndentCreationRepository indentCreationRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
   private WorkflowService workflowService;
    @Autowired
   private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(IndentCreationController.class);


 @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<Object> createIndent(
         @RequestPart("indentRequestDto") String indentRequestDto,
        @RequestPart(value = "uploadingPriorApprovals") MultipartFile uploadingPriorApprovals,
        @RequestPart(value = "uploadTenderDocuments") MultipartFile uploadTenderDocuments,
        @RequestPart(value = "uploadGOIOrRFP") MultipartFile uploadGOIOrRFP,
        @RequestPart(value = "uploadPACOrBrandPAC") MultipartFile uploadPACOrBrandPAC
) throws JsonProcessingException {
     IndentCreationRequestDTO indentRequestDTO = mapper.readValue(indentRequestDto, IndentCreationRequestDTO.class);
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

     // Initiateing the workflow after saving the indent
     String requestId = responseDTO.getIndentId(); // Useing the indent ID as the request ID
     String workflowName = "Indent Workflow";
     String createdBy = indentRequestDTO.getCreatedBy();
     Optional<UserMaster> userMaster = userService.getUserMasterByCreatedBy(createdBy);
     Integer userId = userMaster.get().getUserId();


     // Call initiateWorkflow API
    WorkflowTransitionDto workflowTransitionDto = workflowService.initiateWorkflow(requestId, workflowName, userId);
/*
    // action approve

     // Transition to the next role
     TransitionDto nextTransition = workflowService.nextTransition(
             workflowTransitionDto.getWorkflowId(),
             workflowName,
             workflowTransitionDto.getCurrentRole(),
             requestId
     );
     //Automatic approved with example data. we can approve manual by using swagger link
     TransitionActionReqDto transitionActionReqDto = new TransitionActionReqDto();
     transitionActionReqDto.setWorkflowTransitionId(workflowTransitionDto.getWorkflowTransitionId());
     transitionActionReqDto.setActionBy(createdBy);
     transitionActionReqDto.setAction("APPROVE"); // or "REJECT"
     transitionActionReqDto.setRemarks("Automatically approved by system.");

     WorkflowTransitionDto workflowTransitionDto = workflowService.performTransitionAction(transitionActionReqDto);


 */
     return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
}


    @PutMapping(value = "/{indentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateIndent(
            @PathVariable String indentId,
            @RequestPart("indentRequestDto") String indentRequestDto,
            @RequestPart(value = "uploadingPriorApprovals") MultipartFile uploadingPriorApprovals,
            @RequestPart(value = "uploadTenderDocuments") MultipartFile uploadTenderDocuments,
            @RequestPart(value = "uploadGOIOrRFP") MultipartFile uploadGOIOrRFP,
            @RequestPart(value = "uploadPACOrBrandPAC") MultipartFile uploadPACOrBrandPAC
    ) throws JsonProcessingException {
     IndentCreationRequestDTO indentRequestDTO = mapper.readValue(indentRequestDto,IndentCreationRequestDTO.class);
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
        IndentCreationResponseDTO responseDTO = indentCreationService.updateIndent(indentId, indentRequestDTO,uploadingPriorApprovalsFileName,
                uploadTenderDocumentsFileName,uploadGOIOrRFPFileName,uploadPACOrBrandPACFileName );

        // Return the success response
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }


    // Get Indent by ID
    @GetMapping("/{indentId}")
    public ResponseEntity<Object> getIndentById(@PathVariable String indentId) {
        IndentCreationResponseDTO responseDTO = indentCreationService.getIndentById(indentId);
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

    @DeleteMapping("/{indentId}")
    public ResponseEntity<String> deleteIndent(@PathVariable String indentId) {
        indentCreationService.deleteIndent(indentId);
        return ResponseEntity.ok("indent deleted successfully. Id:"+" " +indentId);
    }




}
