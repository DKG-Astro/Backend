package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_master")
@Data
public class JobMaster {

    @Id
    @Column(name = "job_code")
    private String jobCode;

    @Column(name = "category")
    private String category;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "uom")
    private String uom;

    @Column(name = "value")
    private BigDecimal value;

   // @Column(name = "mode_of_procurement")
  //  private String modeOfProcurement;

   // @OneToMany(mappedBy = "jobCode", cascade = CascadeType.ALL, orphanRemoval = true)
   // private List<VendorNamesForJobWorkMaterial> vendorNames;

    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "updated_by")
    private String updatedBy;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();


}
