package com.astro.entity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vendor_master_util")
public class VendorMasterUtil {

    @Id
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
   // private String purchaseHistory;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    private String comments;

    public enum ApprovalStatus {

        APPROVED,
        REJECTED,
        AWAITING_APPROVAL,
        CHANGE_REQUEST

    }


}
