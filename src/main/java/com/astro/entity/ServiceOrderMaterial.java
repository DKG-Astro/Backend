package com.astro.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_order_material")
@Data
public class ServiceOrderMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materialCode;
    private String materialDescription;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal exchangeRate;
    private String currency;
    private BigDecimal gst;
    private BigDecimal duties;
    private String budgetCode;

    @ManyToOne
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;



}
