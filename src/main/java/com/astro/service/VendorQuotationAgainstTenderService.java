package com.astro.service;

import com.astro.dto.workflow.VendorQuotationAgainstTenderDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VendorQuotationAgainstTenderService {

    public VendorQuotationAgainstTenderDto saveQuotation(VendorQuotationAgainstTenderDto dto);

    public List<VendorQuotationAgainstTenderDto> getQuotationsByTenderId(String tenderId);

}
