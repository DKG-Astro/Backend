package com.astro.dto.workflow.InventoryModule.GprnDto;



import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.util.List;

@Data
public class saveGprnDto {

    private String poId;
    private String date;
    private String challanNo;
    private String deliveryDate;
    private String vendorId;
    private String fieldStation;
    private String indentorName;
    private String supplyExpectedDate;
    private String consigneeDetail;
    private Integer warrantyYears;
    private String project;
    private String receivedBy;
    private Integer createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<MaterialDtlList> materialDtlLists;
}
