package com.astro.service;

import com.astro.dto.workflow.InventoryModule.GoodsReceiptInspectionDto;
import com.astro.dto.workflow.InventoryModule.GprnRequestDto;
import com.astro.entity.InventoryModule.GoodsReceiptInspection;
import com.astro.entity.InventoryModule.Gprn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GprnService {


    public Gprn createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto);


    Gprn updateGprn(Long id, GprnRequestDto gprnRequestDto);
    List<Gprn> getAllGprn();
    Gprn getGprnById(Long id);
    void deleteGprn(Long id);


}
