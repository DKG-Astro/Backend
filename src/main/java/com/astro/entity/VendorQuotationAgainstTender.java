package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Vendor_quotation_against_tender")
public class VendorQuotationAgainstTender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenderId;
    private String vendorId;
 //   private String vendorName;
    private String quotationFileName;
    private String fileType;

    @Column(name = "status")
    private String status;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "version")
    private Integer version;

    @Column(name = "is_latest")
    private Boolean isLatest;


    @Column(name = "created_by")
    private Integer createdBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
}
