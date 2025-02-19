package com.astro.controller;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentReportDetailsDTO;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.service.IndentCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class Reports {
    @Autowired
    private IndentCreationService indentCreationService;

    @GetMapping("/indent")
    public List<IndentReportDetailsDTO> getIndentReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return indentCreationService.getIndentReport(startDate, endDate);
    }




}
