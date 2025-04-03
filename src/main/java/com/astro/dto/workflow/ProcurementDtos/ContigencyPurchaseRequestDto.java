package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;

@Data
public class ContigencyPurchaseRequestDto {

  //  private String contigencyId;
    private String vendorsName;
    private String vendorsInvoiceNo;
    private String Date;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String remarksForPurchase;
    private BigDecimal amountToBePaid;
   //private MultipartFile uploadCopyOfInvoice;
    private String uploadCopyOfInvoice;
    private String fileType;
    private String predifinedPurchaseStatement;
    private String projectDetail;
    private String projectName;
    private String updatedBy;
    private Integer createdBy;
}
