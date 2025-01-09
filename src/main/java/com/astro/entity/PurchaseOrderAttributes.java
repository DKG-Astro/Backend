package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class PurchaseOrderAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal rate;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal gst;
    private BigDecimal duties;
    private BigDecimal freightCharge;
    private String budgetCode;
    @ManyToOne()
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
}
