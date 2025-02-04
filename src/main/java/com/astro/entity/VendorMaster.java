package com.astro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vendor_master")
public class VendorMaster {


    @Id
    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "vendor_type")
    private String vendorType;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "registered_platform")
    private String registeredPlatform;

    @Column(name = "pfms_vendor_code")
    private String pfmsVendorCode;

    @Column(name = "primary_business")
    private String primaryBusiness;

    @Column(name = "address")
    private String address;

    @Column(name = "landline")
    private String landline;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "fax")
    private String fax;

    @Column(name = "pan_no")
    private String panNo;

    @Column(name = "gst_no")
    private String gstNo;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "purchase_history")
    private String purchaseHistory;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();



}
