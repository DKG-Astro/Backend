package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GprnResponseDto {

    private Long gprnNo;


    private String poNo;


    private String date;

    private String deliveryChallanNo;


    private String deliveryChallanDate;


    private String vendorId;

    private String vendorName;

    private String vendorEmail;

    private Long vendorContactNo;

    private String fieldStation;

    private String indentorName;

    private String expectedSupplyDate;

    private String consigneeDetail;

    private Integer warrantyYears;

    private String project;


    private String receivedBy;

    private String materialCode;


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

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
