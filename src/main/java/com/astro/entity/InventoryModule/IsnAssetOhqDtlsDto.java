package com.astro.entity.InventoryModule;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class IsnAssetOhqDtlsDto {
    private Integer assetId;
    private String assetDesc;
    private String uomId;
    private BigDecimal unitPrice;

    private List<IsnOhqDtlsDto> qtyList;
}
