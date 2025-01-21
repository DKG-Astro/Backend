package com.astro.entity.InventoryModule;

import com.astro.entity.ProcurementModule.IndentCreation;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class GprnMaterials {
    //meterial data

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialCode;


    private String description;


    private String uom;


    private Integer orderedQuantity;

    private Integer quantityDelivered;


    private Integer receivedQuantity;


    private Double unitPrice;

    @Column(name = "net_price")
    private BigDecimal netPrice;

    private String makeNo;

    private String modelNo;

    private String serialNo;

    private String warranty;

    private String note;

    private String photographPath;
    @ManyToOne
    @JoinColumn(name = "gprn_id", referencedColumnName = "gprn_no")
    private Gprn gprn;




}
