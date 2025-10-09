package com.astro.dto.workflow.InventoryModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetOhqDetailsDto {


        private Integer ohqId;
        private Integer assetId;
        private String assetDesc;
        private String modelNo;
        private String serialNo;
        private String poId;
        private BigDecimal unitPrice;
        private BigDecimal quantity;
        private BigDecimal bookValue;
        private BigDecimal depriciationRate;
        private String custodianId;
        private Integer locatorId;

}
