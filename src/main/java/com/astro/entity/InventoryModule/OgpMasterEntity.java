package com.astro.entity.InventoryModule;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ogp_master")
@Data
public class OgpMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ogp_sub_process_id")
    private Integer ogpSubProcessId;

    @Column(name = "ogp_process_id")
    private String ogpProcessId;

    @Column(name = "issue_note_id")
    private Integer issueNoteId;

    @Column(name = "ogp_date")
    private LocalDate ogpDate;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}
