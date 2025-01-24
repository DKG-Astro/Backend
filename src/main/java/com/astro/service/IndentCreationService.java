package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;

import java.util.List;

public interface IndentCreationService {


    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO) ;

   public IndentCreationResponseDTO updateIndent(String indentorId, IndentCreationRequestDTO indentRequestDTO);
    public IndentCreationResponseDTO getIndentById(String indentorId);
    public List<IndentCreationResponseDTO> getAllIndents();

  void deleteIndent(String indentorId);
}
