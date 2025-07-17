package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GiDto.GiApprovalDto;
import com.astro.dto.workflow.InventoryModule.GiDto.GiWorkflowStatusDto;
import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.dto.workflow.InventoryModule.gprn.GprnPendingInspectionDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import com.astro.dto.workflow.InventoryModule.grn.UpdateGrnDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpCombinedDetailDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpDetailReportDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpReportDto;
import com.astro.dto.workflow.InventoryModule.isn.IsnDto;
import com.astro.dto.workflow.InventoryModule.ogp.GprApprovalDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoResponseDto;
import com.astro.entity.InventoryModule.GiMasterEntity;
import com.astro.entity.InventoryModule.GrnMasterEntity;
import com.astro.entity.InventoryModule.IsnAssetOhqDtlsDto;
import com.astro.service.InventoryModule.GrnService;
import com.astro.service.ProcessService;
import com.astro.service.InventoryModule.GiService;
import com.astro.service.InventoryModule.IgpService;
import com.astro.service.impl.InventoryModule.GiServiceImpl;
import com.astro.util.ResponseBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/process-controller")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private GiService gis;

    @Autowired
    private IgpService igpService;
    @Autowired
    private GrnService grns;

    @PostMapping("/saveGprn")
    public ResponseEntity<Object> saveGprn(@RequestBody SaveGprnDto req) {
        String processNo = processService.saveGprn(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/saveGi")
    public ResponseEntity<Object> saveGi(@RequestBody SaveGiDto req) {
        String processNo = processService.saveGi(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @GetMapping("/getSubProcessDtls")
    public ResponseEntity<Object> getSubProcessDtls(@RequestParam String processStage, @RequestParam String processNo ) {
       Object created = processService.getSubProcessDtls(processStage, processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }

    @PostMapping("/saveGrv")
    public ResponseEntity<Object> saveGrv(@RequestBody GrvDto req) {
        String processNo = processService.saveGrv(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/saveIgp")
    public ResponseEntity<Object> saveGrn(@RequestBody IgpDto req) {
        String processNo = processService.saveIgp(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    @PostMapping("/saveOgp")
    public ResponseEntity<Object> saveGrn(@RequestBody OgpDto req) {
        String processNo = processService.saveOgp(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    @PostMapping("/saveIsn")
    public ResponseEntity<Object> saveIsn(@RequestBody IsnDto req) {
        String processNo = processService.saveIsn(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/saveGrn")
    public ResponseEntity<Object> saveGrn(@RequestBody GrnDto req) {
        String processNo = processService.saveGrn(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/savePoOgp")
    public ResponseEntity<Object> savePoOgp(@RequestBody OgpPoDto req) {
        String processNo = processService.savePoOgp(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @GetMapping("/getIsnAssetOhqDtls")
    public ResponseEntity<Object> getIsnAssetOhqDtls() {
        List<IsnAssetOhqDtlsDto> res = processService.getIsnAssetOhqDtls();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    
    @GetMapping("/getGiStatusWise")
    public ResponseEntity<Object> getGiStatusWise(@RequestParam String status, @RequestParam Optional<String> createdBy) {
        List<GprnPendingInspectionDto> res = gis.getGiStatusWise(status, createdBy);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }


    @GetMapping("/getPendingGprn")
    public ResponseEntity<Object> getPendingGprn() {
        List<String> pendingGprnList = processService.getPendingGprn();
        Map<String, List<String>> res = new HashMap<>();
        res.put("pendingGprnList", pendingGprnList);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    @GetMapping("/getPoOgp")
    public ResponseEntity<Object> getPoOgp(@RequestParam String processNo) {
        OgpPoResponseDto res = processService.getPoOgp(processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    @GetMapping("/getGatePassReport")
    public ResponseEntity<Object> getGatePassReport() {
        List<IgpCombinedDetailDto> res = igpService.getIgpDetails();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    
    @PostMapping("/approveOgp")
    public ResponseEntity<Object> approveOgp(@RequestBody GprApprovalDto req) {
        processService.approveOgp(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "OGP approved successfully");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/rejectOgp")
    public ResponseEntity<Object> rejectOgp(@RequestBody GprApprovalDto req) {
        processService.rejectOgp(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "OGP rejected successfully");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/approveGprn")
    public ResponseEntity<Object> approveGi(@RequestBody GprApprovalDto req) {
        processService.approveGprn(req.getProcessNo());
        Map<String, String> res = new HashMap<>();
        res.put("message", "GPRN approved successfully");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    @PostMapping("/changeReqGprn")
    public ResponseEntity<Object> changeReqGprn(@RequestBody GprApprovalDto req) {
        processService.changeReqGprn(req.getProcessNo());
        Map<String, String> res = new HashMap<>();
        res.put("message", "GPRN change request successful.");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/rejectGprn")
    public ResponseEntity<Object> rejectGi(@RequestBody GprApprovalDto req) {
        processService.rejectGprn(req.getProcessNo());
        Map<String, String> res = new HashMap<>();
        res.put("message", "GPRN rejected successfully");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/updateGprn")
    public ResponseEntity<Object> updateGprn(@RequestBody SaveGprnDto updateRequest) {
        processService.updateGprn(updateRequest);
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse("GPRN updated successfully"), HttpStatus.OK);
    }

/*
    @PostMapping("/approveGi")
    public ResponseEntity<Object> approveGiId(@RequestBody GprApprovalDto req) {
        gis.approveGi(req.getProcessNo());
        Map<String, String> res = new HashMap<>();
        res.put("message", "GI approved successfully");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/changeReqGi")
    public ResponseEntity<Object> changeReqGi(@RequestBody GprApprovalDto req) {
        gis.changeReqGi(req.getProcessNo());
        Map<String, String> res = new HashMap<>();
        res.put("message", "GI change request successful.");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/rejectGi")
    public ResponseEntity<Object> rejectGiProcessId(@RequestBody GprApprovalDto req) {
        gis.rejectGi(req.getProcessNo());
        Map<String, String> res = new HashMap<>();
        res.put("message", "GI rejected successfully");
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }*/
    @PostMapping("/approveGi")
    public ResponseEntity<Object> approveGiId(@RequestBody GiApprovalDto req) {
        gis.approveGi(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "GI approved successfully");
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(res));
    }

    @PostMapping("/changeReqGi")
    public ResponseEntity<Object> changeReqGi(@RequestBody GiApprovalDto req) {
        gis.changeReqGi(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "GI change request successful.");
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(res));
    }

    @PostMapping("/rejectGi")
    public ResponseEntity<Object> rejectGiProcessId(@RequestBody GiApprovalDto req) {
        gis.rejectGi(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "GI rejected successfully");
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(res));
    }

    @GetMapping("/getGiByStatuses")
    public ResponseEntity<Object> getGiByStatuses() {
        List<GiMasterEntity> res = gis.getGiByStatuses();
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @GetMapping("/getGiByIndentorStatuses")
    public ResponseEntity<Object> getGiByIndentorStatuses() {
        List<GiMasterEntity> res = gis.getGiByIndentorStatuses();
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/updateGi")
    public ResponseEntity<Object> updateGi(@RequestBody SaveGiDto updateRequest) {
        gis.updateGi(updateRequest);
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse("GI updated successfully"), HttpStatus.OK);
    }

    @GetMapping("/giHistory")
    public ResponseEntity<Object> getGiHistory(
            @RequestParam String processId,
            @RequestParam Integer subProcessId
    ) {
      List<GiWorkflowStatusDto> gi = gis.getGiHistoryByProcessId(processId, subProcessId);
      return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(gi), HttpStatus.OK);
    }


    @PostMapping("/approveGrn")
    public ResponseEntity<Object> approveGrnId(@RequestBody GiApprovalDto req) {
        grns.approveGrn(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "GRN approved successfully");
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(res));
    }

    @PostMapping("/changeReqGrn")
    public ResponseEntity<Object> changeReqGrn(@RequestBody GiApprovalDto req) {
        grns.changeReqGrn(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "GRN change request successful.");
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(res));
    }

    @PostMapping("/rejectGrn")
    public ResponseEntity<Object> rejectGrnProcessId(@RequestBody GiApprovalDto req) {
        grns.rejectGrn(req);
        Map<String, String> res = new HashMap<>();
        res.put("message", "GRN rejected successfully");
        return ResponseEntity.ok(ResponseBuilder.getSuccessResponse(res));
    }

    @GetMapping("/getGrnByStatuses")
    public ResponseEntity<Object> getGrnByStatuses() {
        List<GrnMasterEntity> res = grns.getGrnByStatuses();
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @GetMapping("/getGrnByStorePersonStatuses")
    public ResponseEntity<Object> getGrnByStorepersonStatuses() {
        List<GrnMasterEntity> res = grns.getGrnByStorePresonStatuses();
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/updateGrn")
    public ResponseEntity<Object> updateGrn(@RequestBody GrnDto updateRequest) {
        grns.updateGrn(updateRequest);
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse("Grn updated successfully"), HttpStatus.OK);
    }

    @GetMapping("/grnHistory")
    public ResponseEntity<Object> getGrnHistory(
            @RequestParam String processId,
            @RequestParam Integer subProcessId
    ) {
        List<GiWorkflowStatusDto> gi = grns.getGrnHistoryByProcessId(processId, subProcessId);
        return new ResponseEntity<>(ResponseBuilder.getSuccessResponse(gi), HttpStatus.OK);
    }



}
