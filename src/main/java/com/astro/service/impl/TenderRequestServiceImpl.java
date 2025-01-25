package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderMaterialRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;
import com.astro.entity.ProcurementModule.ServiceOrder;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.service.TenderRequestService;
import com.astro.util.CommonUtils;
import com.ctc.wstx.shaded.msv_core.verifier.jarv.TheFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenderRequestServiceImpl implements TenderRequestService {

    @Autowired
    private TenderRequestRepository TRrepo;
    @Override
    public TenderResponseDto createTenderRequest(TenderRequestDto tenderRequestDto) {

        // Check if the indentorId already exists
        if (TRrepo.existsById(tenderRequestDto.getTenderId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Tender Request ID", "SO ID " + tenderRequestDto.getTenderId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }


        TenderRequest tenderRequest = new TenderRequest();

        tenderRequest.setTenderId(tenderRequestDto.getTenderId());
        tenderRequest.setTitleOfTender(tenderRequestDto.getTitleOfTender());
        String openingDate = tenderRequestDto.getOpeningDate();
        tenderRequest.setOpeningDate(CommonUtils.convertStringToDateObject(openingDate));
        String closeingDate = tenderRequestDto.getClosingDate();
        tenderRequest.setClosingDate(CommonUtils.convertStringToDateObject(closeingDate));
        tenderRequest.setIndentId(tenderRequestDto.getIndentId());
        tenderRequest.setIndentMaterials(tenderRequestDto.getIndentMaterials());
        tenderRequest.setModeOfProcurement(tenderRequestDto.getModeOfProcurement());
        tenderRequest.setBidType(tenderRequestDto.getBidType());
        String LastDateOfSubmission = tenderRequestDto.getLastDateOfSubmission();
        tenderRequest.setLastDateOfSubmission(CommonUtils.convertStringToDateObject(LastDateOfSubmission));
        tenderRequest.setApplicableTaxes(tenderRequestDto.getApplicableTaxes());
        tenderRequest.setConsignesAndBillinngAddress(tenderRequestDto.getConsignesAndBillinngAddress());
        tenderRequest.setIncoTerms(tenderRequestDto.getIncoTerms());
        tenderRequest.setPaymentTerms(tenderRequestDto.getPaymentTerms());
        tenderRequest.setLdClause(tenderRequestDto.getLdClause());
        tenderRequest.setApplicablePerformance(tenderRequestDto.getApplicablePerformance());
        tenderRequest.setBidSecurityDeclaration(tenderRequestDto.getBidSecurityDeclaration());
        tenderRequest.setMllStatusDeclaration(tenderRequestDto.getMllStatusDeclaration());
        tenderRequest.setUploadTenderDocuments(tenderRequestDto.getUploadTenderDocuments());
        tenderRequest.setSingleAndMultipleVendors(tenderRequestDto.getSingleAndMultipleVendors());
        tenderRequest.setUploadGeneralTermsAndConditions(tenderRequestDto.getUploadGeneralTermsAndConditions());
        tenderRequest.setUploadSpecificTermsAndConditions(tenderRequestDto.getUploadSpecificTermsAndConditions());
        tenderRequest.setPreBidDisscussions(tenderRequestDto.getPreBidDisscussions());
        tenderRequest.setUpdatedBy(tenderRequestDto.getUpdatedBy());
        tenderRequest.setCreatedBy(tenderRequestDto.getCreatedBy());

        TRrepo.save(tenderRequest);

        return mapToResponseDTO(tenderRequest);
    }


    @Override
    public TenderResponseDto updateTenderRequest(String tenderId, TenderRequestDto tenderRequestDto) {
        TenderRequest existingTR = TRrepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Tender request not found for the provided asset ID.")
                ));
        existingTR.setTitleOfTender(tenderRequestDto.getTitleOfTender());
        String openingDate = tenderRequestDto.getOpeningDate();
        existingTR.setOpeningDate(CommonUtils.convertStringToDateObject(openingDate));
        String closeingDate = tenderRequestDto.getClosingDate();
        existingTR.setClosingDate(CommonUtils.convertStringToDateObject(closeingDate));
        existingTR.setIndentId(tenderRequestDto.getIndentId());
        existingTR.setIndentMaterials(tenderRequestDto.getIndentMaterials());
        existingTR.setModeOfProcurement(tenderRequestDto.getModeOfProcurement());
        existingTR.setBidType(tenderRequestDto.getBidType());
        String LastDateOfSubmission = tenderRequestDto.getLastDateOfSubmission();
        existingTR.setLastDateOfSubmission(CommonUtils.convertStringToDateObject(LastDateOfSubmission));
        existingTR.setApplicableTaxes(tenderRequestDto.getApplicableTaxes());
        existingTR.setConsignesAndBillinngAddress(tenderRequestDto.getConsignesAndBillinngAddress());
        existingTR.setIncoTerms(tenderRequestDto.getIncoTerms());
        existingTR.setPaymentTerms(tenderRequestDto.getPaymentTerms());
        existingTR.setLdClause(tenderRequestDto.getLdClause());
        existingTR.setApplicablePerformance(tenderRequestDto.getApplicablePerformance());
        existingTR.setBidSecurityDeclaration(tenderRequestDto.getBidSecurityDeclaration());
        existingTR.setMllStatusDeclaration(tenderRequestDto.getMllStatusDeclaration());
        existingTR.setUploadTenderDocuments(tenderRequestDto.getUploadTenderDocuments());
        existingTR.setSingleAndMultipleVendors(tenderRequestDto.getSingleAndMultipleVendors());
        existingTR.setUploadGeneralTermsAndConditions(tenderRequestDto.getUploadGeneralTermsAndConditions());
        existingTR.setUploadSpecificTermsAndConditions(tenderRequestDto.getUploadSpecificTermsAndConditions());
        existingTR.setPreBidDisscussions(tenderRequestDto.getPreBidDisscussions());
        existingTR.setUpdatedBy(tenderRequestDto.getUpdatedBy());
        existingTR.setCreatedBy(tenderRequestDto.getCreatedBy());
         TRrepo.save(existingTR);

        return mapToResponseDTO(existingTR);
    }

    @Override
    public TenderResponseDto getTenderRequestById(String tenderId) {
        TenderRequest tenderRequest =TRrepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Tender not found for the provided asset ID.")
                ));
        return mapToResponseDTO(tenderRequest);
    }

    @Override
    public List<TenderResponseDto> getAllTenderRequests() {

        List<TenderRequest> tenderRequests = TRrepo.findAll();
        return tenderRequests.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTenderRequest(String tenderId) {

        TenderRequest tenderRequest=TRrepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "tender not found for the provided ID."
                        )
                ));
        try {
           TRrepo.delete(tenderRequest);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the tender."
                    ),
                    ex
            );
        }
    }

    private TenderResponseDto mapToResponseDTO(TenderRequest tenderRequest) {

        TenderResponseDto tenderResponseDto = new TenderResponseDto();

        tenderResponseDto.setTenderId(tenderRequest.getTenderId());
        tenderResponseDto.setTitleOfTender(tenderRequest.getTitleOfTender());
        LocalDate openingDate = tenderRequest.getOpeningDate();
        tenderResponseDto.setOpeningDate(CommonUtils.convertDateToString(openingDate));
        LocalDate closeingDate = tenderRequest.getClosingDate();
        tenderResponseDto.setClosingDate(CommonUtils.convertDateToString(closeingDate));
        tenderResponseDto.setIndentId(tenderRequest.getIndentId());
        tenderResponseDto.setIndentMaterials(tenderRequest.getIndentMaterials());
        tenderResponseDto.setModeOfProcurement(tenderRequest.getModeOfProcurement());
        tenderResponseDto.setBidType(tenderRequest.getBidType());
        LocalDate LastDateOfSubmission = tenderRequest.getLastDateOfSubmission();
        tenderResponseDto.setLastDateOfSubmission(CommonUtils.convertDateToString(LastDateOfSubmission));
        tenderResponseDto.setApplicableTaxes(tenderRequest.getApplicableTaxes());
        tenderResponseDto.setConsignesAndBillinngAddress(tenderRequest.getConsignesAndBillinngAddress());
        tenderResponseDto.setIncoTerms(tenderRequest.getIncoTerms());
        tenderResponseDto.setPaymentTerms(tenderRequest.getPaymentTerms());
        tenderResponseDto.setLdClause(tenderRequest.getLdClause());
        tenderResponseDto.setApplicablePerformance(tenderRequest.getApplicablePerformance());
        tenderResponseDto.setBidSecurityDeclaration(tenderRequest.getBidSecurityDeclaration());
        tenderResponseDto.setMllStatusDeclaration(tenderRequest.getMllStatusDeclaration());
        tenderResponseDto.setUploadTenderDocuments(tenderRequest.getUploadTenderDocuments());
        tenderResponseDto.setSingleAndMultipleVendors(tenderRequest.getSingleAndMultipleVendors());
        tenderResponseDto.setUploadGeneralTermsAndConditions(tenderRequest.getUploadGeneralTermsAndConditions());
        tenderResponseDto.setUploadSpecificTermsAndConditions(tenderRequest.getUploadSpecificTermsAndConditions());
        tenderResponseDto.setPreBidDisscussions(tenderRequest.getPreBidDisscussions());
        tenderResponseDto.setUpdatedBy(tenderRequest.getUpdatedBy());
        tenderResponseDto.setCreatedBy(tenderRequest.getCreatedBy());
        tenderResponseDto.setCreatedDate(tenderRequest.getCreatedDate());
        tenderResponseDto.setUpdatedDate(tenderRequest.getUpdatedDate());
        return tenderResponseDto;



    }

}
