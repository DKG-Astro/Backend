package com.astro.controller;

import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseReportDto;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentListReportDto;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentReportDetailsDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ApprovedSoListReportDto;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.PendingSoReportDto;
import com.astro.dto.workflow.ProcurementDtos.purchaseOrder.*;
import com.astro.repository.InventoryModule.AssetMasterRepository;
import com.astro.dto.workflow.ProcurementDtos.ProcurementActivityReportResponse;
import com.astro.dto.workflow.ProcurementDtos.TechnoMomReportDTO;
import com.astro.dto.workflow.VendorContractReportDTO;
import com.astro.dto.workflow.InventoryModule.AssetMasterDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpReportDto;
import com.astro.dto.workflow.InventoryModule.isn.IsnReportDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpReportDto;
import com.astro.dto.workflow.InventoryModule.ohq.OhqReportDto;
import com.astro.service.*;
import com.astro.service.InventoryModule.AssetMasterService;
import com.astro.service.InventoryModule.IgpService;
import com.astro.service.InventoryModule.IsnService;
import com.astro.service.InventoryModule.OgpService;
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
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private IsnService isnService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private OgpService  ogpService;

    @Autowired
    private IgpService igpService;
    @Autowired
    private ServiceOrderService serviceOrderService;

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


    @GetMapping("/vendor-contracts/report")
    public ResponseEntity<Object>  getVendorContracts(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        List<VendorContractReportDTO> response = purchaseOrderService.getVendorContractDetails(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping("procurement-activity-report")
    public ResponseEntity<Object> getProcurementActivityReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<ProcurementActivityReportResponse> response = purchaseOrderService.getProcurementActivityReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("poList-report")
    public ResponseEntity<Object> getPoListReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<ApprovedPoListReportDto> response = purchaseOrderService.getApprovedPoReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("soList-report")
    public ResponseEntity<Object> getSoListReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<ApprovedSoListReportDto> response = serviceOrderService.getApprovedSoListReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("ShortClosedCancelledOrderReport")
    public ResponseEntity<Object> getShortClosedCancelledOrderReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<ShortClosedCancelledOrderReportDto> response = purchaseOrderService.getShortClosedCancelledOrders(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("QuarterlyVigilanceReport")
    public ResponseEntity<Object> getQuarterlyVigilanceReport() {

        List<QuarterlyVigilanceReportDto> response = purchaseOrderService.getQuarterlyVigilanceReport();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("pending-po-report")
    public ResponseEntity<Object> getPendingPoReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<pendingPoReportDto> response = purchaseOrderService.getPendingPoReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("MonthlyProcurementReport")
    public ResponseEntity<Object> getMonthlyProcurementReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<MonthlyProcurementReportDto> response = purchaseOrderService.getMonthlyProcurementReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }
    @GetMapping("pending-so-report")
    public ResponseEntity<Object> getPendingSoReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<PendingSoReportDto> response = serviceOrderService.getPendingSoReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }

    @GetMapping("indentList-report")
    public ResponseEntity<Object> getIndentListReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<IndentListReportDto> response = indentCreationService.getAllIndentsReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);

    }

    @GetMapping("/isn")
    public ResponseEntity<Object> getIsnReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<IsnReportDto> response = isnService.getIsnReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping("/ogp")
    public ResponseEntity<Object> getOgpReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<OgpReportDto> response = ogpService.getOgpReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping("/igp")
    public ResponseEntity<Object> getIgpReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<IgpReportDto> response = igpService.getIgpReport(startDate, endDate);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping("/stock")
    public ResponseEntity<Object> getOhqReport() {

        List<OhqReportDto> response = processService.getOhqReport();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    @GetMapping("/asset")
    public ResponseEntity<Object> getAssetReport() {

        List<AssetMasterDto> response = assetMasterService.getAssetReport();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(response), HttpStatus.OK);
    }
    

}
