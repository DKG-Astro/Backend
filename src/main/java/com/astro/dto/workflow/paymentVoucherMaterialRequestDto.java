package com.astro.dto.workflow;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class paymentVoucherMaterialRequestDto {

    private String grnNumber;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal gst;
    private BigDecimal paidAmount;
    private BigDecimal advanceAdjustedAmount;
}
