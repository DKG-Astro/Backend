package com.astro.entity.InventoryModule;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class GprnMaterials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gprn_no", nullable = false)
    private String gprnNo;

    @Column(name = "material_code")
    private String materialCode;
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

    @Lob
    private byte[] photographPath;

    private String photoFileName;

    @ManyToOne
    @JoinColumn(name = "gprn_no", referencedColumnName = "gprn_no", insertable = false, updatable = false)
    @JsonIgnore
    private Gprn gprn;




}
