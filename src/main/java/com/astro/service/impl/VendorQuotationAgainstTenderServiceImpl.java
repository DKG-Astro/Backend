package com.astro.service.impl;

import com.astro.dto.workflow.VendorQuotationAgainstTenderDto;
import com.astro.entity.VendorQuotationAgainstTender;
import com.astro.repository.VendorQuotationAgainstTenderRepository;
import com.astro.service.VendorQuotationAgainstTenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorQuotationAgainstTenderServiceImpl implements VendorQuotationAgainstTenderService {

    @Autowired
    private VendorQuotationAgainstTenderRepository vendorQuotationAgainstTenderRepository;

    @Override
    public VendorQuotationAgainstTenderDto saveQuotation(VendorQuotationAgainstTenderDto dto) {
        VendorQuotationAgainstTender quotation = new VendorQuotationAgainstTender();
        quotation.setTenderId(dto.getTenderId());
        quotation.setVendorId(dto.getVendorId());
       // quotation.setVendorName(dto.getVendorName());
        quotation.setQuotationFileName(dto.getQuotationFileName());
        quotation.setFileType(dto.getFileType());
        quotation.setCreatedBy(dto.getCreatedBy());
      VendorQuotationAgainstTender qu =  vendorQuotationAgainstTenderRepository.save(quotation);
        return mapToResponse(qu);
    }

    @Override
    public List<VendorQuotationAgainstTenderDto> getQuotationsByTenderId(String tenderId) {

       List<VendorQuotationAgainstTender> vqList= vendorQuotationAgainstTenderRepository.findByTenderId(tenderId);

        return vqList.stream().map(vq -> {
            VendorQuotationAgainstTenderDto dto = new VendorQuotationAgainstTenderDto();
            dto.setTenderId(vq.getTenderId());
            dto.setVendorId(vq.getVendorId());
            dto.setQuotationFileName(vq.getQuotationFileName());
            dto.setFileType(vq.getFileType());
            dto.setCreatedBy(vq.getCreatedBy());
            return dto;
        }).collect(Collectors.toList());
    }

    private VendorQuotationAgainstTenderDto mapToResponse(VendorQuotationAgainstTender dto) {
        VendorQuotationAgainstTenderDto quotation = new VendorQuotationAgainstTenderDto();
        quotation.setTenderId(dto.getTenderId());
        quotation.setVendorId(dto.getVendorId());
       // quotation.setVendorName(dto.getVendorName());
        quotation.setQuotationFileName(dto.getQuotationFileName());
        quotation.setFileType(dto.getFileType());
        quotation.setCreatedBy(dto.getCreatedBy());
        return quotation;
    }

    
}
