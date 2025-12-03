package com.astro.dto.workflow.InventoryModule.GiDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
@Data
@AllArgsConstructor
public class GprnIdsWithCreateByForGi {

        private Integer subProcessId;
        private String gprnNo;
        private String poId;
        private String vendorId;
        //  private List<String> materialDescriptions;
        private Set<String> materialDescriptions;  // UNIQUE materials
        private Set<Integer> indentIds;            // all indent IDs mapped to this PO
        private Set<Integer> indentorUserIds;



}
