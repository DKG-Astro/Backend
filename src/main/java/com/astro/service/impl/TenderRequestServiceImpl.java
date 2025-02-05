package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.SreviceOrderDto.ServiceOrderMaterialRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.TenderRequestDto;
import com.astro.dto.workflow.ProcurementDtos.TenderResponseDto;
import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.entity.ProcurementModule.ServiceOrder;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.service.IndentCreationService;
import com.astro.service.TenderRequestService;
import com.astro.util.CommonUtils;
import com.ctc.wstx.shaded.msv_core.verifier.jarv.TheFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class TenderRequestServiceImpl implements TenderRequestService {

    @Autowired
    private TenderRequestRepository TRrepo;
    @Autowired
    private IndentCreationService indentCreationService;
    @Override
    public TenderResponseDto createTenderRequest(TenderRequestDto tenderRequestDto,String uploadTenderDocumentsFileName,String uploadGeneralTermsAndConditionsFileName
            , String uploadSpecificTermsAndConditionsFileName) {

        // Check if the indentorId already exists
        if (TRrepo.existsById(tenderRequestDto.getTenderId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Tender Request ID", "Tender ID " + tenderRequestDto.getTenderId() + " already exists.");
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
        tenderRequest.setSingleAndMultipleVendors(tenderRequestDto.getSingleAndMultipleVendors());
        tenderRequest.setPreBidDisscussions(tenderRequestDto.getPreBidDisscussions());
        tenderRequest.setUpdatedBy(tenderRequestDto.getUpdatedBy());
        tenderRequest.setCreatedBy(tenderRequestDto.getCreatedBy());
        tenderRequest.setUploadTenderDocumentsFileName(uploadTenderDocumentsFileName);
        tenderRequest.setUploadSpecificTermsAndConditionsFileName(uploadSpecificTermsAndConditionsFileName);
        tenderRequest.setUploadGeneralTermsAndConditionsFileName(uploadGeneralTermsAndConditionsFileName);
        handleFileUpload(tenderRequest, tenderRequestDto.getUploadTenderDocuments(),
                tenderRequest::setUploadTenderDocuments);
        handleFileUpload(tenderRequest, tenderRequestDto.getUploadGeneralTermsAndConditions(),
                tenderRequest::setUploadGeneralTermsAndConditions);
        handleFileUpload(tenderRequest, tenderRequestDto.getUploadSpecificTermsAndConditions(),
                tenderRequest::setUploadSpecificTermsAndConditions);


        TRrepo.save(tenderRequest);

        return mapToResponseDTO(tenderRequest);
    }


    @Override
    public TenderResponseDto updateTenderRequest(String tenderId, TenderRequestDto tenderRequestDto,String uploadTenderDocumentsFileName,String uploadGeneralTermsAndConditionsFileName
            , String uploadSpecificTermsAndConditionsFileName) {
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
        existingTR.setSingleAndMultipleVendors(tenderRequestDto.getSingleAndMultipleVendors());
        existingTR.setPreBidDisscussions(tenderRequestDto.getPreBidDisscussions());
        existingTR.setUpdatedBy(tenderRequestDto.getUpdatedBy());
        existingTR.setCreatedBy(tenderRequestDto.getCreatedBy());
        existingTR.setUploadTenderDocumentsFileName(uploadTenderDocumentsFileName);
        existingTR.setUploadSpecificTermsAndConditionsFileName(uploadSpecificTermsAndConditionsFileName);
        existingTR.setUploadGeneralTermsAndConditionsFileName(uploadGeneralTermsAndConditionsFileName);
        handleFileUpload(existingTR, tenderRequestDto.getUploadTenderDocuments(),
                existingTR::setUploadTenderDocuments);
        handleFileUpload(existingTR, tenderRequestDto.getUploadGeneralTermsAndConditions(),
                existingTR::setUploadGeneralTermsAndConditions);
        handleFileUpload(existingTR, tenderRequestDto.getUploadSpecificTermsAndConditions(),
                existingTR::setUploadSpecificTermsAndConditions);
         TRrepo.save(existingTR);

        return mapToResponseDTO(existingTR);
    }

    @Override
    public  TenderWithIndentResponseDTO getTenderRequestById(String tenderId) {
        TenderRequest tenderRequest =TRrepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Tender not found for the provided asset ID.")
                ));

        // Fetch Indent Data using indentId from TenderRequest
        IndentCreationResponseDTO indentData = indentCreationService.getIndentById(tenderRequest.getIndentId());

        // Combine both Tender and Indent data into a single response DTO
        TenderWithIndentResponseDTO responseDTO = new TenderWithIndentResponseDTO();
        // Set Tender Details
        responseDTO.setTenderId(tenderRequest.getTenderId());
        responseDTO.setTitleOfTender(tenderRequest.getTitleOfTender());
        LocalDate openingDate = tenderRequest.getOpeningDate();
        responseDTO.setOpeningDate(CommonUtils.convertDateToString(openingDate));
        LocalDate closeingDate = tenderRequest.getClosingDate();
        responseDTO.setClosingDate(CommonUtils.convertDateToString(closeingDate));
        responseDTO.setIndentId(tenderRequest.getIndentId());
        responseDTO.setIndentMaterials(tenderRequest.getIndentMaterials());
        responseDTO.setModeOfProcurement(tenderRequest.getModeOfProcurement());
        responseDTO.setBidType(tenderRequest.getBidType());
        LocalDate LastDateOfSubmission = tenderRequest.getLastDateOfSubmission();
        responseDTO.setLastDateOfSubmission(CommonUtils.convertDateToString(LastDateOfSubmission));
        responseDTO.setApplicableTaxes(tenderRequest.getApplicableTaxes());
        responseDTO.setConsignesAndBillinngAddress(tenderRequest.getConsignesAndBillinngAddress());
        responseDTO.setIncoTerms(tenderRequest.getIncoTerms());
        responseDTO.setPaymentTerms(tenderRequest.getPaymentTerms());
        responseDTO.setLdClause(tenderRequest.getLdClause());
        responseDTO.setApplicablePerformance(tenderRequest.getApplicablePerformance());
        responseDTO.setBidSecurityDeclaration(tenderRequest.getBidSecurityDeclaration());
        responseDTO.setMllStatusDeclaration(tenderRequest.getMllStatusDeclaration());
        responseDTO.setUploadTenderDocuments(tenderRequest.getUploadTenderDocumentsFileName());
        responseDTO.setSingleAndMultipleVendors(tenderRequest.getSingleAndMultipleVendors());
        responseDTO.setUploadGeneralTermsAndConditions(tenderRequest.getUploadGeneralTermsAndConditionsFileName());
        responseDTO.setUploadSpecificTermsAndConditions(tenderRequest.getUploadSpecificTermsAndConditionsFileName());
        responseDTO.setPreBidDisscussions(tenderRequest.getPreBidDisscussions());
        responseDTO.setUpdatedBy(tenderRequest.getUpdatedBy());
        responseDTO.setCreatedBy(tenderRequest.getCreatedBy());
        responseDTO.setCreatedDate(tenderRequest.getCreatedDate());
        responseDTO.setUpdatedDate(tenderRequest.getUpdatedDate());
        responseDTO.setIndentResponseDTO(indentData);

        return responseDTO;

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
        tenderResponseDto.setUploadTenderDocuments(tenderRequest.getUploadTenderDocumentsFileName());
        tenderResponseDto.setSingleAndMultipleVendors(tenderRequest.getSingleAndMultipleVendors());
        tenderResponseDto.setUploadGeneralTermsAndConditions(tenderRequest.getUploadGeneralTermsAndConditionsFileName());
        tenderResponseDto.setUploadSpecificTermsAndConditions(tenderRequest.getUploadSpecificTermsAndConditionsFileName());
        tenderResponseDto.setPreBidDisscussions(tenderRequest.getPreBidDisscussions());
        tenderResponseDto.setUpdatedBy(tenderRequest.getUpdatedBy());
        tenderResponseDto.setCreatedBy(tenderRequest.getCreatedBy());
        tenderResponseDto.setCreatedDate(tenderRequest.getCreatedDate());
        tenderResponseDto.setUpdatedDate(tenderRequest.getUpdatedDate());
        return tenderResponseDto;



    }

    public void handleFileUpload(TenderRequest tenderRequest, MultipartFile file, Consumer<byte[]> fileSetter) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileSetter.accept(fileBytes);
            } catch (IOException e) {
                throw new InvalidInputException(new ErrorDetails(500, 3, "File Processing Error",
                        "Error while processing the uploaded file. Please try again."));
            }
        } else {
            fileSetter.accept(null);  // Handle gracefully if no file is uploaded
        }
    }

}
