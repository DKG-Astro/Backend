package com.astro.entity.InventoryModule;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grv_master")
@Data
public class GrvMasterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer grvSubProcessId;
    
    private Integer giSubProcessId;
    private String giProcessId;
    private String grvProcessId;
    private LocalDate date;
    private String createdBy;
    private String locationId;
    
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
}