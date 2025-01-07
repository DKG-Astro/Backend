package com.astro.entity.InventoryModule;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data

public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String assetCode;

    @Column(unique = true, nullable = false)
    private String materialCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String uom;

    private String makeNo;
    private String modelNo;
    private String serialNo;
    private String componentName;
    private String componentCode;
    private int quantity;

    @Column(nullable = false)
    private String locator;

    private String transactionHistory;

    @Column(nullable = false)
    private String currentCondition;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();


}
