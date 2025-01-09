package com.astro.service.impl;

import com.astro.dto.workflow.InventoryModule.GprnRequestDto;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.repository.InventoryModule.GprnRepository;
import com.astro.service.GprnService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GprnServiceImpl implements GprnService {

    @Autowired
    private GprnRepository gprnRepository;


    @Transactional
    public Gprn createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto) {
        // Map GprnRequestDto to Gprn entity
        Gprn gprn = new Gprn();
        gprn.setPoNo(gprnRequestDto.getPoNo());
        String Date = gprnRequestDto.getDate();
        gprn.setDate(CommonUtils.convertStringToDateObject(Date));
        gprn.setDeliveryChallanNo(gprnRequestDto.getDeliveryChallanNo());
        String DeliveryChallanDate = gprnRequestDto.getDeliveryChallanDate();
        gprn.setDeliveryChallanDate(CommonUtils.convertStringToDateObject(DeliveryChallanDate));
        gprn.setVendorId(gprnRequestDto.getVendorId());
        gprn.setVendorName(gprnRequestDto.getVendorName());
        gprn.setFieldStation(gprnRequestDto.getFieldStation());
        gprn.setIndentorName(gprnRequestDto.getIndentorName());
        String ExpectedSupplyDate= gprnRequestDto.getExpectedSupplyDate();
        gprn.setExpectedSupplyDate(CommonUtils.convertStringToDateObject(ExpectedSupplyDate));
        gprn.setConsigneeDetail(gprnRequestDto.getConsigneeDetail());
        gprn.setReceivedBy(gprnRequestDto.getReceivedBy());
        gprn.setWarranty(gprnRequestDto.getWarranty());
        gprn.setProject(gprnRequestDto.getProject());
         gprn.setVendorEmail(gprnRequestDto.getVendorEmail());
         gprn.setVendorContactNo(gprnRequestDto.getVendorContactNo());
        // Save Material Details
        gprn.setMaterialCode(gprnRequestDto.getMaterialCode());
        gprn.setDescription(gprnRequestDto.getDescription());
        gprn.setUom(gprnRequestDto.getUom());
        gprn.setOrderedQuantity(gprnRequestDto.getOrderedQuantity());
        gprn.setQuantityDelivered(gprnRequestDto.getQuantityDelivered());
        gprn.setReceivedQuantity(gprnRequestDto.getReceivedQuantity());
        gprn.setUnitPrice(gprnRequestDto.getUnitPrice());
        gprn.setNetPrice(gprnRequestDto.getNetPrice());
        gprn.setMakeNo(gprnRequestDto.getMakeNo());
        gprn.setModelNo(gprnRequestDto.getModelNo());
        gprn.setSerialNo(gprnRequestDto.getSerialNo());
        gprn.setWarrantyYears(gprnRequestDto.getWarrantyYears());
        gprn.setNote(gprnRequestDto.getNote());
        gprn.setPhotographPath(gprnRequestDto.getPhotographPath());
        gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
        gprn.setCreatedBy(gprnRequestDto.getCreatedBy());

        gprnRepository.save(gprn);



        return gprn;
    }

    @Override
    public Gprn updateGprn(Long id, GprnRequestDto gprnRequestDto) {
        Gprn existing = gprnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods Return not found!"));
        existing.setPoNo(gprnRequestDto.getPoNo());
        String Date = gprnRequestDto.getDate();
        existing.setDate(CommonUtils.convertStringToDateObject(Date));
        existing.setDeliveryChallanNo(gprnRequestDto.getDeliveryChallanNo());
        String DeliveryChallanDate = gprnRequestDto.getDeliveryChallanDate();
        existing.setDeliveryChallanDate(CommonUtils.convertStringToDateObject(DeliveryChallanDate));
        existing.setVendorId(gprnRequestDto.getVendorId());
        existing.setVendorName(gprnRequestDto.getVendorName());
        existing.setFieldStation(gprnRequestDto.getFieldStation());
        existing.setIndentorName(gprnRequestDto.getIndentorName());
        String ExpectedSupplyDate= gprnRequestDto.getExpectedSupplyDate();
        existing.setExpectedSupplyDate(CommonUtils.convertStringToDateObject(ExpectedSupplyDate));
        existing.setConsigneeDetail(gprnRequestDto.getConsigneeDetail());
        existing.setReceivedBy(gprnRequestDto.getReceivedBy());
        existing.setWarranty(gprnRequestDto.getWarranty());
        existing.setWarrantyYears(gprnRequestDto.getWarrantyYears());


        // Save Material Details
        existing.setMaterialCode(gprnRequestDto.getMaterialCode());
        existing.setDescription(gprnRequestDto.getDescription());
        existing.setUom(gprnRequestDto.getUom());
        existing.setOrderedQuantity(gprnRequestDto.getOrderedQuantity());
        existing.setQuantityDelivered(gprnRequestDto.getQuantityDelivered());
        existing.setReceivedQuantity(gprnRequestDto.getReceivedQuantity());
        existing.setUnitPrice(gprnRequestDto.getUnitPrice());
        //  material.setNetPrice(materialDto.getNetPrice());
        existing.setNetPrice(gprnRequestDto.getNetPrice());
        existing.setMakeNo(gprnRequestDto.getMakeNo());
        existing.setModelNo(gprnRequestDto.getModelNo());
        existing.setSerialNo(gprnRequestDto.getSerialNo());

        existing.setNote(gprnRequestDto.getNote());
        existing.setPhotographPath(gprnRequestDto.getPhotographPath());
        existing.setUpdatedBy(gprnRequestDto.getUpdatedBy());
        existing.setCreatedBy(gprnRequestDto.getCreatedBy());
        return gprnRepository.save(existing);
    }

    @Override
    public List<Gprn> getAllGprn() {
        return gprnRepository.findAll();
    }

    @Override
    public Gprn getGprnById(Long id) {
        return gprnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods Return not found!"));
    }

    @Override
    public void deleteGprn(Long id) {
        gprnRepository.deleteById(id);
    }
}
