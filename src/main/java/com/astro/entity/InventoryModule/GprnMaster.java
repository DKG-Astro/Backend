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
public class GprnMaster {

    @Id
    @Column(name = "process_id")
    private String processId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_process_id")
    private Integer subProcessId;

    @Column(name = "po_id")
    private String poId;

    @Column(name = "date")
    private Date date;

    @Column(name = "challan_no")
    private String challanNo;


    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "field_station")
    private String fieldStation;

    @Column(name = "indentor_name")
    private String indentorName;

    @Column(name = "supply_expected_date")
    private Date supplyExpectedDate;

    @Column(name = "consignee_detail")
    private String consigneeDetail;

    @Column(name = "warranty_years")
    private BigDecimal warrantyYears;

    @Column(name = "project")
    private String project;

    @Column(name = "received_by")
    private String receivedBy;

    @OneToMany(mappedBy = "gprnMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GprnMaterialDetails> materialDetails;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();

}
