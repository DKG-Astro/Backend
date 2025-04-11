package com.astro.dto.workflow.InventoryModule.ogp;

import java.util.List;

import lombok.Data;

@Data
public class OgpPoDto {
    private String poId;
    private String ogpDate;
    private String locationId;
    private Integer createdBy;
    private List<OgpPoDtlDto> materialDtlList;
}
