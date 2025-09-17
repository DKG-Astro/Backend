package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AutionAssetsDisposalsDto {
    private Integer disposalDetailId;
    private Integer disposalId;
    private Integer assetId;
    private String assetDesc;
    private BigDecimal disposalQuantity;
    private Integer locatorId;
    private BigDecimal bookValue;
    private BigDecimal depriciationRate;
    private BigDecimal unitPrice;
    private String custodianId;
    private BigDecimal poValue;

    private String reasonForDisposal;

    private String disposalDate;

    private String locationId;

    private String status;

}
