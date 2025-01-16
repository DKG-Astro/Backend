package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GprnRequestDto;
import com.astro.dto.workflow.InventoryModule.GprnResponseDto;
import com.astro.entity.InventoryModule.GoodsReturn;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.GprnRepository;
import com.astro.service.GprnService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GprnServiceImpl implements GprnService {

    @Autowired
    private GprnRepository gprnRepository;


    @Transactional
    public GprnResponseDto createGprnWithMaterialDetails(GprnRequestDto gprnRequestDto) {
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
        String ExpectedSupplyDate = gprnRequestDto.getExpectedSupplyDate();
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
        BigDecimal receivedQuantity = gprnRequestDto.getReceivedQuantity() != null
                ? BigDecimal.valueOf(gprnRequestDto.getReceivedQuantity())
                : BigDecimal.ZERO;
        BigDecimal unitPrice = gprnRequestDto.getUnitPrice() != null
                ? BigDecimal.valueOf(gprnRequestDto.getUnitPrice())
                : BigDecimal.ZERO;
        gprn.setNetPrice(receivedQuantity.multiply(unitPrice));
       // gprn.setNetPrice(gprnRequestDto.getNetPrice());
        gprn.setMakeNo(gprnRequestDto.getMakeNo());
        gprn.setModelNo(gprnRequestDto.getModelNo());
        gprn.setSerialNo(gprnRequestDto.getSerialNo());
        gprn.setWarrantyYears(gprnRequestDto.getWarrantyYears());
        gprn.setNote(gprnRequestDto.getNote());
        gprn.setPhotographPath(gprnRequestDto.getPhotographPath());
        gprn.setUpdatedBy(gprnRequestDto.getUpdatedBy());
        gprn.setCreatedBy(gprnRequestDto.getCreatedBy());

        gprnRepository.save(gprn);


        return mapToResponseDTO(gprn);
    }


    @Override
    public GprnResponseDto updateGprn(Long id, GprnRequestDto gprnRequestDto) {
        Gprn existing = gprnRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Gprn not found for the provided asset ID.")
                ));
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
        String ExpectedSupplyDate = gprnRequestDto.getExpectedSupplyDate();
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
        BigDecimal receivedQuantity = gprnRequestDto.getReceivedQuantity() != null
                ? BigDecimal.valueOf(gprnRequestDto.getReceivedQuantity())
                : BigDecimal.ZERO;
        BigDecimal unitPrice = gprnRequestDto.getUnitPrice() != null
                ? BigDecimal.valueOf(gprnRequestDto.getUnitPrice())
                : BigDecimal.ZERO;
        existing.setNetPrice(receivedQuantity.multiply(unitPrice));
       // existing.setNetPrice(gprnRequestDto.getNetPrice());
        existing.setMakeNo(gprnRequestDto.getMakeNo());
        existing.setModelNo(gprnRequestDto.getModelNo());
        existing.setSerialNo(gprnRequestDto.getSerialNo());

        existing.setNote(gprnRequestDto.getNote());
        existing.setPhotographPath(gprnRequestDto.getPhotographPath());
        existing.setUpdatedBy(gprnRequestDto.getUpdatedBy());
        existing.setCreatedBy(gprnRequestDto.getCreatedBy());
        gprnRepository.save(existing);
        return mapToResponseDTO(existing);
    }

    @Override
    public List<GprnResponseDto> getAllGprn() {


        List<Gprn> gprns = gprnRepository.findAll();
        return gprns.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public GprnResponseDto getGprnById(Long id) {
        Gprn gprn= gprnRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Gprn not found for the provided asset ID.")
                ));
        return mapToResponseDTO(gprn);
    }

    @Override
    public void deleteGprn(Long id) {

       Gprn  gprn=gprnRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Gprn not found for the provided ID."
                        )
                ));
        try {
            gprnRepository.delete(gprn);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  gprn."
                    ),
                    ex
            );
        }

}

    private GprnResponseDto mapToResponseDTO(Gprn gprn) {
        GprnResponseDto  gprnResponseDto = new GprnResponseDto();
        gprnResponseDto.setGprnNo(gprn.getGprnNo());
        gprnResponseDto.setPoNo(gprn.getPoNo());
        LocalDate Date = gprn.getDate();
        gprnResponseDto.setDate(CommonUtils.convertDateToString(Date));
        gprnResponseDto.setDeliveryChallanNo(gprn.getDeliveryChallanNo());
        LocalDate DeliveryChallanDate = gprn.getDeliveryChallanDate();
        gprnResponseDto.setDeliveryChallanDate(CommonUtils.convertDateToString(DeliveryChallanDate));
        gprnResponseDto.setVendorId(gprn.getVendorId());
        gprnResponseDto.setVendorName(gprn.getVendorName());
        gprnResponseDto.setFieldStation(gprn.getFieldStation());
        gprnResponseDto.setIndentorName(gprn.getIndentorName());
        LocalDate ExpectedSupplyDate= gprn.getExpectedSupplyDate();
        gprnResponseDto.setExpectedSupplyDate(CommonUtils.convertDateToString(ExpectedSupplyDate));
        gprnResponseDto.setConsigneeDetail(gprn.getConsigneeDetail());
        gprnResponseDto.setReceivedBy(gprn.getReceivedBy());
        gprnResponseDto.setWarranty(gprn.getWarranty());
        gprnResponseDto.setProject(gprn.getProject());
        gprnResponseDto.setVendorEmail(gprn.getVendorEmail());
        gprnResponseDto.setVendorContactNo(gprn.getVendorContactNo());
        // Save Material Details
        gprnResponseDto.setMaterialCode(gprn.getMaterialCode());
        gprnResponseDto.setDescription(gprn.getDescription());
        gprnResponseDto.setUom(gprn.getUom());
        gprnResponseDto.setOrderedQuantity(gprn.getOrderedQuantity());
        gprnResponseDto.setQuantityDelivered(gprn.getQuantityDelivered());
        gprnResponseDto.setReceivedQuantity(gprn.getReceivedQuantity());
        gprnResponseDto.setUnitPrice(gprn.getUnitPrice());
        gprnResponseDto.setNetPrice(gprn.getNetPrice());
        gprnResponseDto.setMakeNo(gprn.getMakeNo());
        gprnResponseDto.setModelNo(gprn.getModelNo());
        gprnResponseDto.setSerialNo(gprn.getSerialNo());
        gprnResponseDto.setWarrantyYears(gprn.getWarrantyYears());
        gprnResponseDto.setNote(gprn.getNote());
        gprnResponseDto.setPhotographPath(gprn.getPhotographPath());
        gprnResponseDto.setUpdatedBy(gprn.getUpdatedBy());
        gprnResponseDto.setCreatedBy(gprn.getCreatedBy());
        gprnResponseDto.setCreatedDate(gprn.getCreatedDate());
        gprnResponseDto.setUpdatedDate(gprn.getUpdatedDate());
        return gprnResponseDto;
    }
}
