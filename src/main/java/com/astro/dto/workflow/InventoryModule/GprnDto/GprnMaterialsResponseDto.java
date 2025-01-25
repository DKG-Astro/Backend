package com.astro.dto.workflow.InventoryModule.GprnDto;

import lombok.Data;


import java.math.BigDecimal;


@Data
public class GprnMaterialsResponseDto {

    private Long materialCode;


    private String description;


    private String uom;


    private Integer orderedQuantity;

    private Integer quantityDelivered;


    private Integer receivedQuantity;


    private Double unitPrice;

    private BigDecimal netPrice;

    private String makeNo;

    private String modelNo;

    private String serialNo;

    private String warranty;

    private String note;

    private String photographPath;



}
