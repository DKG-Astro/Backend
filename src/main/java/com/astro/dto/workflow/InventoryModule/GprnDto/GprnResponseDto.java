package com.astro.dto.workflow.InventoryModule.GprnDto;

import com.astro.util.Base64ToByteArrayConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    private String receivedQty;

    private String pendingQty;
    private String acceptedQty;
    @JsonDeserialize(converter = Base64ToByteArrayConverter.class)
    private byte[] provisionalReceiptCertificate;

    private String receivedBy;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


    private List<GprnMaterialsResponseDto> gprnMaterialsResponsetDtos =Collections.emptyList();
}
