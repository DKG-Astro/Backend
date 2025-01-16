package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

@Data
public class GoodsInspectionRequestDto {
    private String goodsInspectionNo;
    private String installationDate;
    private String commissioningDate;
    private String uploadInstallationReport;
    private int acceptedQuantity;
    private int rejectedQuantity;
    private String updatedBy;
    private String createdBy;

}