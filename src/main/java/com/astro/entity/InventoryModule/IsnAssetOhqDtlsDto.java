package com.astro.entity.InventoryModule;

import java.util.List;

import lombok.Data;

@Data
public class IsnAssetOhqDtlsDto {
    private Integer assetId;
    private String assetDesc;
    private String uomId;

    private List<IsnOhqDtlsDto> qtyList;
}
