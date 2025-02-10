package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContigencyPurchaseReportDto {

    private String id;
    private String material;
    private String materialCategory;
    private String materialSubCategory;
    private String endUser;
    private BigDecimal value;
    private String paidTo;
    private String vendorName;
    private String project;

}
