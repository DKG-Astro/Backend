package com.astro.service;

import com.astro.dto.workflow.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.IndentDto.IndentCreationResponseDTO;
import com.astro.entity.IndentCreation;

import java.util.List;

public interface IndentCreationService {


    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO) ;

   public IndentCreationResponseDTO updateIndent(Long indentId, IndentCreationRequestDTO indentRequestDTO);
    public IndentCreationResponseDTO getIndentById(Long indentId);
    public List<IndentCreationResponseDTO> getAllIndents();

  void deleteIndent(Long id);
}
