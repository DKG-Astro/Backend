package com.astro.entity.InventoryModule;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "gprn_master")
public class GprnMasterEntity {

    @Column(name = "process_id")
    private String processId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_process_id")
    private Integer subProcessId;

    @Column(name = "po_id")
    private String poId;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "challan_no")
    private String challanNo;


    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "field_station")
    private String fieldStation;

    @Column(name = "indentor_name")
    private String indentorName;

    @Column(name = "supply_expected_date")
    private LocalDate supplyExpectedDate;

    @Column(name = "consignee_detail")
    private String consigneeDetail;

    @Column(name="status")
    private String status;

    @Column(name = "warranty_years")
    private BigDecimal warrantyYears;

    @Column(name = "warranty")
    private String warranty;

    @Column(name = "project")
    private String project;

    @Column(name = "received_by")
    private String receivedBy;

    // @OneToMany(mappedBy = "gprnMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<GprnMaterialDtlEntity> materialDetails;
    
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updateDate = LocalDateTime.now();

}
