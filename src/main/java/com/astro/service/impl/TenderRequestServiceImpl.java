package com.astro.service.impl;

import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.service.TenderRequestService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenderRequestServiceImpl implements TenderRequestService {

    @Autowired
    private TenderRequestRepository TRrepo;
    @Override
    public TenderRequest createTenderRequest(TenderRequestDto tenderRequestDto) {

        TenderRequest tenderRequest = new TenderRequest();

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

        return tenderRequest;
    }

    @Override
    public TenderRequest updateTenderRequest(Long id, TenderRequestDto tenderRequestDto) {
        TenderRequest existingTR = TRrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tender Request not found with id: " + id));


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

        return existingTR;
    }

    @Override
    public TenderRequest getTenderRequestById(Long id) {
        return TRrepo.findById(id).orElseThrow(() -> new RuntimeException("Teender Request not found!"));
    }

    @Override
    public List<TenderRequest> getAllTenderRequests() {

        return TRrepo.findAll();
    }

    @Override
    public void deleteTenderRequest(Long id) {

        TRrepo.deleteById(id);
    }
}
