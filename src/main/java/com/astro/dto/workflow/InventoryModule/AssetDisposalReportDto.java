package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssetDisposalReportDto {

    private Long id;
    private String locationId;
    private String status;
    private Integer custodianId;
    private LocalDateTime createDate;
    private LocalDate disposalDate;
    private Integer createdBy;
    private String action;
    private String auctionId;
    private LocalDate auctionDate;
    private BigDecimal reservePrice;
    private BigDecimal auctionPrice;
    private String vendorName;
    private List<AssetDisposalMaterialDto> materialDtos;
}
