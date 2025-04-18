package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GoodsInspectionResponseDto {


    private Long goodsInspectionNo;


    private String installationDate;

    private String commissioningDate;

    private String uploadInstallationReport;

    private int acceptedQuantity;

    private int rejectedQuantity;
    private String goodsReturnPermamentOrReplacement;
    private String goodsReturnFullOrPartial;
    private String goodsReturnReason;

    private Boolean materialRejectionAdviceSent;


    private Boolean poAmendmentNotified;
    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
