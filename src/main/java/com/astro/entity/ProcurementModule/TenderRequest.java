package com.astro.entity.ProcurementModule;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class TenderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleOfTender;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String indentId;
    private String indentMaterials;
    private String modeOfProcurement;
    private String bidType;
    private LocalDate lastDateOfSubmission;
    private String applicableTaxes;
    private String consignesAndBillinngAddress;
    private String incoTerms;
    private String paymentTerms;
    private String ldClause;
    private String applicablePerformance;
    private Boolean bidSecurityDeclaration;
    private Boolean mllStatusDeclaration;
    @Lob
    private byte[] uploadTenderDocuments;
    private String singleAndMultipleVendors;
    @Lob
    private byte[] uploadGeneralTermsAndConditions;
    @Lob
    private byte[] uploadSpecificTermsAndConditions;
    private String preBidDisscussions;
    @Column(name = "updated_by")
    private String updatedBy;
    private String createdBy;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();


}
