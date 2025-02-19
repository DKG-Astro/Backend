package com.astro.dto.workflow.ProcurementDtos.IndentDto;

import com.astro.entity.ProcurementModule.IndentCreation;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IndentReportDetailsDTO {

    private String indentId;
    private LocalDate approvedDate;
    private String assignedTo;
    private Long tenderRequest;
    private String modeOfProcurement;
    private Long poId;
    private String poStatus;
    private LocalDate submittedDate;
    private String pendingApprovalWith;
    private LocalDate poApprovedDate;
    private String material;
    private String materialCategory;
    private String materialSubCategory;
    private String vendorName;
    private String indentorName;
    private BigDecimal valueOfIndent;
    private BigDecimal valueOfPo;
    private String project;
    private String grinNo;
    private String invoiceNo;
    private String gissNo;
    private BigDecimal pendingPaymentValue;
    private String currentStageOfIndent;
    private String shortClosed;
    private String reasonForShortClosure;

}

