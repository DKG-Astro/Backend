package com.astro.service;



import com.astro.dto.workflow.RegisteredVendorsDataDto;
import com.astro.dto.workflow.VendorContractReportDTO;
import com.astro.dto.workflow.VendorMasterRequestDto;
import com.astro.dto.workflow.VendorMasterResponseDto;

import java.util.List;

public interface VendorMasterService {

    public VendorMasterResponseDto createVendorMaster(VendorMasterRequestDto vendorMasterRequestDto);
    public VendorMasterResponseDto updateVendorMaster(String vendorId, VendorMasterRequestDto vendorMasterRequestDto);
    public List<VendorMasterResponseDto> getAllVendorMasters();

    public VendorMasterResponseDto getVendorMasterById(String vendorId);
    public void deleteVendorMaster(String vendorId);

   public List<VendorMasterResponseDto> getAllNotApprovedVendors();

    public List<RegisteredVendorsDataDto> getVendorPurchaseOrders(String vendorId);


 //   List<VendorContractReportDTO> getVendorContracts(String startDate, String endDate);
}
