package com.astro.dto.workflow;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobMasterRequestDto {

   // private String jobCode;
    private String category;
    private String jobDescription;
    private String assetId;
    private String uom;
    private BigDecimal value;
    private String modeOfProcurement;
    private List<String> vendorNames;
    private String updatedBy;
    private Integer createdBy;
}
