package com.astro.entity.ProcurementModule;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data

public class IndentCreation {


        @Id
        @Column(name = "indentor_id", nullable = false, unique = true)
        private String indentorId;

        @Column(name = "indentor_name")
        private String indentorName;


        @Column(name = "indentor_mobile_no")
        private String indentorMobileNo;

        @Column(name = "indentor_email_address")
        private String indentorEmailAddress;

        @Column(name = "consignes_location")
        private String consignesLocation;
        @Lob
        @Column(name = "uploading_prior_approvals")
        private byte[] uploadingPriorApprovals;

        @Column(name = "project_name")
        private String projectName;

        @Lob
        @Column(name = "upload_tender_documents")
        private byte[] uploadTenderDocuments;

        @Column(name = "is_pre_bit_meeting_required")
        private Boolean isPreBitMeetingRequired;

        @Column(name = "pre_bid_meeting_date")
        private LocalDate preBidMeetingDate;

        @Column(name = "pre_bid_meeting_venue")
        private String preBidMeetingVenue;

        @Column(name = "is_it_a_rate_contract_indent")
        private Boolean isItARateContractIndent;

        @Column(name = "estimated_rate")
        private BigDecimal estimatedRate;

        @Column(name = "period_of_contract")
        private BigDecimal periodOfContract;

        @Column(name = "single_and_multiple_job")
        private String singleAndMultipleJob;

        @Lob
        @Column(name = "upload_goi_or_rfp")
        private byte[] uploadGOIOrRFP;

        @Lob
        @Column(name = "upload_pac_or_brand_pac")
        private byte[] uploadPACOrBrandPAC;

        @Column(name = "uploading_prior_approvals_file_name")
        private String uploadingPriorApprovalsFileName;
        @Column(name = "upload_tender_documents_file_name")
        private String uploadTenderDocumentsFileName;
        @Column(name = "upload_goi_or_rfp_file_name")
        private String uploadGOIOrRFPFileName;
        @Column(name = "upload_pac_or_brand_pac_file_name")
        private String uploadPACOrBrandPACFileName;

        @OneToMany(mappedBy = "indentCreation", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<MaterialDetails> materialDetails;

        @Column(name = "created_by")
        private String createdBy;

        @Column(name = "updated_by")
        private String updatedBy;

        @Column(name = "created_date")
        private LocalDateTime createdDate = LocalDateTime.now();

        @Column(name = "updated_date")
        private LocalDateTime updatedDate = LocalDateTime.now();
    }


