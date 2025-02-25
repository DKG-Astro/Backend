package com.astro.entity.ProcurementModule;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IndentId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "indent_id")
    private String indentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tender_id", nullable = false)
    private TenderRequest tenderRequest;

}
