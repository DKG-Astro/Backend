package com.astro.service.impl;

import com.astro.dto.workflow.VendorQuotationAgainstTenderDto;
import com.astro.dto.workflow.VendorStatusDto;
import com.astro.entity.VendorMaster;
import com.astro.entity.VendorMasterUtil;
import com.astro.entity.VendorQuotationAgainstTender;
import com.astro.repository.VendorMasterRepository;
import com.astro.repository.VendorMasterUtilRepository;
import com.astro.repository.VendorQuotationAgainstTenderRepository;
import com.astro.service.VendorQuotationAgainstTenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorQuotationAgainstTenderServiceImpl implements VendorQuotationAgainstTenderService {

    @Autowired
    private VendorQuotationAgainstTenderRepository vendorQuotationAgainstTenderRepository;
    @Autowired
    private VendorMasterRepository vendorMasterRepository;
    @Autowired
    private VendorMasterUtilRepository vendorMasterUtilRepository;

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

    @Override
    public VendorStatusDto getVendorStatus(String vendorId) {

        VendorStatusDto dto = new VendorStatusDto();
        dto.setVendorId(vendorId);

        // Checking for approved vendors
        Optional<VendorMaster> approved = vendorMasterRepository.findByVendorId(vendorId);
        if (approved.isPresent()) {
            VendorMaster vm = approved.get();
            dto.setStatus(vm.getStatus());
            dto.setComments("Vendor is approved.");
            return dto;
        }

        // Checking for rejected/awaiting vendors
        Optional<VendorMasterUtil> pendingOrRej = vendorMasterUtilRepository.findByVendorId(vendorId);
        if (pendingOrRej.isPresent()) {
            VendorMasterUtil vendor = pendingOrRej.get();
            dto.setStatus(vendor.getApprovalStatus().name()); // rejected or awaiting for approval
            dto.setComments(vendor.getComments());
            return dto;
        }

        // Not found anywhere
        dto.setStatus("NOT_FOUND");
        dto.setComments("Vendor ID is not available in the system.");
        return dto;
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
