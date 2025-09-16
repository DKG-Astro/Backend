package com.astro.entity.InventoryModule;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "ogp_asset_disposal_detail")
@Data
public class OgpAssetDisposalDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ogp_disposal_detail_id")
    private Integer ogpDisposalDetailId;

    @Column(name = "disposal_ogp_id", nullable = false)
    private Integer disposalOgpId;

    @Column(name = "asset_id", nullable = false)
    private Integer assetId;

    @Column(name = "asset_desc", nullable = false)
    private String assetDesc;

    @Column(name = "disposal_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal disposalQuantity;

    @Column(name = "disposal_category", nullable = false)
    private String disposalCategory;

    @Column(name = "disposal_mode", nullable = false)
    private String disposalMode;

    @Column(name = "sales_note_filename")
    private String salesNoteFilename;

    private Integer ohqId;
    private Integer locatorId;
    private BigDecimal bookValue;
    private BigDecimal depriciationRate;
    private BigDecimal unitPrice;
    private String custodianId;
    private BigDecimal poValue;
}
