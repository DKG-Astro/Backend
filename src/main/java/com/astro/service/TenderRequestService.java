package com.astro.service;


import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;
import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.entity.ProcurementModule.TenderRequest;

import java.util.List;

public interface TenderRequestService {


    public TenderResponseDto createTenderRequest(TenderRequestDto tenderRequestDto);
           // ,String uploadTenderDocumentsFileName,String uploadGeneralTermsAndConditionsFileName, String uploadSpecificTermsAndConditionsFileName);
    public TenderResponseDto updateTenderRequest(String tenderId, TenderRequestDto tenderRequestDto);
           // ,String uploadTenderDocumentsFileName,String uploadGeneralTermsAndConditionsFileName , String uploadSpecificTermsAndConditionsFileName);
   public TenderWithIndentResponseDTO getTenderRequestById(String tenderId);
    public List<TenderResponseDto> getAllTenderRequests();
    public void deleteTenderRequest(String tenderId);


    //TenderWithIndentResponseDTO getTendersRequestById(String tenderId);
}
