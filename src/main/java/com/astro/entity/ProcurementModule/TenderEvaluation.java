package com.astro.entity.ProcurementModule;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tender_evaluation")
@Data
public class TenderEvaluation {
   // @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
   // private Long tenderEvaluationId;
    @Id
    private String tenderId;
    private String uploadQualifiedVendorsFileName;
    private String uploadTechnicallyQualifiedVendorsFileName;
    private String uploadCommeriallyQualifiedVendorsFileName;
    private String formationOfTechnoCommerialComitee;
    private String responseFileName;
    private String responseForTechnicallyQualifiedVendorsFileName;
    private String responseForCommeriallyQualifiedVendorsFileName;
    private String fileType;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();




}
