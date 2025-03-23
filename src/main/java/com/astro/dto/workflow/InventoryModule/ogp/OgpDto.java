package com.astro.dto.workflow.InventoryModule.ogp;

import lombok.Data;
import java.util.List;

@Data
public class OgpDto {
    private String ogpDate;
    private String issueNoteId;
    private String locationId;
    private Integer createdBy;
    private String ogpId;
    private List<OgpMaterialDtlDto> materialDtlList;
}