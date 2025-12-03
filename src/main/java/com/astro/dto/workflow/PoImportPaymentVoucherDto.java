package com.astro.dto.workflow;

import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.PurchaseOrderAttributesDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PoImportPaymentVoucherDto {

    private String poId;
    private String vendorId;
    private String vendorName;
    private BigDecimal poValue;
    private List<PurchaseOrderAttributesDTO> materials;
}
