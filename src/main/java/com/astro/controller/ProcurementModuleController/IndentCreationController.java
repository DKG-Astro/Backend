package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.service.IndentCreationService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indents")
public class IndentCreationController {

    @Autowired
    private IndentCreationService indentCreationService;

    @PostMapping
    public ResponseEntity<Object> createIndent(@RequestBody IndentCreationRequestDTO indentRequestDTO) {
        IndentCreationResponseDTO responseDTO = indentCreationService.createIndent(indentRequestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    // Update Indent
    @PutMapping("/{indentorId}")
    public ResponseEntity<Object> updateIndent(@PathVariable String indentorId,
                                                                  @RequestBody IndentCreationRequestDTO indentRequestDTO) {
        IndentCreationResponseDTO responseDTO = indentCreationService.updateIndent(indentorId, indentRequestDTO);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(responseDTO), HttpStatus.OK);
    }

    // Get Indent by ID
    @GetMapping("/{indentorId}")
    public ResponseEntity<Object> getIndentById(@PathVariable String indentorId) {
        IndentCreationResponseDTO responseDTO = indentCreationService.getIndentById(indentorId);
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
