package com.astro.service;


import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;
import com.astro.entity.ProcurementModule.TenderRequest;

import java.util.List;

public interface TenderRequestService {


    public TenderResponseDto createTenderRequest(TenderRequestDto tenderRequestDto);
    public TenderResponseDto updateTenderRequest(Long id, TenderRequestDto tenderRequestDto);
    public TenderResponseDto getTenderRequestById(Long id);
    public List<TenderResponseDto> getAllTenderRequests();
    public void deleteTenderRequest(Long id);


}
