package com.astro.service;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;

import java.util.List;

public interface ContigencyPurchaseService {


    public ContigencyPurchase createTenderRequest(ContigencyPurchaseDto contigencyPurchaseDto);
    public ContigencyPurchase updateTenderRequest(Long ContigencyId, ContigencyPurchaseDto contigencyPurchaseDto);
    public ContigencyPurchase getTenderRequestById(Long ContigencyId);
    public List<ContigencyPurchase> getAllTenderRequests();
    public void deleteTenderRequest(Long ContigencyId);

}
