package com.astro.dto.workflow.InventoryModule.GprnDto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class MaterialDtlList {
   private String materialCode;
   private String materialDesc;
   private String uomId;
   private BigDecimal receivedQuantity;
   private BigDecimal unitPrice;
   private String makeNo;
   private String modelNo;
   private String serialNo;
   private String warrantyTerms;
   private String note;
   private String photo;

}



