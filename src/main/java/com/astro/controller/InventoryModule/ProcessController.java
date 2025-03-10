package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GprnDto.SaveGprnDto;
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
    public ResponseEntity<Object> saveGprn(@RequestBody SaveGprnDto saveGprnDto) {
        String processNo = processService.saveGprn(saveGprnDto);
        Map<String, String> res = new HashMap<>();
        res.put("processNo", processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }
    @GetMapping("/getSubProcessDtls")
    public ResponseEntity<Object> getSubProcessDtls(@RequestParam String processStage, @RequestParam String processNo ) {
       Object created = processService.getSubProcessDtls(processStage, processNo);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }
}
