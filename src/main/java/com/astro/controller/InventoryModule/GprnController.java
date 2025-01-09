package com.astro.controller.InventoryModule;

import com.astro.dto.workflow.InventoryModule.GoodsReturnDto;
import com.astro.dto.workflow.InventoryModule.GprnRequestDto;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.service.GprnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gprns")
public class GprnController {

    @Autowired
    private GprnService gprnService;

    @PostMapping
    public ResponseEntity<Gprn> createGprn(@RequestBody GprnRequestDto gprnRequestDto) {
        Gprn savedGprn = gprnService.createGprnWithMaterialDetails(gprnRequestDto);
        return ResponseEntity.ok(savedGprn);
    }
    @GetMapping
    public ResponseEntity<List<Gprn>> getAllGprn() {
        List<Gprn> gprns=gprnService.getAllGprn();
        return ResponseEntity.ok(gprns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gprn> getGprnById(@PathVariable Long id) {
        Gprn gprn = gprnService.getGprnById(id);
        return ResponseEntity.ok(gprn);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Gprn> updateGprnById(
            @PathVariable Long id, @RequestBody GprnRequestDto gprnRequestDto) {
        Gprn gprn =gprnService.updateGprn(id,gprnRequestDto);
        return ResponseEntity.ok(gprn);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGprn(@PathVariable Long id) {
        gprnService.deleteGprn(id);
        return ResponseEntity.ok("Gprn deleted successfully!");
    }



}
