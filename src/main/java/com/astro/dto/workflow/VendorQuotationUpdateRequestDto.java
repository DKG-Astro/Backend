package com.astro.dto.workflow;

import lombok.Data;

@Data
public class VendorQuotationUpdateRequestDto {

    private String tenderId;
    private String vendorId;
    private String status;
    private String remarks;


}
