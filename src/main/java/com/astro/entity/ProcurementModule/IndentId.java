package com.astro.entity.ProcurementModule;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IndentId {

    @Id
    @Column(name = "indent_id")
    private String indentId;

    @ManyToOne
    @JoinColumn(name = "tender_id", nullable = false)
    private TenderRequest tenderRequest;
}
