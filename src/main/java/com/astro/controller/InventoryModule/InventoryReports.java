package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentReportDetailsDTO;
import com.astro.entity.InventoryModule.IssueRegisterDTO;
import com.astro.service.InventoryModule.GiService;
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
@RequestMapping("/api/Inventory/reports")
public class InventoryReports {


    @Autowired
    private GiService giService;

    @GetMapping("/issue-register")
    public ResponseEntity<Object> getissueRegisterReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<IssueRegisterDTO> response=giService.getIssueRegisterReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
}
