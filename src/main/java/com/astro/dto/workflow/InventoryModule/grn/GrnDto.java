package com.astro.dto.workflow.InventoryModule.grn;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GrnDto {
    private String grnType;
    private String igpId;
    private String giNo;
    private String grnNo;
    private String installationDate;
    private String commissioningDate;
    private String grnDate;
    private String createdBy;
    private Integer systemCreatedBy;
    private String locationId;
    private String custodianId;
    private String custodianName;
    private Boolean storesStock;
    private List<String> giNos;
    private List<GrnMaterialDtlDto> materialDtlList;

    private BigDecimal grnAmount;
}
