package com.astro.service;


import com.astro.dto.workflow.TenderRequestDto;
import com.astro.entity.TenderRequest;

import java.util.List;

public interface TenderRequestService {


    public TenderRequest createTenderRequest(TenderRequestDto tenderRequestDto);
    public TenderRequest updateTenderRequest(Long id, TenderRequestDto tenderRequestDto);
    public TenderRequest getTenderRequestById(Long id);
    public List<TenderRequest> getAllTenderRequests();
    public void deleteTenderRequest(Long id);


}