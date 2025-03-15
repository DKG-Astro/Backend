package com.astro.entity.ProcurementModule;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
public class MaterialDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material_code", nullable = false, unique = true)
    private String materialCode;
    @Column(name = "indent_id")
    private String indentId;

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
    @Column(name="mode_of_procurement")
    private String modeOfProcurement;


  //  @ManyToOne
  //  @JoinColumn(name = "indent_creation_id", nullable = false)
   // @ManyToMany(mappedBy = "materialDetails")
  // Many-to-Many Relationship with IndentCreation

   // private IndentCreation indentCreation;
   @ManyToOne
   @JoinColumn(name = "indent_id", referencedColumnName = "indent_id", insertable = false, updatable = false)
   private IndentCreation indentCreation;



}
