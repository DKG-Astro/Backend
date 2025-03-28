package com.astro.dto.workflow;

import lombok.Data;

@Data
public class VendorRegistrationRequestDTO {

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

}
