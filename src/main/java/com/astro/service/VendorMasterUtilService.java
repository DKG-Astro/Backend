package com.astro.service;

import com.astro.dto.workflow.ApprovalAndRejectionRequestDTO;
import com.astro.dto.workflow.VendorMasterResponseDto;
import com.astro.dto.workflow.VendorRegistrationRequestDTO;
import com.astro.dto.workflow.VendorRegistrationResponseDTO;
import com.astro.entity.VendorMasterUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VendorMasterUtilService {
    public VendorRegistrationResponseDTO registerVendor(VendorRegistrationRequestDTO dto);

    public List<VendorRegistrationResponseDTO> getAllAwaitingApprovalVendors();

    public String performAction(ApprovalAndRejectionRequestDTO request);

    public VendorRegistrationResponseDTO getVendorMasterUtilById(String vendorId);

}
