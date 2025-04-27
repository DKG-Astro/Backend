package com.astro.dto.workflow;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VendorRegiEmailResponseDTO {

    private String vendorId;
    private String vendorName;
    private String vendorType;
    private String contactNumber;
    private String emailAddress;
    private Boolean registeredPlatform;
    private String pfmsVendorCode;
    private String primaryBusiness;
    private String address;
    private String landlineNumber;
    private String mobileNumber;
    private String faxNumber;
    private String panNumber;
    private String gstNumber;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String approvalStatus;
    private String comments;
    private Boolean emailStatus;
    private String updatedBy;
    private Integer createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
