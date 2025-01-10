package com.astro.entity.ProcurementModule;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "service_order")
@Data
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenderId;
    private String consignesAddress;
    private String billingAddress;
    private BigDecimal jobCompletionPeriod;
    private Boolean ifLdClauseApplicable;
    private String incoTerms;
    private String paymentTerms;
    private String vendorName;
    private String vendorAddress;
    @Column(name = "applicable_pbg_to_be_submitted")
    private String applicablePBGToBeSubmitted;
    private String vendorsAccountNo;
    @Column(name = "vendors_zrsc_code")
    private String vendorsZRSCCode;
    private String vendorsAccountName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_order_id")
    private List<ServiceOrderMaterial> materials;
    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();



}
