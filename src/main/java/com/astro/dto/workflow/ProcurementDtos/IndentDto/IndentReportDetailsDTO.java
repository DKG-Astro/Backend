package com.astro.dto.workflow.ProcurementDtos.IndentDto;


import lombok.Data;
import java.util.Date;

@Data
public class IndentReportDetailsDTO {

    private String indentId;
    private Date approvedDate;
    private String assignedTo;
    private String tenderRequest;
    private String modeOfTendering;
    private String correspondingPoSo;
    private String statusOfPoSo;
    private Date submittedDate;
    private String pendingApprovalWith;
    private Date poSoApprovedDate;
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
    private String currentStageOfIndent;
    private String shortClosedAndCancelled;
    private String reasonForShortClosure;

}

