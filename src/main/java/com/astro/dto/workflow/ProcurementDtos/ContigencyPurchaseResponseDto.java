package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ContigencyPurchaseResponseDto {

    private Long ContigencyId;

    private String vendorsName;
    private String vendorsInvoiceNo;
    private String Date;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String remarksForPurchase;
    private BigDecimal amountToBePaid;
    private byte[] uploadCopyOfInvoice;
    private String predifinedPurchaseStatement;
    private String projectDetail;
    private String updatedBy;
    private String createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


}
