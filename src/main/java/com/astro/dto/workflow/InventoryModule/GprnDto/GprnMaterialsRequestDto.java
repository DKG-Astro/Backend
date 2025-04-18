package com.astro.dto.workflow.InventoryModule.GprnDto;

import lombok.Data;




@Data
public class GprnMaterialsRequestDto {

 private String description;


    private String uom;


    private Integer orderedQuantity;

    private Integer quantityDelivered;


    private Integer receivedQuantity;


    private Double unitPrice;


   // private BigDecimal netPrice;

    private String makeNo;

    private String modelNo;

    private String serialNo;

    private String warranty;

    private String note;

    private String photographPath;




}
