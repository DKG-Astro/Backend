package com.astro.dto.workflow.ProcurementDtos.IndentDto;

import com.astro.entity.ProcurementModule.IndentCreation;
import lombok.Data;

@Data
public class IndentReportDetailsDTO {

    private String indentId;
    private String approvedDate;
    private String assignedTo;
    private String tenderRequest;
    private String modeOfTendering;
    private String correspondingPoSo;
    private String statusOfPoSo;
    private String submittedDate;
    private String pendingApprovalWith;
    private String poSoApprovedDate;
    private String material;
    private String materialCategory;
    private String materialSubCategory;
    private String vendorName;
    private String indentorName;
    private Double valueOfIndent;
    private Double valueOfPo;
    private String project;
    private String grinNo;
    private String invoiceNo;
    private String gissNo;
    private Double valuePendingToBePaid;
    private String currentStageOfTheIndent;
    private String shortClosedCancelled;
    private String reasonForShortClosureCancellation;

}

