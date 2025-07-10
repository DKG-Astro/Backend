package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.QuotationViewHistoryDto;
import com.astro.dto.workflow.ProcurementDtos.VendorQuotationChangeRequestDto;
import com.astro.dto.workflow.VendorQuotationAgainstTenderDto;
import com.astro.dto.workflow.VendorQuotationUpdateRequestDto;
import com.astro.dto.workflow.VendorStatusDto;
import com.astro.entity.ProcurementModule.TenderRequest;
import com.astro.entity.VendorLoginDetails;
import com.astro.entity.VendorMaster;
import com.astro.entity.VendorMasterUtil;
import com.astro.entity.VendorQuotationAgainstTender;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.*;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.repository.ProcurementModule.IndentIdRepository;
import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.service.VendorQuotationAgainstTenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorQuotationAgainstTenderServiceImpl implements VendorQuotationAgainstTenderService {

    @Autowired
    private VendorQuotationAgainstTenderRepository vendorQuotationAgainstTenderRepository;
    @Autowired
    private VendorMasterRepository vendorMasterRepository;
    @Autowired
    private VendorMasterUtilRepository vendorMasterUtilRepository;
    @Autowired
    private VendorLoginDetailsRepository vendorLoginDetailsRepository;
    @Autowired
    private IndentIdRepository indentRepo;
    @Autowired
    private VendorNamesForJobWorkMaterialRepository vRepo;
    @Autowired
    private TenderRequestRepository tenderRepo;
   /* @Override
    public VendorQuotationAgainstTenderDto saveQuotation(VendorQuotationAgainstTenderDto dto) {
        VendorQuotationAgainstTender quotation = new VendorQuotationAgainstTender();
        quotation.setTenderId(dto.getTenderId());
        quotation.setVendorId(dto.getVendorId());
       // quotation.setVendorName(dto.getVendorName());
        quotation.setQuotationFileName(dto.getQuotationFileName());
        quotation.setFileType(dto.getFileType());
        quotation.setCreatedBy(dto.getCreatedBy());
      VendorQuotationAgainstTender qu =  vendorQuotationAgainstTenderRepository.save(quotation);
        return mapToResponse(qu);
    }*/
   @Override
   public VendorQuotationAgainstTenderDto saveQuotation(VendorQuotationAgainstTenderDto dto) {
       // Step 1: Fetch all quotations for this tenderId and vendorId
       List<VendorQuotationAgainstTender> existingQuotations =
               vendorQuotationAgainstTenderRepository.findAllByTenderIdAndVendorId(dto.getTenderId(), dto.getVendorId());

       // Step 2: Mark all existing quotations as not latest, and get max version
       int maxVersion = 0;
       for (VendorQuotationAgainstTender q : existingQuotations) {
           q.setIsLatest(false);
           vendorQuotationAgainstTenderRepository.save(q);
           if (q.getVersion() != null && q.getVersion() > maxVersion) {
               maxVersion = q.getVersion();
           }
       }

       // Step 3: Save the new quotation with version = maxVersion + 1
       VendorQuotationAgainstTender quotation = new VendorQuotationAgainstTender();
       quotation.setTenderId(dto.getTenderId());
       quotation.setVendorId(dto.getVendorId());
       quotation.setQuotationFileName(dto.getQuotationFileName());
       quotation.setFileType(dto.getFileType());
       quotation.setCreatedBy(dto.getCreatedBy());
       quotation.setVersion(maxVersion + 1);
       quotation.setIsLatest(true);
       quotation.setStatus("SUBMITTED");
       quotation.setCreatedDate(LocalDateTime.now());
       quotation.setUpdatedDate(LocalDateTime.now());

       VendorQuotationAgainstTender saved = vendorQuotationAgainstTenderRepository.save(quotation);
       return mapToResponse(saved);
   }


   /* @Override
    public List<VendorQuotationAgainstTenderDto> getQuotationsByTenderId(String tenderId) {

       List<VendorQuotationAgainstTender> vqList= vendorQuotationAgainstTenderRepository.findByTenderId(tenderId);

        return vqList.stream()
                .filter(vq -> !"Rejected".equalsIgnoreCase(vq.getStatus()))
                .map(vq -> {
            VendorQuotationAgainstTenderDto dto = new VendorQuotationAgainstTenderDto();
            dto.setTenderId(vq.getTenderId());
            dto.setVendorId(vq.getVendorId());
            dto.setQuotationFileName(vq.getQuotationFileName());
            dto.setFileType(vq.getFileType());
            dto.setCreatedBy(vq.getCreatedBy());
            return dto;
        }).collect(Collectors.toList());
    }*/
   @Override
   public List<VendorQuotationAgainstTenderDto> getQuotationsByTenderId(String tenderId) {
       List<VendorQuotationAgainstTender> vqList = vendorQuotationAgainstTenderRepository.findLatestNonRejectedQuotations(tenderId);

       return vqList.stream().map(vq -> {
           VendorQuotationAgainstTenderDto dto = new VendorQuotationAgainstTenderDto();
           dto.setTenderId(vq.getTenderId());
           dto.setVendorId(vq.getVendorId());
           dto.setQuotationFileName(vq.getQuotationFileName());
           dto.setFileType(vq.getFileType());
           dto.setCreatedBy(vq.getCreatedBy());
           dto.setVersion(vq.getVersion()); //new one in dto if want remove
           return dto;
       }).collect(Collectors.toList());
   }

  /* @Override
   public List<VendorQuotationAgainstTenderDto> getQuotationsByTenderId(String tenderId) {
       List<VendorQuotationAgainstTender> vqList = vendorQuotationAgainstTenderRepository.findByTenderId(tenderId);
       Optional<TenderRequest> tenderOpt = tenderRepo.findByTenderId(tenderId);


       TenderRequest tender = tenderOpt.get();
       LocalDate openingDate = tender.getOpeningDate();
       LocalDate closingDate = tender.getClosingDate();
       System.out.println("opendate"+openingDate +"closeing date"+ closingDate);

       return vqList.stream()
               .filter(vq -> !"Rejected".equalsIgnoreCase(vq.getStatus()))
               .filter(vq -> {
                   if (vq.getCreatedDate() == null) return false;
                   LocalDate createdDate = vq.getCreatedDate().toLocalDate();
                   return !createdDate.isBefore(openingDate) && !createdDate.isAfter(closingDate);
               })
               .map(vq -> {
                   VendorQuotationAgainstTenderDto dto = new VendorQuotationAgainstTenderDto();
                   dto.setTenderId(vq.getTenderId());
                   dto.setVendorId(vq.getVendorId());
                   dto.setQuotationFileName(vq.getQuotationFileName());
                   dto.setFileType(vq.getFileType());
                   dto.setCreatedBy(vq.getCreatedBy());
                   return dto;
               })
               .collect(Collectors.toList());
   }*/


    @Override
    public List<String> getVendorsWhoDidNotSubmitQuotation(String tenderId) {

        List<String> indentIds = indentRepo.findTenderWithIndent(tenderId);
        List<String> allVendorIds = new ArrayList<>();
        for (String indentId : indentIds) {
            List<String> vendorIds = vRepo.findVendorNamesByIndentId(indentId);
            allVendorIds.addAll(vendorIds);
        }
        List<String> submittedVendorIds = vendorQuotationAgainstTenderRepository.findVendorIdsByTenderId(tenderId);
        allVendorIds.removeAll(submittedVendorIds);
        return allVendorIds;
    }

    @Override
    public VendorStatusDto getVendorStatus(String vendorId) {

        VendorStatusDto dto = new VendorStatusDto();
        dto.setVendorId(vendorId);

        // approved vendors
        Optional<VendorMaster> approved = vendorMasterRepository.findByVendorId(vendorId);
        Optional<VendorLoginDetails> vendorLogin =vendorLoginDetailsRepository.findByVendorId(vendorId);
        VendorLoginDetails vl = vendorLogin.get();
        if (approved.isPresent()) {
            VendorMaster vm = approved.get();
            dto.setStatus(vm.getStatus());
            dto.setComments("Vendor is approved.");
            dto.setPassword(vl.getPassword());
            dto.setEmailStatus(vl.getEmailSent());
            return dto;
        }

        // rejected/awaiting vendors
        Optional<VendorMasterUtil> pendingOrRej = vendorMasterUtilRepository.findByVendorId(vendorId);
        if (pendingOrRej.isPresent()) {
            VendorMasterUtil vendor = pendingOrRej.get();
            dto.setStatus(vendor.getApprovalStatus().name()); // rejected or awaiting for approval
            dto.setComments(vendor.getComments());
            dto.setPassword(vl.getPassword());
            dto.setEmailStatus(vl.getEmailSent());
            return dto;
        }

        // Not found anywhere
        dto.setStatus("NOT_FOUND");
        dto.setComments("Vendor ID is not available in the system.");
        return dto;
    }

    private VendorQuotationAgainstTenderDto mapToResponse(VendorQuotationAgainstTender dto) {
        VendorQuotationAgainstTenderDto quotation = new VendorQuotationAgainstTenderDto();
        quotation.setTenderId(dto.getTenderId());
        quotation.setVendorId(dto.getVendorId());
       // quotation.setVendorName(dto.getVendorName());
        quotation.setQuotationFileName(dto.getQuotationFileName());
        quotation.setFileType(dto.getFileType());
        quotation.setCreatedBy(dto.getCreatedBy());
        return quotation;
    }

   /* public boolean updateStatusAndRemarks(VendorQuotationUpdateRequestDto request) {
        OPtional<VendorQuotationAgainstTender> optional = vendorQuotationAgainstTenderRepository.findByTenderIdAndVendorId(
                request.getTenderId(), request.getVendorId());

        if (optional.isPresent()) {
            VendorQuotationAgainstTender record = optional.get();
            record.setStatus(request.getStatus());
            record.setRemarks(request.getRemarks());
            record.setUpdatedDate(LocalDateTime.now());
            vendorQuotationAgainstTenderRepository.save(record);
            return true;
        } else {
            return false;
        }
    }*/
   public boolean updateStatusAndRemarks(VendorQuotationUpdateRequestDto request) {
       List<VendorQuotationAgainstTender> quotations = vendorQuotationAgainstTenderRepository.findByTenderIdAndVendorId(
               request.getTenderId(), request.getVendorId());

       if (!quotations.isEmpty()) {
           // You can choose to update all or only the latest one
           for (VendorQuotationAgainstTender record : quotations) {
               record.setStatus(request.getStatus());
               record.setRemarks(request.getRemarks());
               record.setUpdatedDate(LocalDateTime.now());
               vendorQuotationAgainstTenderRepository.save(record);
           }
           return true;
       } else {
           return false;
       }
   }


    public boolean markQuotationForChangeRequest(VendorQuotationChangeRequestDto request) {
        // Find latest quotation for this vendor and tender
        Optional<VendorQuotationAgainstTender> optional = vendorQuotationAgainstTenderRepository
                .findTopByTenderIdAndVendorIdAndIsLatestTrueOrderByVersionDesc(
                        request.getTenderId(), request.getVendorId());

        if (optional.isPresent()) {
            VendorQuotationAgainstTender quotation = optional.get();
            quotation.setStatus("CHANGE_REQUESTED");
            quotation.setRemarks(request.getRemarks());
            quotation.setUpdatedDate(LocalDateTime.now());

            vendorQuotationAgainstTenderRepository.save(quotation);
            return true;
        }

        return false;
    }
    @Override
    public List<QuotationViewHistoryDto> getVendorHistory(String tenderId, String vendorId) {
        List<VendorQuotationAgainstTender> list =
                vendorQuotationAgainstTenderRepository.findAllByTenderIdAndVendorIdOrderByCreatedDateDesc(tenderId, vendorId);


        if (list.isEmpty()) {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "No quotation history found for this vendor and tender"
            ));
        }

          return list.stream().map(quotation -> {
            QuotationViewHistoryDto dto = new QuotationViewHistoryDto();
            dto.setStatus(quotation.getStatus());
            dto.setRemarks(quotation.getRemarks());
            dto.setDate(quotation.getCreatedDate());
            return dto;
        }).collect(Collectors.toList());
    }





}
