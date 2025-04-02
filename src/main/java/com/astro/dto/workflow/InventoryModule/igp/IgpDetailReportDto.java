package com.astro.dto.workflow.InventoryModule.igp;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class IgpDetailReportDto {
    private Integer detailId;
    private Integer assetId;
    private String assetDesc;
    private String materialDesc;
    private Integer locatorId;
    private String locatorDesc;
    private BigDecimal quantity;
    private String uomId;
}