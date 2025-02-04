package com.astro.dto.workflow.InventoryModule.GprnDto;



import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.util.List;

@Data
public class GprnRequestDto {

    private String gprnNo;
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
    private MultipartFile provisionalReceiptCertificate;
    private String receivedBy;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

  //  private List<GprnMaterialsRequestDto> gprnMaterialsRequest =Collections.emptyList();
    private List<GprnMaterialsRequestDto> gprnMaterials;
}
