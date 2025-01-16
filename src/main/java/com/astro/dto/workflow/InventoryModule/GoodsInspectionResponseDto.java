package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GoodsInspectionResponseDto {

    private Long id;

    private String goodsInspectionNo;


    private String installationDate;

    private String commissioningDate;

    private String uploadInstallationReport;

    private int acceptedQuantity;

    private int rejectedQuantity;
    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
