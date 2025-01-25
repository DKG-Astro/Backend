package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "material_master")
@Data
public class MaterialMaster {

    @Id
    @Column(name = "material_code")
    private String materialCode;

    @Column(name = "category")
    private String category;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "description")
    private String description;

    @Column(name = "uom")
    private String uom;

    @Column(name = "mode_of_procurement")
    private String modeOfProcurement;

    @Column(name = "end_of_life")
    private String endOfLife;

    @Column(name = "depreciation_rate")
    private BigDecimal depreciationRate;

    @Column(name = "stock_levels_min")
    private BigDecimal stockLevelsMin;

    @Column(name = "stock_levels_max")
    private BigDecimal stockLevelsMax;

    @Column(name = "re_order_level")
    private BigDecimal reOrderLevel;

    @Column(name = "condition_of_goods")
    private String conditionOfGoods;

    @Column(name = "shelf_life")
    private String shelfLife;

    @Lob
    @Column(name = "upload_image")
    private byte[] uploadImage;

    @Column(name = "indigenous_or_imported")
    private Boolean indigenousOrImported;


}
