package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.List;

@Data
public class ContigencyPurchaseRequestDto {

    //  private String contigencyId;
    private String vendorName;
    private String vendorInvoiceNo;
    private String Date;
    //  private String materialCode;
    //  private String materialDescription;
    //   private BigDecimal quantity;
    //   private BigDecimal unitPrice;
    private String remarksForPurchase;
    //  private BigDecimal amountToBePaid;
    //private MultipartFile uploadCopyOfInvoice;
    private String uploadCopyOfInvoice;
    private String fileType;
    private String predifinedPurchaseStatement;
    private String projectDetail;
    private String projectName;
    private String paymentTo;
    private String paymentToVendor;
    private String paymentToEmployee;
    private String updatedBy;
    private Integer createdBy;
    private List<CpMaterialRequestDto> cpMaterials;
}
