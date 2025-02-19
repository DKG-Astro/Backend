package com.astro.entity.ProcurementModule;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Data
public class PurchaseOrder {


  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "po_id")
    private String poId;
    @Column(name = "tender_id")
    private String tenderId;
    @Column(name = "indent_id")
    private String indentId;
    @Column(name = "warranty")
    private BigDecimal warranty;
    @Column(name = "consignes_address")
    private String consignesAddress;
    @Column(name = "billing_address")
    private String billingAddress;
    @Column(name = "delivery_period")
    private BigDecimal deliveryPeriod;
    @Column(name = "if_ld_clause_applicable")
    private  Boolean ifLdClauseApplicable;
    @Column(name = "inco_terms")
    private String incoTerms;
    @Column(name = "payment_terms")
    private String paymentTerms;
    @Column(name = "vendor_name")
    private String vendorName;
    @Column(name = "vendor_address")
    private String vendorAddress;
    @Column(name = "applicable_pbg_to_be_submitted")
    private String applicablePbgToBeSubmitted;
    @Column(name = "transporter_and_freight_for_warder_details")
    private String transporterAndFreightForWarderDetails;
    @Column(name = "vendor_account_number")
    private String vendorAccountNumber;
    @Column(name = "vendors_zfsc_code")
    private String vendorsZfscCode;
    @Column(name = "vendor_account_name")
    private String vendorAccountName;
    @Column(name = "total_value_of_po")
    private BigDecimal totalValueOfPo;
    @Column(name = "project_name")
    private String projectName;

   // @OneToMany(cascade = CascadeType.ALL)
   // @JoinColumn(name = "purchase_order_id")
   @ManyToMany(cascade = CascadeType.PERSIST)
   @JoinTable(
           name = "purchase_order_attributes_mapping",
           joinColumns = @JoinColumn(name = "po_id"),
           inverseJoinColumns = @JoinColumn(name = "material_code")
   )
    private List<PurchaseOrderAttributes> purchaseOrderAttributes;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
}
