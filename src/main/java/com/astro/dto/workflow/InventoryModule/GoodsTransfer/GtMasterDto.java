package com.astro.dto.workflow.InventoryModule.GoodsTransfer;

import java.util.List;

import lombok.Data;

@Data
public class GtMasterDto {
    private String id;
    private String senderLocationId;
    private String receiverLocationId;
    private Integer receiverCustodianId;
    private Integer senderCustodianId;
    private String gtDate;
    private Integer createdBy;
    private List<GtDtl> materialDtlList;
}
