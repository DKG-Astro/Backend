package com.astro.dto.workflow.ProcurementDtos.IndentDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialDetailsRequestDTO {

    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String uom;
   // private BigDecimal totalPrice;
    private String budgetCode;
    private String materialCategory;
    private String materialSubCategory;
    private String materialAndJob;
    private String modeOfProcurement;
}
