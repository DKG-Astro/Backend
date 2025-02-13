package com.astro.dto.workflow;

import lombok.Data;

@Data
public class VendorContractReportDTO {

    private Integer orderId;
    private String modeOfProcurement;
    private String underAmc;
    private String amcExpiryDate;
    private String amcFor;
    private String endUser;
    private Integer noOfParticipants;
    private Double value;
    private String location;
    private String vendorName;
    private String previouslyRenewedAmcs;
    private String categoryOfSecurity;
    private String validityOfSecurity;

}
