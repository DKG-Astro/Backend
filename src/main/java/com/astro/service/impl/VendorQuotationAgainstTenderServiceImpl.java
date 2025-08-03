package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.*;
import com.astro.dto.workflow.ProcurementDtos.QuotationViewHistoryDto;
import com.astro.dto.workflow.ProcurementDtos.VendorQuotationChangeRequestDto;
import com.astro.entity.ProcurementModule.TenderEvaluation;
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
import com.astro.repository.ProcurementModule.TenderEvaluationRepository;
import com.astro.repository.ProcurementModule.TenderRequestRepository;
import com.astro.service.VendorQuotationAgainstTenderService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Autowired
    private TenderEvaluationRepository tenderEvaluationRepository;
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
       quotation.setPriceBidFileName(dto.getPriceBidFileName());
       quotation.setFileType(dto.getFileType());
       quotation.setCreatedBy(dto.getCreatedBy());
       quotation.setVersion(maxVersion + 1);
       quotation.setIsLatest(true);
       quotation.setStatus("SUBMITTED");
       quotation.setModifiedBy(1);
       quotation.setCurrentRole(VendorQuotationAgainstTender.WorkflowActorRole.VENDOR);
       quotation.setNextRole(VendorQuotationAgainstTender.WorkflowActorRole.INDENTOR);
       quotation.setCreatedDate(LocalDateTime.now());
       quotation.setUpdatedDate(LocalDateTime.now());

       VendorQuotationAgainstTender saved = vendorQuotationAgainstTenderRepository.save(quotation);
       return mapToResponse(saved);
   }


   @Override
   public List<VendorQuotationAgainstTenderDto> getQuotationsByTenderId(String tenderId, String loggedInRole) {

       List<VendorQuotationAgainstTender> allLatest = vendorQuotationAgainstTenderRepository.findLatestVersionsForTender(tenderId);

       return allLatest.stream()
               .map(vq -> {
                   VendorQuotationAgainstTenderDto dto = new VendorQuotationAgainstTenderDto();
                   dto.setTenderId(vq.getTenderId());
                   dto.setVendorId(vq.getVendorId());
                   dto.setQuotationFileName(vq.getQuotationFileName());
                   dto.setFileType(vq.getFileType());
                   dto.setCreatedBy(vq.getCreatedBy());
                   dto.setVersion(vq.getVersion());
                   dto.setRemarks(vq.getRemarks());
                   dto.setStatus(vq.getStatus()); //  SUBMITTED, CHANGE_REQUESTED, Rejected
                   dto.setIndentorStatus(vq.getIndentorStatus());
                   dto.setSopStatus(vq.getSpoStatus());
                 //  dto.setAcceptanceStatus(vq.getAcceptanceStatus()); // ACCEPTED etc.

                   dto.setCanIndentorAct(canIndentorAct(vq, loggedInRole));
                   dto.setCanSpoAct(canSpoAct(vq, loggedInRole));

                   return dto;
               })
               .collect(Collectors.toList());
   }

private boolean canIndentorAct(VendorQuotationAgainstTender vq, String role) {
    // Only Indentor or Purchase Personal can act
    if (!(role.equalsIgnoreCase("INDENT CREATOR") || role.equalsIgnoreCase("Purchase personnel"))) {
        return false;
    }

    String status = vq.getStatus();
    String nextRole = String.valueOf(vq.getNextRole());

    // Indentor can act only if they are the next role and status is actionable
    if ("INDENTOR".equalsIgnoreCase(nextRole) &&
            !"ACCEPTED".equalsIgnoreCase(status) &&
            !"REJECTED".equalsIgnoreCase(status)) {
        return true;
    }

    return false;
}
    private String normalizeRole(String role) {
        if (role == null) return "";
        return role.trim().replaceAll("\\s+", "_").toUpperCase();
    }

    private boolean canSpoAct(VendorQuotationAgainstTender vq, String role) {
    String roleName= normalizeRole(role);
        if (!"STORE_PURCHASE_OFFICER".equalsIgnoreCase(roleName)) {
            return false;
        }

        // If SPO already sent a change request back to indentor and waiting, block further SPO action
        if (Boolean.TRUE.equals(vq.getChangeRequestToIndentor())) {
            return false;
        }

        // SPO can act if it's their turn (nextRole == STORE_PURCHASE_OFFICER)
        return vq.getNextRole() == VendorQuotationAgainstTender.WorkflowActorRole.STORE_PURCHASE_OFFICER;
    }





    public VendorQuotationAcceptedAndRejectedDataDto getAllVendorQuotationsByTenderId(String tenderId) {
        VendorQuotationAcceptedAndRejectedDataDto responseDto = new VendorQuotationAcceptedAndRejectedDataDto();


        List<VendorQuotationAgainstTender> vqList = vendorQuotationAgainstTenderRepository
                .findRejectedOrAcceptedQuotations(tenderId);


        List<VendorQuotationAgainstTenderDto> dtoList = vqList.stream()
                .map(vq -> {
                    VendorQuotationAgainstTenderDto dto = new VendorQuotationAgainstTenderDto();
                    dto.setTenderId(vq.getTenderId());
                    dto.setVendorId(vq.getVendorId());
                    dto.setQuotationFileName(vq.getQuotationFileName());
                    dto.setFileType(vq.getFileType());
                    dto.setCreatedBy(vq.getCreatedBy());
                    dto.setVersion(vq.getVersion());
                    dto.setRemarks(vq.getRemarks());
                    dto.setStatus(vq.getStatus());

                    return dto;
                })
                .collect(Collectors.toList());


        responseDto.setVendor(dtoList);


        TenderEvaluation tr = tenderEvaluationRepository.findByTenderId(tenderId);

        if (tr != null) {
            responseDto.setUploadQualifiedVendorsFileName(tr.getUploadQualifiedVendorsFileName());
        }

        return responseDto;
    }





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


   public boolean updateStatusAndRemarks(VendorQuotationUpdateRequestDto request) {
       List<VendorQuotationAgainstTender> quotations = vendorQuotationAgainstTenderRepository.findByTenderIdAndVendorId(
               request.getTenderId(), request.getVendorId());

       if (!quotations.isEmpty()) {
           // You can choose to update all or only the latest one
           for (VendorQuotationAgainstTender record : quotations) {
               record.setStatus(request.getStatus());
               record.setRemarks(request.getRemarks());
               record.setUpdatedDate(LocalDateTime.now());
             //  quotation.setIndentorStatus("ACCEPTED");
            //   quotation.setIndentorRemarks("Accepted by indentor");
               vendorQuotationAgainstTenderRepository.save(record);
           }
           return true;
       } else {
           return false;
       }
   }


public boolean markQuotationForChangeRequest(VendorQuotationChangeRequestDto request) {
    Optional<VendorQuotationAgainstTender> optional = vendorQuotationAgainstTenderRepository
            .findTopByTenderIdAndVendorIdAndIsLatestTrueOrderByVersionDesc(
                    request.getTenderId(), request.getVendorId());

    if (optional.isEmpty()) return false;

    VendorQuotationAgainstTender oldQuotation = optional.get();

    // Mark old version as not latest
    oldQuotation.setIsLatest(false);
    vendorQuotationAgainstTenderRepository.save(oldQuotation);

    // Create new version
    VendorQuotationAgainstTender newQuotation = new VendorQuotationAgainstTender();
    newQuotation.setTenderId(oldQuotation.getTenderId());
    newQuotation.setVendorId(oldQuotation.getVendorId());
    newQuotation.setQuotationFileName(oldQuotation.getQuotationFileName());
    newQuotation.setFileType(oldQuotation.getFileType());
    newQuotation.setCreatedBy(oldQuotation.getCreatedBy());
    newQuotation.setVersion(oldQuotation.getVersion() + 1);
    newQuotation.setIsLatest(true);
    newQuotation.setCreatedDate(LocalDateTime.now());
    newQuotation.setUpdatedDate(LocalDateTime.now());

    // Copy other statuses if required
    newQuotation.setAcceptanceStatus(oldQuotation.getAcceptanceStatus());
    newQuotation.setSpoStatus(oldQuotation.getSpoStatus());
    newQuotation.setSpoRemarks(oldQuotation.getSpoRemarks());

    // Set Indentor change request values
    newQuotation.setStatus("CHANGE_REQUESTED");
    newQuotation.setIndentorStatus("CHANGE_REQUESTED");
    newQuotation.setIndentorRemarks(request.getRemarks());
    newQuotation.setStatus("CHANGE_REQUESTED");
   // newQuotation.setRemarks(request.getRemarks());
    newQuotation.setModifiedBy(request.getUserId());
    newQuotation.setCurrentRole(VendorQuotationAgainstTender.WorkflowActorRole.INDENTOR);
    newQuotation.setNextRole(VendorQuotationAgainstTender.WorkflowActorRole.VENDOR);


    vendorQuotationAgainstTenderRepository.save(newQuotation);
    return true;
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

public boolean acceptVendorQuotation(String tenderId, String vendorId,Integer userId) {
    Optional<VendorQuotationAgainstTender> optional =
            vendorQuotationAgainstTenderRepository
                    .findTopByTenderIdAndVendorIdAndIsLatestTrueOrderByVersionDesc(tenderId, vendorId);

    if (optional.isEmpty()) return false;

    VendorQuotationAgainstTender oldQuotation = optional.get();
    oldQuotation.setIsLatest(false);
    vendorQuotationAgainstTenderRepository.save(oldQuotation);

    VendorQuotationAgainstTender newQuotation = new VendorQuotationAgainstTender();
    newQuotation.setTenderId(tenderId);
    newQuotation.setVendorId(vendorId);
    newQuotation.setQuotationFileName(oldQuotation.getQuotationFileName());
    newQuotation.setFileType(oldQuotation.getFileType());
    newQuotation.setCreatedBy(oldQuotation.getCreatedBy());
    newQuotation.setVersion(oldQuotation.getVersion() + 1);
    newQuotation.setIsLatest(true);
    newQuotation.setCreatedDate(LocalDateTime.now());
    newQuotation.setUpdatedDate(LocalDateTime.now());

    // Indentor accepted, hand over to SPO
    newQuotation.setStatus("PENDING_SPO"); // intermediate
  //  newQuotation.setRemarks("Accepted by indentor");
    newQuotation.setIndentorStatus("ACCEPTED");
    newQuotation.setIndentorRemarks("Accepted by indentor");
    newQuotation.setModifiedBy(userId);
    newQuotation.setCurrentRole(VendorQuotationAgainstTender.WorkflowActorRole.INDENTOR);
    newQuotation.setNextRole(VendorQuotationAgainstTender.WorkflowActorRole.STORE_PURCHASE_OFFICER);

    vendorQuotationAgainstTenderRepository.save(newQuotation);
    return true;
}



    /**
     * Store/Purchase Officer reviews after indentor decision.
     * action: "ACCEPT", "REJECT", "CHANGE_REQUEST_TO_INTENTOR"
     */
    public boolean storeOfficerReviewQuotation(String tenderId, String vendorId, String action, String remarks, Integer userId) {
        Optional<VendorQuotationAgainstTender> optional =
                vendorQuotationAgainstTenderRepository.findTopByTenderIdAndVendorIdAndIsLatestTrueOrderByVersionDesc(tenderId, vendorId);

        if (optional.isEmpty()) return false;

        VendorQuotationAgainstTender oldQuotation = optional.get();

        // Mark old as not latest
        oldQuotation.setIsLatest(false);
        vendorQuotationAgainstTenderRepository.save(oldQuotation);

        // Create new version
        VendorQuotationAgainstTender newQuotation = new VendorQuotationAgainstTender();
        newQuotation.setTenderId(oldQuotation.getTenderId());
        newQuotation.setVendorId(oldQuotation.getVendorId());
        newQuotation.setQuotationFileName(oldQuotation.getQuotationFileName());
        newQuotation.setFileType(oldQuotation.getFileType());
        newQuotation.setCreatedBy(oldQuotation.getCreatedBy());
        newQuotation.setVersion(oldQuotation.getVersion() + 1);
        newQuotation.setIsLatest(true);
        newQuotation.setCreatedDate(LocalDateTime.now());
        newQuotation.setUpdatedDate(LocalDateTime.now());

        newQuotation.setIndentorStatus(oldQuotation.getIndentorStatus());
        newQuotation.setIndentorRemarks(oldQuotation.getIndentorRemarks());
        newQuotation.setModifiedBy(userId);
        newQuotation.setCurrentRole(VendorQuotationAgainstTender.WorkflowActorRole.STORE_PURCHASE_OFFICER);

        switch (action) {
            case "ACCEPT":
                newQuotation.setSpoStatus("ACCEPTED");
                newQuotation.setSpoRemarks(remarks);
                newQuotation.setAcceptanceStatus("ACCEPTED");
                newQuotation.setChangeRequestToIndentor(false);
                newQuotation.setStatus("Completed");
                newQuotation.setRemarks(remarks);
                newQuotation.setNextRole(null);
                break;
            case "REJECT":
                newQuotation.setSpoStatus("REJECTED");
                newQuotation.setSpoRemarks(remarks);
                newQuotation.setAcceptanceStatus("REJECTED");
                newQuotation.setChangeRequestToIndentor(false);
                newQuotation.setStatus("REJECTED");
                newQuotation.setRemarks(remarks);
                newQuotation.setNextRole(null);
                break;
            case "CHANGE_REQUEST_TO_INTENTOR":
                newQuotation.setSpoStatus("CHANGE_REQUESTED_TO_INTENTOR");
                newQuotation.setSpoRemarks(remarks);
                newQuotation.setChangeRequestToIndentor(true);

                newQuotation.setStatus("CHANGE_REQUESTED");
                newQuotation.setRemarks(remarks);
                newQuotation.setNextRole(VendorQuotationAgainstTender.WorkflowActorRole.INDENTOR);
                break;
            default:
                return false;
        }

        vendorQuotationAgainstTenderRepository.save(newQuotation);
        return true;
    }

    @Transactional
    public boolean rejectVendorQuotation(String tenderId, String vendorId, String remarks, Integer userId) {
        Logger log = LoggerFactory.getLogger(getClass());

        Optional<VendorQuotationAgainstTender> optional =
                vendorQuotationAgainstTenderRepository
                        .findTopByTenderIdAndVendorIdAndIsLatestTrueOrderByVersionDesc(tenderId, vendorId);

        if (optional.isEmpty()) {
            log.warn("No latest quotation found to reject for tenderId={} vendorId={}", tenderId, vendorId);
            return false;
        }

        VendorQuotationAgainstTender oldQuotation = optional.get();

        // mark previous as not latest
        oldQuotation.setIsLatest(false);
        vendorQuotationAgainstTenderRepository.save(oldQuotation);

        // build new version
        VendorQuotationAgainstTender newQuotation = new VendorQuotationAgainstTender();
        newQuotation.setTenderId(tenderId);
        newQuotation.setVendorId(vendorId);
        newQuotation.setQuotationFileName(oldQuotation.getQuotationFileName());
        newQuotation.setFileType(oldQuotation.getFileType());
        newQuotation.setCreatedBy(oldQuotation.getCreatedBy());
        newQuotation.setVersion((oldQuotation.getVersion() != null ? oldQuotation.getVersion() : 0) + 1);
        newQuotation.setIsLatest(true);
        newQuotation.setCreatedDate(LocalDateTime.now());
        newQuotation.setUpdatedDate(LocalDateTime.now());

        // Indentor rejected → forward to SPO
        newQuotation.setStatus("PENDING_SPO"); // intermediate
        newQuotation.setRemarks(remarks);
        newQuotation.setIndentorStatus("REJECTED");
        newQuotation.setIndentorRemarks(remarks);
        newQuotation.setModifiedBy(userId); // indentor
        newQuotation.setCurrentRole(VendorQuotationAgainstTender.WorkflowActorRole.INDENTOR);
        newQuotation.setNextRole(VendorQuotationAgainstTender.WorkflowActorRole.STORE_PURCHASE_OFFICER);

        // (Optional) clear acceptance/spo fields if not applicable
        newQuotation.setAcceptanceStatus(null);
        newQuotation.setSpoStatus(null);
        newQuotation.setSpoRemarks(null);
        newQuotation.setChangeRequestToIndentor(false);

        VendorQuotationAgainstTender saved = vendorQuotationAgainstTenderRepository.save(newQuotation);
        log.info("Created new rejected quotation version: tenderId={} vendorId={} version={}",
                tenderId, vendorId, saved.getVersion());

        return true;
    }

    @Override
    public List<TenderEvaluationHistory> getFullQuotationHistory(String tenderId, String vendorId) {
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

        return list.stream().map(vq -> {
            TenderEvaluationHistory dto = new TenderEvaluationHistory();
            dto.setId(vq.getId());
            dto.setTenderId(vq.getTenderId());
            dto.setVendorId(vq.getVendorId());
            dto.setQuotationFileName(vq.getQuotationFileName());
            dto.setFileType(vq.getFileType());
            dto.setStatus(vq.getStatus());
            dto.setRemarks(vq.getRemarks());
            dto.setVersion(vq.getVersion());
            dto.setIsLatest(vq.getIsLatest());
            dto.setCreatedBy(vq.getCreatedBy());
            dto.setAcceptanceStatus(vq.getAcceptanceStatus());
            dto.setCreatedDate(vq.getCreatedDate());
            dto.setUpdatedDate(vq.getUpdatedDate());
            dto.setIndentorStatus(vq.getIndentorStatus());
            dto.setIndentorRemarks(vq.getIndentorRemarks());
            dto.setSpoStatus(vq.getSpoStatus());
            dto.setSpoRemarks(vq.getSpoRemarks());
            dto.setChangeRequestToIndentor(vq.getChangeRequestToIndentor());
            dto.setModifiedBy(vq.getModifiedBy());
            dto.setCurrentRole(vq.getCurrentRole());
            dto.setNextRole(vq.getNextRole());
            return dto;
        }).collect(Collectors.toList());

    }

    @Override
    public List<String> getVendorsWithCompletedQuotation(String tenderId) {
        return vendorQuotationAgainstTenderRepository.findVendorIdsWithCompletedStatus(tenderId);
    }



}
