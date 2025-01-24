package com.astro.entity;

import lombok.Data;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
public class MaterialCreation {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_code")
    private String materialCode;
    @Column(name = "material_name")
    private String materialName;
    @Column(name = "material_description")
    private String materialDescription;
    @Column(name = "mode_of_procurement")
    private String modeOfProcurement;
    @Column(name = "material_category")
    private String materialCategory;
    @Column(name = "material_sub_category")
    private String materialSubCategory;
    @Column(name = "uom")
    private String uom;
    @Column(name = "end_of_life")
    private BigDecimal endOfLife;
    @Column(name = "book_value")
    private BigDecimal bookValue;
    @Column(name = "deprication_rate")
    private BigDecimal depricationRate;
    @Column(name = "indegenious_or_imported")
    private String IndegeniousOrImported;
    @Column(name = "min_level")
    private BigDecimal minLevel;
    @Column(name = "max_level")
    private BigDecimal maxLevel;
    @Column(name = "re_order_level")
    private BigDecimal reOrderLevel;
    @Column(name = "condition_of_material")
    private String conditionOfMaterial;
    @Column(name = "locator")
    private String locator;
    @Column(name = "shelf")
    private String shelf;
    @Column(name = "`rank`")
    private String rank;
    @Column(name = "zone")
    private String zone;
    @Column(name = "building")
    private String building;
    @Lob
    @Column(name = "upload_image")
    private byte[] uploadImage;
    @Column(name = "shelf_life")
    private BigDecimal shelfLife;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();

}
