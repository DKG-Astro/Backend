package com.astro.controller.ProcurementModuleController;


import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.service.IndentCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indents")
public class IndentCreationController {

    @Autowired
    private IndentCreationService indentCreationService;

    @PostMapping
    public ResponseEntity<IndentCreationResponseDTO> createIndent(@RequestBody IndentCreationRequestDTO indentRequestDTO) {
        IndentCreationResponseDTO responseDTO = indentCreationService.createIndent(indentRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Update Indent
    @PutMapping("/{id}")
    public ResponseEntity<IndentCreationResponseDTO> updateIndent(@PathVariable Long id,
                                                                  @RequestBody IndentCreationRequestDTO indentRequestDTO) {
        IndentCreationResponseDTO responseDTO = indentCreationService.updateIndent(id, indentRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Get Indent by ID
    @GetMapping("/{id}")
    public ResponseEntity<IndentCreationResponseDTO> getIndentById(@PathVariable Long id) {
        IndentCreationResponseDTO responseDTO = indentCreationService.getIndentById(id);
        return responseDTO != null ? ResponseEntity.ok(responseDTO) : ResponseEntity.notFound().build();
    }

    // Get All Indents
    @GetMapping
    public ResponseEntity<List<IndentCreationResponseDTO>> getAllIndents() {
        List<IndentCreationResponseDTO> responseDTOs = indentCreationService.getAllIndents();
        return ResponseEntity.ok(responseDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIndent(@PathVariable Long id) {
        indentCreationService.deleteIndent(id);
        return ResponseEntity.ok("indent  deleted successfully. Id:"+" " +id);
    }




}
