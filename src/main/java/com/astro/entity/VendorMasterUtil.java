package com.astro.entity;
import lombok.Data;
import org.apache.logging.log4j.message.StringFormattedMessage;

import javax.persistence.*;
import java.time.LocalDateTime;

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
   //private String purchaseHistory;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    private String comments;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "updated_by")
    private String updatedBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();

    public enum ApprovalStatus {

        APPROVED,
        REJECTED,
        AWAITING_APPROVAL,
        CHANGE_REQUEST

    }


}
