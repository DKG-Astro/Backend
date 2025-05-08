package com.astro.dto.workflow.ProcurementDtos;

import lombok.Data;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContigencyPurchaseResponseDto {

    private String contigencyId;

    private String vendorName;
    private String vendorInvoiceNo;
    private String Date;
    //private String materialCode;
    //  private String materialDescription;
    //  private BigDecimal quantity;
    //  private BigDecimal unitPrice;
    private String remarksForPurchase;
    //   private BigDecimal amountToBePaid;
    private String uploadCopyOfInvoice;
    private String fileType;
    private String predifinedPurchaseStatement;
    private String projectDetail;
    private String projectName;
    private String updatedBy;
    private Integer createdBy;
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
    private List<CpMaterialResponseDto> cpMaterials;


}
