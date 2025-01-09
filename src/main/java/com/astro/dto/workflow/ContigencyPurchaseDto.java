package com.astro.dto.workflow;

import com.astro.util.Base64ToByteArrayConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContigencyPurchaseDto {

    private String vendorsName;
    private String vendorsInvoiceNo;
    private String Date;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String remarksForPurchase;
    private BigDecimal amountToBePaid;
    @JsonDeserialize(converter = Base64ToByteArrayConverter.class)
    private byte[] uploadCopyOfInvoice;
    private String predifinedPurchaseStatement;
    private String projectDetail;
    private String updatedBy;

    private String createdBy;
}
