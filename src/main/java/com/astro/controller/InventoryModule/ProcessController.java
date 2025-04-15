package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.dto.workflow.InventoryModule.gprn.GprnPendingInspectionDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpDetailReportDto;
import com.astro.dto.workflow.InventoryModule.igp.IgpDto;
import com.astro.dto.workflow.InventoryModule.isn.IsnDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoDto;
import com.astro.dto.workflow.InventoryModule.ogp.OgpPoResponseDto;
import com.astro.entity.InventoryModule.IsnAssetOhqDtlsDto;
import com.astro.service.ProcessService;
import com.astro.service.InventoryModule.GiService;
import com.astro.service.InventoryModule.IgpService;
import com.astro.util.ResponseBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @GetMapping("/getPendingGi")
    public ResponseEntity<Object> getPendingGi() {
        List<GprnPendingInspectionDto> res = gis.getPendingGi();
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
        List<IgpDetailReportDto> res = igpService.getIgpDetails();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    
}
