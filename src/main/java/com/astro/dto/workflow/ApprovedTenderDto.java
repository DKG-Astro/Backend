package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApprovedTenderDto {

    private String tenderId;
    private String bidType;
    private BigDecimal totalValue;
    public ApprovedTenderDto(String tenderId, String bidType, BigDecimal totalValue) {
        this.tenderId = tenderId;
        this.bidType = bidType;
        this.totalValue = totalValue;
    }


}
