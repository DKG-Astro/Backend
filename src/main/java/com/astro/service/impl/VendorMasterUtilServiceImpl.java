package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ApprovalAndRejectionRequestDTO;
import com.astro.dto.workflow.VendorRegiEmailResponseDTO;
import com.astro.dto.workflow.VendorRegistrationRequestDTO;
import com.astro.dto.workflow.VendorRegistrationResponseDTO;
import com.astro.entity.*;
import com.astro.exception.BusinessException;
import com.astro.exception.EmailNotSentException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.*;
import com.astro.service.VendorMasterUtilService;
import com.astro.util.EmailService;
import com.astro.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorMasterUtilServiceImpl implements VendorMasterUtilService {

    @Autowired
    private VendorMasterUtilRepository vendorMasterUtilRepository;
    @Autowired
    private UserRoleMasterRepository userRoleMasterRepository;
    @Autowired
    private VendorMasterRepository vendorMasterRepository;
    @Autowired
    private VendorIdSequenceRepository vendorIdSequenceRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VendorLoginDetailsRepository vendorLoginDetailsRepository;
    @Override
    public VendorRegiEmailResponseDTO registerVendor(VendorRegistrationRequestDTO dto) {
        VendorMasterUtil vendor = new VendorMasterUtil();

      //  Integer maxNumber = vendorMasterUtilRepository.findMaxVendorNumber();
        Integer maxNumber = vendorIdSequenceRepository.findMaxVendorId();
        int nextNumber = (maxNumber == null) ? 1001 : maxNumber + 1;

        String vendorId = "V" + nextNumber;

     // String vendorId = "V" + System.currentTimeMillis();
        VendorIdSequence vendorIdSequence = new VendorIdSequence();
        vendorIdSequence.setVendorId(nextNumber);
        vendorIdSequenceRepository.save(vendorIdSequence);
        vendor.setVendorId(vendorId);
        vendor.setVendorNumber(nextNumber);
        vendor.setVendorName(dto.getVendorName());
        vendor.setVendorType(dto.getVendorType());
        vendor.setContactNumber(dto.getContactNumber());
        vendor.setEmailAddress(dto.getEmailAddress());
        vendor.setRegisteredPlatform(dto.getRegisteredPlatform());
        vendor.setPfmsVendorCode(dto.getPfmsVendorCode());
        vendor.setPrimaryBusiness(dto.getPrimaryBusiness());
        vendor.setAddress(dto.getAddress());
        vendor.setLandlineNumber(dto.getLandlineNumber());
        vendor.setMobileNumber(dto.getMobileNumber());
        vendor.setFaxNumber(dto.getFaxNumber());
        vendor.setPanNumber(dto.getPanNumber());
        vendor.setGstNumber(dto.getGstNumber());
        vendor.setBankName(dto.getBankName());
        vendor.setAccountNumber(dto.getAccountNumber());
        vendor.setIfscCode(dto.getIfscCode());
        vendor.setApprovalStatus(VendorMasterUtil.ApprovalStatus.AWAITING_APPROVAL); // Default Status
        vendor.setComments(null);
        vendor.setCreatedBy(dto.getCreatedBy());
        vendor.setUpdatedBy(dto.getUpdatedBy());

       VendorMasterUtil vm = vendorMasterUtilRepository.save(vendor);

        String password = PasswordGenerator.generateRandomPassword();
        try {
            //sending email to vendor
            emailService.sendEmail(vm.getEmailAddress(), vm.getVendorId(), password);

            VendorLoginDetails vendorLoginDetails = new VendorLoginDetails();
            vendorLoginDetails.setVendorId(vm.getVendorId());
            vendorLoginDetails.setEmailAddress(vm.getEmailAddress());
            vendorLoginDetails.setPassword(password);
            vendorLoginDetails.setEmailSent(true); //sent mail to vendor
            vendorLoginDetailsRepository.save(vendorLoginDetails);
        } catch (IOException e) {
            String errorMessage = "Failed to send email to vendor: " + vm.getEmailAddress();
            VendorLoginDetails vendorLoginDetails = new VendorLoginDetails();
            vendorLoginDetails.setVendorId(vm.getVendorId());
            vendorLoginDetails.setEmailAddress(vm.getEmailAddress());
            vendorLoginDetails.setPassword(password);
            vendorLoginDetails.setEmailSent(false); // failed while sending mail to vendor

            vendorLoginDetailsRepository.save(vendorLoginDetails);
            throw new EmailNotSentException(errorMessage);
        }
        return mapToRigisterResponse(vendor);
    }

    private VendorRegiEmailResponseDTO mapToRigisterResponse(VendorMasterUtil vendor) {

        VendorRegiEmailResponseDTO vendorResponse = new VendorRegiEmailResponseDTO();

        vendorResponse.setVendorId(vendor.getVendorId());
        vendorResponse.setVendorName(vendor.getVendorName());
        vendorResponse.setVendorType(vendor.getVendorType());
        vendorResponse.setContactNumber(vendor.getContactNumber());
        vendorResponse.setEmailAddress(vendor.getEmailAddress());
        vendorResponse.setRegisteredPlatform(vendor.getRegisteredPlatform());
        vendorResponse.setPfmsVendorCode(vendor.getPfmsVendorCode());
        vendorResponse.setPrimaryBusiness(vendor.getPrimaryBusiness());
        vendorResponse.setAddress(vendor.getAddress());
        vendorResponse.setLandlineNumber(vendor.getLandlineNumber());
        vendorResponse.setMobileNumber(vendor.getMobileNumber());
        vendorResponse.setFaxNumber(vendor.getFaxNumber());
        vendorResponse.setPanNumber(vendor.getPanNumber());
        vendorResponse.setGstNumber(vendor.getGstNumber());
        vendorResponse.setBankName(vendor.getBankName());
        vendorResponse.setAccountNumber(vendor.getAccountNumber());
        vendorResponse.setIfscCode(vendor.getIfscCode());
        vendorResponse.setApprovalStatus(vendor.getApprovalStatus().name());
        vendorResponse.setCreatedBy(vendor.getCreatedBy());
        vendorResponse.setUpdatedBy(vendor.getUpdatedBy());
        vendorResponse.setCreatedDate(vendor.getCreatedDate());
        vendorResponse.setUpdatedDate(vendor.getUpdatedDate());
        vendorResponse.setComments(vendor.getComments());
       Optional<VendorLoginDetails> vendorLogin = vendorLoginDetailsRepository.findByVendorId(vendor.getVendorId());
       VendorLoginDetails vl = vendorLogin.get();
       vendorResponse.setEmailStatus(vl.getEmailSent());


        return vendorResponse;

    }

    @Override
    public List<VendorRegistrationResponseDTO> getAllAwaitingApprovalVendors() {

       List<VendorMasterUtil> vendors =vendorMasterUtilRepository.findByApprovalStatus(VendorMasterUtil.ApprovalStatus.AWAITING_APPROVAL);

        return vendors.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    private VendorRegistrationResponseDTO mapToResponse(VendorMasterUtil vendor) {

        VendorRegistrationResponseDTO vendorResponse = new VendorRegistrationResponseDTO();

        vendorResponse.setVendorId(vendor.getVendorId());
        vendorResponse.setVendorName(vendor.getVendorName());
        vendorResponse.setVendorType(vendor.getVendorType());
        vendorResponse.setContactNumber(vendor.getContactNumber());
        vendorResponse.setEmailAddress(vendor.getEmailAddress());
        vendorResponse.setRegisteredPlatform(vendor.getRegisteredPlatform());
        vendorResponse.setPfmsVendorCode(vendor.getPfmsVendorCode());
        vendorResponse.setPrimaryBusiness(vendor.getPrimaryBusiness());
        vendorResponse.setAddress(vendor.getAddress());
        vendorResponse.setLandlineNumber(vendor.getLandlineNumber());
        vendorResponse.setMobileNumber(vendor.getMobileNumber());
        vendorResponse.setFaxNumber(vendor.getFaxNumber());
        vendorResponse.setPanNumber(vendor.getPanNumber());
        vendorResponse.setGstNumber(vendor.getGstNumber());
        vendorResponse.setBankName(vendor.getBankName());
        vendorResponse.setAccountNumber(vendor.getAccountNumber());
        vendorResponse.setIfscCode(vendor.getIfscCode());
        vendorResponse.setApprovalStatus(vendor.getApprovalStatus().name());
        vendorResponse.setCreatedBy(vendor.getCreatedBy());
        vendorResponse.setUpdatedBy(vendor.getUpdatedBy());
        vendorResponse.setCreatedDate(vendor.getCreatedDate());
        vendorResponse.setUpdatedDate(vendor.getUpdatedDate());
        vendorResponse.setComments(vendor.getComments());

        return vendorResponse;

    }


    @Override
    public String performAction(ApprovalAndRejectionRequestDTO request) {
        // Validateing the user roleId = 11 (Store Purchase Officer)
        UserRoleMaster userRoleMaster = userRoleMasterRepository.findByRoleIdAndUserId(11, request.getActionBy());

        if (Objects.isNull(userRoleMaster)) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.UNAUTHORIZED_ACTION,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Unauthorized user"
            ));
        }

        // Fetch vendor (Ensure it exists)
        VendorMasterUtil vendor = vendorMasterUtilRepository
                .findById(request.getRequestId())
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION,
                        "Vendor ID not found!"
                )));


        if ("APPROVED".equalsIgnoreCase(request.getAction())) {
            int actionBy =request.getActionBy();
            String remarks = request.getRemarks();
            return approveVendor(vendor, actionBy, remarks);
        } else if ("REJECTED".equalsIgnoreCase(request.getAction())) {
            return rejectVendor(vendor, request.getRemarks());
        } else if ("CHANGE REQUEST".equalsIgnoreCase(request.getAction())) {
            return changeRequestVendor(vendor, request.getRemarks());

        } else {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.INVALID_ACTION,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid action. Use 'APPROVED' or 'REJECTED'."
            ));
        }
    }

    @Override
    public String performAllAction(List<ApprovalAndRejectionRequestDTO> request) {

        String response =null;
        for(ApprovalAndRejectionRequestDTO dto: request){
            response=performAction(dto);
        }
        return response;
    }

    @Override
    public VendorRegistrationResponseDTO getVendorMasterUtilById(String vendorId) {
        VendorMasterUtil vendorMasterUtil= vendorMasterUtilRepository.findById(vendorId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Vendor master Util not found for the provided vendor id.")
                ));
        return mapToResponse(vendorMasterUtil);
    }

    private String changeRequestVendor(VendorMasterUtil vendor, String remarks) {

        vendor.setApprovalStatus(VendorMasterUtil.ApprovalStatus.CHANGE_REQUEST);
        vendor.setComments(remarks);

        vendorMasterUtilRepository.save(vendor);
        return "Vendor " + vendor.getVendorId() + " Change Requested.";

    }


    private String approveVendor(VendorMasterUtil vendor,int actionBy, String remarks ) {
        VendorMaster newVendor = new VendorMaster();
        newVendor.setVendorId(vendor.getVendorId());
        newVendor.setVendorType(vendor.getVendorType());
        newVendor.setVendorName(vendor.getVendorName());
        newVendor.setContactNo(vendor.getContactNumber());
        newVendor.setEmailAddress(vendor.getEmailAddress());
        newVendor.setRegisteredPlatform(vendor.getRegisteredPlatform());
        newVendor.setPfmsVendorCode(vendor.getPfmsVendorCode());
        newVendor.setPrimaryBusiness(vendor.getPrimaryBusiness());
        newVendor.setAddress(vendor.getAddress());
        newVendor.setLandline(vendor.getLandlineNumber());
        newVendor.setMobileNo(vendor.getMobileNumber());
        newVendor.setFax(vendor.getFaxNumber());
        newVendor.setPanNo(vendor.getPanNumber());
        newVendor.setGstNo(vendor.getGstNumber());
        newVendor.setBankName(vendor.getBankName());
        newVendor.setAccountNo(vendor.getAccountNumber());
        newVendor.setIfscCode(vendor.getIfscCode());
        newVendor.setPurchaseHistory(null);
        newVendor.setStatus("APPROVED");
        newVendor.setCreatedBy(actionBy);
        newVendor.setRemarks(remarks);

        //Saveing approved vendor id into VendorMaster
        vendorMasterRepository.save(newVendor);
        vendorMasterUtilRepository.deleteById(vendor.getVendorId());
        return "Vendor " + vendor.getVendorId() + " has been APPROVED and added the vendor data to vendor master, deleted from vendor master util.";
    }

    private String rejectVendor(VendorMasterUtil vendor, String remarks) {

        vendor.setApprovalStatus(VendorMasterUtil.ApprovalStatus.REJECTED);
        vendor.setComments(remarks);

        vendorMasterUtilRepository.save(vendor);
        return "Vendor " + vendor.getVendorId() + " has been REJECTED.";
    }



}
