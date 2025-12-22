package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payment_voucher_grn")
@Data
public class PaymentVoucherGrn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grn_number", nullable = false)
    private String grnNumber;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "advance_adjusted")
    private BigDecimal advanceAdjusted;
    @Column(name = "payment_voucher_type")
    private String paymentVouchertype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_voucher_id")
    private PaymentVoucher paymentVoucher;
}
