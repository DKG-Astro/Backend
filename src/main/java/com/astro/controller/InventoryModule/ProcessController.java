package com.astro.controller.InventoryModule;


import com.astro.dto.workflow.InventoryModule.GprnDto.getGprnDtlsDto;
import com.astro.dto.workflow.InventoryModule.GprnDto.saveGprnDto;
import com.astro.service.ProcessService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/process-controller")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping
    public ResponseEntity<Object> saveGprn(@RequestBody saveGprnDto saveGprnDto) {
          saveGprnDto created = processService.saveGprn(saveGprnDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getSubProcessDtls(@PathVariable String processStage,String processId ) {
       getGprnDtlsDto created = processService.getSubProcessDtls(processStage,processId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(created), HttpStatus.OK);
    }
}
