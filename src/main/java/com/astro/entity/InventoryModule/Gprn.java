package com.astro.entity.InventoryModule;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "GPRN")
public class Gprn {

    @Id
    @Column(name = "gprn_no")
    private String gprnNo;
    @Column(name = "po_Id")
    private String poId;
    private LocalDate date;
    @Column(name="delivery_challan_no")
    private String deliveryChallanNo;
    @Column(name = "delivery_challan_date")
    private LocalDate deliveryChallanDate;
    @Column(name="vendor_id")
    private String vendorId;
    @Column(name = "vendor_name")
    private String vendorName;
    private String vendorEmail;
    private Long vendorContactNo;
    @Column(name = "field_station")
    private String fieldStation;
    @Column(name="indentor_name")
    private String indentorName;
    @Column(name="expected_supply_date")
    private LocalDate expectedSupplyDate;
    @Column(name = "consignee_detail")
    private String consigneeDetail;
    private Integer warrantyYears;
    private String project;
    private String receivedQty;
    private String pendingQty;
    private String acceptedQty;
    @Lob
    private byte[] provisionalReceiptCertificate;

    private String provisionalReceiptCertificateFileName;

    @OneToMany(mappedBy = "gprn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GprnMaterials> gprnMaterials = new ArrayList<>();

    @Column(name = "received_by")
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
