package com.astro.dto.workflow;

import com.astro.util.Base64ToByteArrayConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.Lob;
import java.sql.Blob;
import java.time.LocalDate;

@Data
public class TenderRequestDto {

    private String titleOfTender;
    private String openingDate;
    private String closingDate;
    private String indentId;
    private String indentMaterials;
    private String modeOfProcurement;
    private String bidType;
    private String lastDateOfSubmission;
    private String applicableTaxes;
    private String consignesAndBillinngAddress;
    private String incoTerms;
    private String paymentTerms;
    private String ldClause;
    private String applicablePerformance;
    private Boolean bidSecurityDeclaration;
    private Boolean mllStatusDeclaration;
    @JsonDeserialize(converter = Base64ToByteArrayConverter.class)
    private byte[] uploadTenderDocuments;
    private String singleAndMultipleVendors;
    @JsonDeserialize(converter = Base64ToByteArrayConverter.class)
    private byte[] uploadGeneralTermsAndConditions;
    @JsonDeserialize(converter = Base64ToByteArrayConverter.class)
    private byte[] uploadSpecificTermsAndConditions;
    private String preBidDisscussions;
    private String updatedBy;
    private String createdBy;


}
