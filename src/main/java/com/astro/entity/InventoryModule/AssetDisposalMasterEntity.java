package com.astro.entity.InventoryModule;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_disposal")
@Data
public class AssetDisposalMasterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disposal_id")
    private Integer disposalId;
    
    @Column(name = "disposal_date", nullable = false)
    private LocalDate disposalDate;
    
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;
    
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;
    
    @Column(name = "location_id", nullable = false)
    private String locationId;
    
    @Column(name = "vendor_id")
    private String vendorId;
}