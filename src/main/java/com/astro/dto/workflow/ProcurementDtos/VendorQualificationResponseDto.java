package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;

@Data
public class VendorQualificationResponseDto {
    private String vendorId;
    private Boolean qualified;
    private String remarks;
}
