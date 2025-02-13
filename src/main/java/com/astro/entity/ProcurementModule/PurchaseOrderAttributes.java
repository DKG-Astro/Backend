package com.astro.entity.ProcurementModule;

import com.astro.entity.ProcurementModule.PurchaseOrder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class PurchaseOrderAttributes {


    @Id
    @Column(name = "material_code")
    private String materialCode;
    @Column(name = "material_description")
    private String materialDescription;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "currency")
    private String currency;
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;
    @Column(name = "gst")
    private BigDecimal gst;
    @Column(name = "duties")
    private BigDecimal duties;
    @Column(name = "freight_charge")
    private BigDecimal freightCharge;
    @Column(name = "budget_code")
    private String budgetCode;
   // @ManyToOne
   // @JoinColumn(name = "purchase_order_id", referencedColumnName = "po_id")
   // private PurchaseOrder purchaseOrder;
   @ManyToMany(mappedBy = "purchaseOrderAttributes")
   private List<PurchaseOrder> purchaseOrders = new ArrayList<>();;
}
