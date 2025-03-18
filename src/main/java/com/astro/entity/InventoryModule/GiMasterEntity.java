package com.astro.entity.InventoryModule;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Entity
@Table(name = "goods_inspection_master")
@Data
public class GiMasterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_sub_process_id")
    private Integer inspectionSubProcessId;
    
    @Column(name = "gprn_process_id", nullable = false)
    private String gprnProcessId;
    
    @Column(name = "gprn_sub_process_id", nullable = false)
    private Integer gprnSubProcessId;
    
    @Column(name = "installation_date")
    private LocalDate installationDate;
    
    @Column(name = "commissioning_date")
    private LocalDate commissioningDate;

    @Column(name="create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="location_id")
    private String locationId;
}