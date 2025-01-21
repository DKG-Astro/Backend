package com.astro.entity.InventoryModule;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "goods_inspection")
public class GoodsInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_inspection_no")
    private Long goodsInspectionNo;

    @Column(name = "gpr_id")
    private Long gprId; // Foreign key to Good Provisional Receipt entity

    @Column(name = "installation_date")
    private LocalDate installationDate;

    @Column(name = "commissioning_date")
    private LocalDate commissioningDate;

    @Lob
    @Column(name = "upload_installation_report")
    private String uploadInstallationReport;

    @Column(name = "accepted_quantity", nullable = false)
    private int acceptedQuantity;

    @Column(name = "rejected_quantity", nullable = false)
    private int rejectedQuantity;

    @Column(name="goods_return_permament_or_replacement")
    private String goodsReturnPermamentOrReplacement;
    @Column(name="goods_return_full_or_partial")
    private String goodsReturnFullOrPartial;
    @Column(name="goods_return_reason")
    private String goodsReturnReason;

    @Column(name = "material_rejection_advice_sent")
    private Boolean materialRejectionAdviceSent = false;

    @Column(name = "po_amendment_notified")
    private Boolean poAmendmentNotified = false;

    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate = LocalDateTime.now();
}
