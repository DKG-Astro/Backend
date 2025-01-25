package com.astro.entity.ProcurementModule;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class MaterialDetails {


  //  @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
   // private Long id;

    @Id
    @Column(name = "material_code", nullable = false, unique = true)
    private String materialCode;

    @Column(name = "material_description")
    private String materialDescription;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "uom")
    private String uom;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "budget_code")
    private String budgetCode;

    @Column(name = "material_category")
    private String materialCategory;

    @Column(name = "material_sub_category")
    private String materialSubCategory;

    @Column(name = "material_and_job")
    private String materialAndJob;


    @ManyToOne
    @JoinColumn(name = "indent_creation_id", nullable = false)
    private IndentCreation indentCreation;


}
