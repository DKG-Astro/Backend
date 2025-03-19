package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GiDto.SaveGiDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
import com.astro.dto.workflow.InventoryModule.grn.GrnDto;
import com.astro.dto.workflow.InventoryModule.grv.GrvDto;
import com.astro.service.ProcessService;
import com.astro.util.ResponseBuilder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/process-controller")
public class ProcessController {

    @Autowired
    private ProcessService processService;

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
    @PostMapping("/saveGrn")
    public ResponseEntity<Object> saveGrn(@RequestBody GrnDto req) {
        String processNo = processService.saveGrn(req);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
}
