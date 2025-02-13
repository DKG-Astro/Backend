package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IndentCreationService {


    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO,String uploadingPriorApprovalsFileName,
                                                  String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName );

   public IndentCreationResponseDTO updateIndent(String indentId, IndentCreationRequestDTO indentRequestDTO,String uploadingPriorApprovalsFileName,
                                                 String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName);

    public IndentCreationResponseDTO getIndentById(String indentId);

    public List<IndentCreationResponseDTO> getAllIndents();

    void deleteIndent(String indentId);

}
