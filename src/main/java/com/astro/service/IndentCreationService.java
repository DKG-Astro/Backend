package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.*;
import com.astro.dto.workflow.ProcurementDtos.TechnoMomReportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IndentCreationService {

    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO);
            //,String uploadingPriorApprovalsFileName,String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName );

   public IndentCreationResponseDTO updateIndent(String indentId, IndentCreationRequestDTO indentRequestDTO);
         //  ,String uploadingPriorApprovalsFileName, String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName);

    public IndentCreationResponseDTO getIndentById(String indentId);

    public IndentDataResponseDto getIndentDataById(String indentId) throws IOException;

    public List<IndentCreationResponseDTO> getAllIndents();

    void deleteIndent(String indentId);


    List<IndentReportDetailsDTO> getIndentReport(String startDate, String endDate);

    public List<TechnoMomReportDTO> getTechnoMomReport(String startDate, String endDate);

    public List<materialHistoryDto> getIndentIdAndUserId(String materialCode);

}
