package com.astro.entity.InventoryModule;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String receivedBy;
//meterial data
    @Column(nullable = false)
    private String materialCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String uom;

    @Column(nullable = false)
    private Integer orderedQuantity;

    private Integer quantityDelivered;

    @Column(nullable = false)
    private Integer receivedQuantity;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(name = "net_price", insertable = false, updatable = false)
    private BigDecimal netPrice;

    private String makeNo;

    private String modelNo;

    private String serialNo;

    private String warranty;

    private String note;

    private String photographPath;

    private String createdBy;

    private String updatedBy;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();

}
