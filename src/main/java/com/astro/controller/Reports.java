package com.astro.controller;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseReportDto;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentReportDetailsDTO;
import com.astro.dto.workflow.ProcurementDtos.TechnoMomReportDTO;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.service.ContigencyPurchaseService;
import com.astro.service.IndentCreationService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ContigencyPurchaseService CPservice;

    @GetMapping("/indent")
    public ResponseEntity<Object> getIndentReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
     List<IndentReportDetailsDTO>  response=indentCreationService.getIndentReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("/cp/report")
    public ResponseEntity<Object> getContigencyPurchaseReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<ContigencyPurchaseReportDto> response = CPservice.getContigencyPurchaseReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping("/techNoMom/report")
    public ResponseEntity<Object> getTechnoMomReport(
            @RequestParam String startDate,
            @RequestParam String endDate){
        List<TechnoMomReportDTO> response=indentCreationService.getTechnoMomReport(startDate,endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }



}
