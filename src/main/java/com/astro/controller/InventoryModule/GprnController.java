package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnResponseDto;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.service.GprnService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gprns")
public class GprnController {

    @Autowired
    private GprnService gprnService;

    @PostMapping
    public ResponseEntity<Object> createGprn(@RequestBody GprnRequestDto gprnRequestDto) {
        GprnResponseDto savedGprn = gprnService.createGprnWithMaterialDetails(gprnRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(savedGprn), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAllGprn() {
        List<GprnResponseDto> gprns=gprnService.getAllGprn();
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprns), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGprnById(@PathVariable Long id) {
        GprnResponseDto gprn = gprnService.getGprnById(id);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprn), HttpStatus.OK);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGprnById(
            @PathVariable Long id, @RequestBody GprnRequestDto gprnRequestDto) {
        GprnResponseDto gprn =gprnService.updateGprn(id,gprnRequestDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(gprn), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGprn(@PathVariable Long id) {
        gprnService.deleteGprn(id);
        return ResponseEntity.ok("Gprn deleted successfully!");
    }



}
