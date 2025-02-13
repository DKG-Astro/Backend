package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.VendorContractReportDTO;
import com.astro.dto.workflow.VendorMasterRequestDto;
import com.astro.dto.workflow.VendorMasterResponseDto;
import com.astro.entity.VendorMaster;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.VendorMasterRepository;
import com.astro.service.VendorMasterService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorMasterServiceImpl implements VendorMasterService {

    @Autowired
    private VendorMasterRepository vendorMasterRepository;

    @Override
    public VendorMasterResponseDto createVendorMaster(VendorMasterRequestDto vendorMasterRequestDto) {

        // Check if the indentorId already exists
        if (vendorMasterRepository.existsById(vendorMasterRequestDto.getVendorId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate vendor id", "vendor id" + vendorMasterRequestDto.getVendorId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

        VendorMaster vendorMaster = new VendorMaster();
        vendorMaster.setVendorId(vendorMasterRequestDto.getVendorId());
        vendorMaster.setVendorType(vendorMasterRequestDto.getVendorType());
        vendorMaster.setVendorName(vendorMasterRequestDto.getVendorName());
        vendorMaster.setContactNo(vendorMasterRequestDto.getContactNo());
        vendorMaster.setEmailAddress(vendorMasterRequestDto.getEmailAddress());
        vendorMaster.setRegisteredPlatform(vendorMasterRequestDto.getRegisteredPlatform());
        vendorMaster.setPfmsVendorCode(vendorMasterRequestDto.getPfmsVendorCode());
        vendorMaster.setPrimaryBusiness(vendorMasterRequestDto.getPrimaryBusiness());
        vendorMaster.setAddress(vendorMasterRequestDto.getAddress());
        vendorMaster.setLandline(vendorMasterRequestDto.getLandline());
        vendorMaster.setMobileNo(vendorMasterRequestDto.getMobileNo());
        vendorMaster.setFax(vendorMasterRequestDto.getFax());
        vendorMaster.setPanNo(vendorMasterRequestDto.getPanNo());
        vendorMaster.setGstNo(vendorMasterRequestDto.getGstNo());
        vendorMaster.setBankName(vendorMasterRequestDto.getBankName());
        vendorMaster.setAccountNo(vendorMasterRequestDto.getAccountNo());
        vendorMaster.setIfscCode(vendorMasterRequestDto.getIfscCode());
        vendorMaster.setPurchaseHistory(vendorMasterRequestDto.getPurchaseHistory());
        vendorMaster.setStatus(vendorMasterRequestDto.getStatus());
        vendorMaster.setCreatedBy(vendorMasterRequestDto.getCreatedBy());
        vendorMaster.setUpdatedBy(vendorMasterRequestDto.getUpdatedBy());


        vendorMasterRepository.save(vendorMaster);

        return mapToResponseDTO(vendorMaster);

    }



    @Override
    public VendorMasterResponseDto updateVendorMaster(String vendorId, VendorMasterRequestDto vendorMasterRequestDto) {

        VendorMaster vendorMaster = vendorMasterRepository.findById(vendorId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "vendor master not found for the provided vendor id.")
                ));

        vendorMaster.setVendorId(vendorMasterRequestDto.getVendorId());
        vendorMaster.setVendorType(vendorMasterRequestDto.getVendorType());
        vendorMaster.setVendorName(vendorMasterRequestDto.getVendorName());
        vendorMaster.setContactNo(vendorMasterRequestDto.getContactNo());
        vendorMaster.setEmailAddress(vendorMasterRequestDto.getEmailAddress());
        vendorMaster.setRegisteredPlatform(vendorMasterRequestDto.getRegisteredPlatform());
        vendorMaster.setPfmsVendorCode(vendorMasterRequestDto.getPfmsVendorCode());
        vendorMaster.setPrimaryBusiness(vendorMasterRequestDto.getPrimaryBusiness());
        vendorMaster.setAddress(vendorMasterRequestDto.getAddress());
        vendorMaster.setLandline(vendorMasterRequestDto.getLandline());
        vendorMaster.setMobileNo(vendorMasterRequestDto.getMobileNo());
        vendorMaster.setFax(vendorMasterRequestDto.getFax());
        vendorMaster.setPanNo(vendorMasterRequestDto.getPanNo());
        vendorMaster.setGstNo(vendorMasterRequestDto.getGstNo());
        vendorMaster.setBankName(vendorMasterRequestDto.getBankName());
        vendorMaster.setAccountNo(vendorMasterRequestDto.getAccountNo());
        vendorMaster.setIfscCode(vendorMasterRequestDto.getIfscCode());
        vendorMaster.setPurchaseHistory(vendorMasterRequestDto.getPurchaseHistory());
        vendorMaster.setStatus(vendorMasterRequestDto.getStatus());
        vendorMaster.setCreatedBy(vendorMasterRequestDto.getCreatedBy());
        vendorMaster.setUpdatedBy(vendorMasterRequestDto.getUpdatedBy());

        vendorMasterRepository.save(vendorMaster);

        return mapToResponseDTO(vendorMaster);
    }

    @Override
    public List<VendorMasterResponseDto> getAllVendorMasters() {
        List<VendorMaster> vendorMasters= vendorMasterRepository.findAll();
        return vendorMasters.stream().map(this::mapToResponseDTO).collect(Collectors.toList());

    }

    @Override
    public VendorMasterResponseDto getVendorMasterById(String vendorId) {
       VendorMaster vendorMaster= vendorMasterRepository.findById(vendorId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Vendor master not found for the provided vendor id.")
                ));
        return mapToResponseDTO(vendorMaster);
    }

    @Override
    public void deleteVendorMaster(String vendorId) {


        VendorMaster vendorMaster=vendorMasterRepository.findById(vendorId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Vendor master not found for the provided Vendor ID."
                        )
                ));
        try {
            vendorMasterRepository.delete(vendorMaster);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the Vendor master."
                    ),
                    ex
            );
        }

    }

    @Override
    public List<VendorContractReportDTO> getVendorContracts(String startDate, String endDate) {
        List<Object[]> results    =vendorMasterRepository.findVendorContracts(CommonUtils.convertStringToDateObject(startDate),CommonUtils.convertStringToDateObject(endDate));
        return results.stream().map(row -> {
          VendorContractReportDTO dto =  new VendorContractReportDTO();
                   dto.setOrderId ((Integer) row[0]); // orderId
                    dto.setModeOfProcurement((String) row[1]);  // modeOfProcurement
                    dto.setUnderAmc((String) row[2]); // underAmc
                    dto.setAmcExpiryDate((String) row[3]);  // amcExpiryDate
                    dto.setAmcFor((String) row[4]); // amcFor
                   dto.setEndUser((String) row[5]);  // endUser
                    dto.setNoOfParticipants((row[6] != null) ? ((Number) row[6]).intValue() : null); // noOfParticipants
                    dto.setValue((Double) row[7]);  // value
                    dto.setLocation((String) row[8]);  // location
                    dto.setVendorName((String) row[9]);  // vendorName
                    dto.setPreviouslyRenewedAmcs((String) row[10]); // previouslyRenewedAmcs
                    dto.setCategoryOfSecurity((String) row[11]); // categoryOfSecurity
                    dto.setValidityOfSecurity((String) row[12]); // validityOfSecurity
            return dto;
        }).collect(Collectors.toList());

    }

    private VendorMasterResponseDto mapToResponseDTO(VendorMaster vendorMaster) {

        VendorMasterResponseDto responseDto = new VendorMasterResponseDto();

        responseDto.setVendorId(vendorMaster.getVendorId());
        responseDto.setVendorType(vendorMaster.getVendorType());
        responseDto.setVendorName(vendorMaster.getVendorName());
        responseDto.setContactNo(vendorMaster.getContactNo());
        responseDto.setEmailAddress(vendorMaster.getEmailAddress());
        responseDto.setRegisteredPlatform(vendorMaster.getRegisteredPlatform());
        responseDto.setPfmsVendorCode(vendorMaster.getPfmsVendorCode());
        responseDto.setPrimaryBusiness(vendorMaster.getPrimaryBusiness());
        responseDto.setAddress(vendorMaster.getAddress());
        responseDto.setLandline(vendorMaster.getLandline());
        responseDto.setMobileNo(vendorMaster.getMobileNo());
        responseDto.setFax(vendorMaster.getFax());
        responseDto.setPanNo(vendorMaster.getPanNo());
        responseDto.setGstNo(vendorMaster.getGstNo());
        responseDto.setBankName(vendorMaster.getBankName());
        responseDto.setAccountNo(vendorMaster.getAccountNo());
        responseDto.setIfscCode(vendorMaster.getIfscCode());
        responseDto.setPurchaseHistory(vendorMaster.getPurchaseHistory());
        responseDto.setStatus(vendorMaster.getStatus());
        responseDto.setCreatedBy(vendorMaster.getCreatedBy());
        responseDto.setUpdatedBy(vendorMaster.getUpdatedBy());
        responseDto.setCreatedDate(vendorMaster.getCreatedDate());
        responseDto.setUpdatedDate(vendorMaster.getUpdatedDate());

        return responseDto;


    }
}
