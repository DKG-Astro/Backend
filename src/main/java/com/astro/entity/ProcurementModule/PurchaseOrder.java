package com.astro.entity.ProcurementModule;

//import jakarta.persistence.*;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poId;

    private String tenderId;
    private String indentId;

    private BigDecimal warranty;
    private String consignesAddress;
    private String billingAddress;

    private BigDecimal deliveryPeriod;
    private  Boolean ifLdClauseApplicable;
    private String incoterms;
    @Column(name = "paymentterms")
    private String paymentterms;
    private String vendorName;
    private String vendorAddress;
    private String applicablePbgToBeSubmitted;
    @Column(name = "transposter_and_freight_for_warder_details ")
    private String transposterAndFreightForWarderDetails;
    private String vendorAccountNumber;
    private String vendorsZfscCode;
    private String vendorAccountName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_order_id")
    private List<PurchaseOrderAttributes> purchaseOrderAttributes;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();


}
