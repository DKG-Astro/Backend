package com.astro.dto.workflow.ProcurementDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class performanceWarrsntySecurityReportDto {
    private String poId;
    private LocalDateTime createdDate;
    private String modeOfProcurement;
    private String vendorName;
    private String titleOfTender;
    private BigDecimal totalValueOfPo;

    private String typeOfSecurity;
    private String securityNumber;
    private LocalDate securityDate;
    private LocalDate expiryDate;


}
