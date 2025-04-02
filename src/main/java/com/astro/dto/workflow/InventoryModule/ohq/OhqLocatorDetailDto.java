package com.astro.dto.workflow.InventoryModule.ohq;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OhqLocatorDetailDto {
    private Integer locatorId;
    private String locatorDesc;
    private BigDecimal quantity;
}