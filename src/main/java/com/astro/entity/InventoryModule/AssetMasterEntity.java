package com.astro.entity.InventoryModule;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "asset_master")
@Data
public class AssetMasterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assetId;
    
    private String materialCode;
    private String materialDesc;
    private String assetDesc;
    private String makeNo;
    private String serialNo;
    private String modelNo;
    private String uomId;
    private String componentName;
    private Integer componentId;
    private BigDecimal initQuantity;
    
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
    
    private Integer createdBy;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    private Integer updatedBy;
}