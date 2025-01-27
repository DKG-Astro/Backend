package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import javax.persistence.Column;

@Data
public class GoodsInspectionRequestDto {
    private String goodsInspectionNo;
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
    private String updatedBy;
    private String createdBy;

}
