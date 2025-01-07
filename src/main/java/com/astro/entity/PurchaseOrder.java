package com.astro.entity;

//import jakarta.persistence.*;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_order")
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poId;

    @Column(nullable = false)
    private String tenderRequests; // Comma-separated tender requests

    @Column(nullable = false)
    private String correspondingIndents; // Comma-separated indent IDs

    @Column(nullable = false)
    private String materialDescription;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitRate;

    @Column(nullable = false)
    private String currency;

    private BigDecimal exchangeRate;

    @Column(nullable = false)
    private BigDecimal gstPercentage;
    @Column(nullable = false)
    private BigDecimal dutiesPercentage;

    private BigDecimal freightCharges;

    private LocalDate deliveryPeriod;

    private String warranty;

    private String consigneeAddress;

    private String additionalTermsAndConditions;
    private String updatedBy;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();


}
