package com.astro.dto.workflow.InventoryModule;

import lombok.Data;

@Data
public class GoodsReturnDto {

    private String goodsReturnNoteNo;
    private Integer rejectedQuantity;
    private Integer returnQuantity;
    private String typeOfReturn;
    private String reasonOfReturn;
}
