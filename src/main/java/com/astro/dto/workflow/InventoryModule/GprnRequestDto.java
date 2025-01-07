package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

import java.util.List;

@Data
public class GprnRequestDto {

    private String poNo;
    private String date;
    private String deliveryChallanNo;
    private String deliveryChallanDate;
    private String vendorId;
    private String vendorName;
    private String fieldStation;
    private String indentorName;
    private String expectedSupplyDate;
    private String consigneeDetail;
    private String receivedBy;
    private String materialCode;
    private String description;
    private String uom;
    private Integer orderedQuantity;
    private Integer quantityDelivered;
    private Integer receivedQuantity;
    private Double unitPrice;
    private Double netPrice;
    private String makeNo;
    private String modelNo;
    private String serialNo;
    private String warranty;
    private String note;
    private String photographPath;

}
