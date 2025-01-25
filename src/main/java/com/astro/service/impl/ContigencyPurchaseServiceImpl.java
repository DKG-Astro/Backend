package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;
import com.astro.entity.InventoryModule.GoodsInspection;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.entity.ProcurementModule.ContigencyPurchase;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.ContigencyPurchaseRepository;
import com.astro.service.ContigencyPurchaseService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContigencyPurchaseServiceImpl implements ContigencyPurchaseService {
    @Autowired
    private ContigencyPurchaseRepository CPrepo;

    @Override
    public ContigencyPurchaseResponseDto createContigencyPurchase(ContigencyPurchaseRequestDto contigencyPurchaseDto) {

        // Check if the indentorId already exists
        if (CPrepo.existsById(contigencyPurchaseDto.getContigencyId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Contigency Purchase ID", "CP ID " + contigencyPurchaseDto.getContigencyId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

        ContigencyPurchase contigencyPurchase= new ContigencyPurchase();

        contigencyPurchase.setContigencyId(contigencyPurchaseDto.getContigencyId());
        contigencyPurchase.setVendorsName(contigencyPurchaseDto.getVendorsName());
        contigencyPurchase.setVendorsInvoiceNo(contigencyPurchaseDto.getVendorsInvoiceNo());
        String Date = contigencyPurchaseDto.getDate();
        contigencyPurchase.setDate(CommonUtils.convertStringToDateObject(Date));
        contigencyPurchase.setMaterialCode(contigencyPurchaseDto.getMaterialCode());
        contigencyPurchase.setMaterialDescription(contigencyPurchaseDto.getMaterialDescription());
        contigencyPurchase.setQuantity(contigencyPurchaseDto.getQuantity());
        contigencyPurchase.setUnitPrice(contigencyPurchaseDto.getUnitPrice());
        contigencyPurchase.setRemarksForPurchase(contigencyPurchaseDto.getRemarksForPurchase());
        contigencyPurchase.setAmountToBePaid(contigencyPurchaseDto.getAmountToBePaid());
        contigencyPurchase.setUploadCopyOfInvoice(contigencyPurchaseDto.getUploadCopyOfInvoice());
        contigencyPurchase.setPredifinedPurchaseStatement(contigencyPurchaseDto.getPredifinedPurchaseStatement());
        contigencyPurchase.setProjectDetail(contigencyPurchaseDto.getProjectDetail());
        contigencyPurchase.setUpdatedBy(contigencyPurchaseDto.getUpdatedBy());
        contigencyPurchase.setCreatedBy(contigencyPurchaseDto.getCreatedBy());
        CPrepo.save(contigencyPurchase);

        return mapToResponseDTO(contigencyPurchase);
    }



    @Override
    public ContigencyPurchaseResponseDto updateContigencyPurchase(String contigencyId, ContigencyPurchaseRequestDto contigencyPurchaseDto) {
        ContigencyPurchase existingCP = CPrepo.findById(contigencyId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                " ContigencyPurchase not found for the provided contigency purchase ID.")
                ));
        existingCP.setVendorsName(contigencyPurchaseDto.getVendorsName());
        existingCP.setVendorsInvoiceNo(contigencyPurchaseDto.getVendorsInvoiceNo());
        String Date = contigencyPurchaseDto.getDate();
        existingCP.setDate(CommonUtils.convertStringToDateObject(Date));
        existingCP.setMaterialCode(contigencyPurchaseDto.getMaterialCode());
        existingCP.setMaterialDescription(contigencyPurchaseDto.getMaterialDescription());
        existingCP.setQuantity(contigencyPurchaseDto.getQuantity());
        existingCP.setUnitPrice(contigencyPurchaseDto.getUnitPrice());
        existingCP.setRemarksForPurchase(contigencyPurchaseDto.getRemarksForPurchase());
        existingCP.setAmountToBePaid(contigencyPurchaseDto.getAmountToBePaid());
        existingCP.setUploadCopyOfInvoice(contigencyPurchaseDto.getUploadCopyOfInvoice());
        existingCP.setPredifinedPurchaseStatement(contigencyPurchaseDto.getPredifinedPurchaseStatement());
        existingCP.setProjectDetail(contigencyPurchaseDto.getProjectDetail());
        existingCP.setUpdatedBy(contigencyPurchaseDto.getUpdatedBy());
        existingCP.setCreatedBy(contigencyPurchaseDto.getCreatedBy());
   CPrepo.save(existingCP);

        return mapToResponseDTO(existingCP);
    }

    @Override
    public ContigencyPurchaseResponseDto getContigencyPurchaseById(String contigencyId) {
        ContigencyPurchase contigencyPurchase = CPrepo.findById(contigencyId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Contigency Purchase not found for the provided contigency purchase ID.")
                ));
        return mapToResponseDTO(contigencyPurchase);
    }

    @Override
    public List<ContigencyPurchaseResponseDto> getAllContigencyPurchase() {
        List<ContigencyPurchase> contigencyPurchases = CPrepo.findAll();
        return contigencyPurchases.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteContigencyPurchase(String contigencyId) {

      ContigencyPurchase contigencyPurchase=CPrepo.findById(contigencyId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "ContigencyPurchase not found for the provided ID."
                        )
                ));
        try {
            CPrepo.delete(contigencyPurchase);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  Contigency purchase."
                    ),
                    ex
            );
        }
    }

    private ContigencyPurchaseResponseDto mapToResponseDTO(ContigencyPurchase contigencyPurchase) {
        ContigencyPurchaseResponseDto contigencyPurchaseResponseDto = new ContigencyPurchaseResponseDto();
        contigencyPurchaseResponseDto.setContigencyId(contigencyPurchase.getContigencyId());
        contigencyPurchaseResponseDto.setVendorsName(contigencyPurchase.getVendorsName());
        contigencyPurchaseResponseDto.setVendorsInvoiceNo(contigencyPurchase.getVendorsInvoiceNo());
        LocalDate Date = contigencyPurchase.getDate();
        contigencyPurchaseResponseDto.setDate(CommonUtils.convertDateToString(Date));
        contigencyPurchaseResponseDto.setMaterialCode(contigencyPurchase.getMaterialCode());
        contigencyPurchaseResponseDto.setMaterialDescription(contigencyPurchase.getMaterialDescription());
        contigencyPurchaseResponseDto.setQuantity(contigencyPurchase.getQuantity());
        contigencyPurchaseResponseDto.setUnitPrice(contigencyPurchase.getUnitPrice());
        contigencyPurchaseResponseDto.setRemarksForPurchase(contigencyPurchase.getRemarksForPurchase());
        contigencyPurchaseResponseDto.setAmountToBePaid(contigencyPurchase.getAmountToBePaid());
        contigencyPurchaseResponseDto.setUploadCopyOfInvoice(contigencyPurchase.getUploadCopyOfInvoice());
        contigencyPurchaseResponseDto.setPredifinedPurchaseStatement(contigencyPurchase.getPredifinedPurchaseStatement());
        contigencyPurchaseResponseDto.setProjectDetail(contigencyPurchase.getProjectDetail());
        contigencyPurchaseResponseDto.setUpdatedBy(contigencyPurchase.getUpdatedBy());
        contigencyPurchaseResponseDto.setCreatedBy(contigencyPurchase.getCreatedBy());
        contigencyPurchaseResponseDto.setUpdatedDate(contigencyPurchase.getUpdatedDate());
        contigencyPurchaseResponseDto.setCreatedDate(contigencyPurchase.getCreatedDate());
        return contigencyPurchaseResponseDto;

    }
}
