package com.astro.dto.workflow;

import lombok.Data;

@Data
public class VendorQuotationAgainstTenderDto {

    private String tenderId;
    private String vendorId;
  //  private String vendorName;
    private String quotationFileName;
    private String fileType;
    private Integer createdBy;
}
