package com.astro.entity.InventoryModule;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "GPRN")
public class Gprn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gprn_no")
    private Long gprnNo;

    @Column(nullable = false)
    private String poNo;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String deliveryChallanNo;

    @Column(nullable = false)
    private LocalDate deliveryChallanDate;

    @Column(nullable = false)
    private String vendorId;

    @Column(nullable = false)
    private String vendorName;

    private String vendorEmail;

    private Long vendorContactNo;

    @Column(nullable = false)
    private String fieldStation;

    @Column(nullable = false)
    private String indentorName;

    @Column(nullable = false)
    private LocalDate expectedSupplyDate;

    @Column(nullable = false)
    private String consigneeDetail;

    private Integer warrantyYears;

    private String project;

    private String receivedQty;

    private String pendingQty;
    private String acceptedQty;

    @Lob
    private byte[] provisionalReceiptCertificate;



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "gprn_id")  // Updated to reflect the new column name in the GprnMaterials table
    private List<GprnMaterials> gprnMaterials;

    @Column(nullable = false)
    private String receivedBy;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();

}
