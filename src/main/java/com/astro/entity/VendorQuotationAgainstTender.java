package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "created_by")
    private Integer createdBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
}
