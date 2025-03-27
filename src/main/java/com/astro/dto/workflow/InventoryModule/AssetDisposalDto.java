package com.astro.dto.workflow.InventoryModule;

import java.util.List;

import lombok.Data;

@Data
public class AssetDisposalDto {
    private String disposalDate;
    private Integer createdBy;
    private String locationId;
    private String vendorId;
    private List<AssetDisposalDetailDto> materialDtlList;
}