package com.astro.entity;

import jdk.jfr.Label;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "contigency_purchase")
public class ContigencyPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ContigencyId;

    private String vendorsName;
    private String vendorsInvoiceNo;
    private LocalDate Date;
    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String remarksForPurchase;
    private BigDecimal amountToBePaid;
    @Lob
    private byte[] uploadCopyOfInvoice;
    private String predifinedPurchaseStatement;
    private String projectDetail;
    private String updateBy;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate = LocalDateTime.now();






}
