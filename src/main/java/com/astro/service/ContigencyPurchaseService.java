package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;

import java.util.List;

public interface ContigencyPurchaseService {


    public ContigencyPurchaseResponseDto createContigencyPurchase(ContigencyPurchaseRequestDto contigencyPurchaseDto);
    public ContigencyPurchaseResponseDto updateContigencyPurchase(Long ContigencyId, ContigencyPurchaseRequestDto contigencyPurchaseDto);
    public ContigencyPurchaseResponseDto getContigencyPurchaseById(Long ContigencyId);
    public List<ContigencyPurchaseResponseDto> getAllContigencyPurchase();
    public void deleteContigencyPurchase(Long ContigencyId);

}
