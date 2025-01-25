package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;

import java.util.List;

public interface ContigencyPurchaseService {


    public ContigencyPurchaseResponseDto createContigencyPurchase(ContigencyPurchaseRequestDto contigencyPurchaseDto);
    public ContigencyPurchaseResponseDto updateContigencyPurchase(String contigencyId, ContigencyPurchaseRequestDto contigencyPurchaseDto);
    public ContigencyPurchaseResponseDto getContigencyPurchaseById(String contigencyId);
    public List<ContigencyPurchaseResponseDto> getAllContigencyPurchase();
    public void deleteContigencyPurchase(String contigencyId);

}
