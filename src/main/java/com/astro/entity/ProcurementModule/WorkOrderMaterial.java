package com.astro.entity.ProcurementModule;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class WorkOrderMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workCode;
    private String workDescription;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal exchangeRate;
    private String currency;
    private BigDecimal gst;
    private BigDecimal duties;
    private String budgetCode;

    @ManyToOne
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

}
