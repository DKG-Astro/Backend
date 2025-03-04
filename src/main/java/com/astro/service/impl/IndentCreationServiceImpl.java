package com.astro.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.*;
import com.astro.dto.workflow.ProcurementDtos.TechnoMomReportDTO;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.entity.ProcurementModule.IndentMaterialMapping;
import com.astro.entity.ProcurementModule.MaterialDetails;
import com.astro.entity.ProjectMaster;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.repository.ProcurementModule.IndentCreation.IndentMaterialMappingRepository;
import com.astro.repository.ProcurementModule.IndentCreation.MaterialDetailsRepository;
import com.astro.repository.ProjectMasterRepository;
import com.astro.service.IndentCreationService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;



@Service
public class IndentCreationServiceImpl implements IndentCreationService {

        @Autowired
        private IndentCreationRepository indentCreationRepository;

        @Autowired
        private MaterialDetailsRepository materialDetailsRepository;
        @Autowired
        private IndentMaterialMappingRepository indentMaterialMappingRepository;
        @Autowired
        private ProjectMasterRepository projectMasterRepository;

    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO){
            //,String uploadingPriorApprovalsFileName, String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName) {
        // Check if the indentorId already exists
        if (indentCreationRepository.existsById(indentRequestDTO.getIndentId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Indent ID", "Indent ID " + indentRequestDTO.getIndentId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

        // Iterate over materialDetails and check if materialCode already exists
        String materialCategory = null;
        for (MaterialDetailsRequestDTO materialRequest : indentRequestDTO.getMaterialDetails()) {
          /* if (materialDetailsRepository.existsById(materialRequest.getMaterialCode())) {
                ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Material Code",
                        "Material Code " + materialRequest.getMaterialCode() + " already exists.");
                throw new InvalidInputException(errorDetails);
            }

           */


            if (materialCategory == null) {
                materialCategory = materialRequest.getMaterialCategory();
            } else if (!materialCategory.equals(materialRequest.getMaterialCategory())) {
                throw new InvalidInputException(new ErrorDetails(400, 2, "Inconsistent Material Category",
                        "All materials must have the same material category."));
            }

        }

        IndentCreation indentCreation = new IndentCreation();

        indentCreation.setIndentorName(indentRequestDTO.getIndentorName());
        indentCreation.setIndentId(indentRequestDTO.getIndentId());
        indentCreation.setIndentorMobileNo(indentRequestDTO.getIndentorMobileNo());
        indentCreation.setIndentorEmailAddress(indentRequestDTO.getIndentorEmailAddress());
        indentCreation.setConsignesLocation(indentRequestDTO.getConsignesLocation());
        indentCreation.setUploadingPriorApprovalsFileName(indentRequestDTO.getUploadingPriorApprovalsFileName());
        indentCreation.setProjectName(indentRequestDTO.getProjectName());
        indentCreation.setIsPreBitMeetingRequired(indentRequestDTO.getIsPreBidMeetingRequired());
        String Date = indentRequestDTO.getPreBidMeetingDate();
        indentCreation.setPreBidMeetingDate(CommonUtils.convertStringToDateObject(Date));
        indentCreation.setPreBidMeetingVenue(indentRequestDTO.getPreBidMeetingVenue());
        indentCreation.setIsItARateContractIndent(indentRequestDTO.getIsItARateContractIndent());
        indentCreation.setEstimatedRate(indentRequestDTO.getEstimatedRate());
        indentCreation.setPeriodOfContract(indentRequestDTO.getPeriodOfContract());
        indentCreation.setSingleAndMultipleJob(indentRequestDTO.getSingleAndMultipleJob());
        indentCreation.setFileType(indentRequestDTO.getFileType());
    indentCreation.setUploadTenderDocumentsFileName(indentRequestDTO.getUploadTenderDocumentsFileName());
    indentCreation.setUploadGOIOrRFPFileName(indentRequestDTO.getUploadGOIOrRFPFileName());
    indentCreation.setUploadPACOrBrandPACFileName(indentRequestDTO.getUploadPACOrBrandPACFileName());
/*
        handleFileUpload(indentCreation, indentRequestDTO.getUploadingPriorApprovals(),
                indentCreation::setUploadingPriorApprovals);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadTenderDocuments(),
                indentCreation::setUploadTenderDocuments);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadGOIOrRFP(),
                indentCreation::setUploadGOIOrRFP);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadPACOrBrandPAC(),
                indentCreation::setUploadPACOrBrandPAC);

 */

        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
     //   indentCreationRepository.save(indentCreation);

        // Save MaterialDetails entities and link them to the indentCreation
        List<MaterialDetails> materialDetailsList = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {

            MaterialDetails material = new MaterialDetails();
            material.setMaterialCode(materialRequest.getMaterialCode());
            material.setIndentId(indentRequestDTO.getIndentId());
            material.setMaterialDescription(materialRequest.getMaterialDescription());
            material.setQuantity(materialRequest.getQuantity());
            material.setUnitPrice(materialRequest.getUnitPrice());
            material.setUom(materialRequest.getUom());
            // Calculate total price
            BigDecimal totalPrice = materialRequest.getQuantity().multiply(materialRequest.getUnitPrice());
            material.setTotalPrice(totalPrice);
            material.setBudgetCode(materialRequest.getBudgetCode());
            material.setMaterialCategory(materialRequest.getMaterialCategory());
            material.setMaterialSubCategory(materialRequest.getMaterialSubCategory());
            material.setMaterialAndJob(materialRequest.getMaterialAndJob());
            // Establish bidirectional relationship
          //  indentCreation.getMaterialDetails().add(material);  // Add material to indent
           // material.getIndentCreations().add(indentCreation);
            // ADD Indent to Material
            material.setIndentCreation(indentCreation);
          //  material.setIndentCreations(indentCreation);  // Associate with the current indentCreation
            return material;
        }).collect(Collectors.toList());

        indentCreation.setMaterialDetails(materialDetailsList);

        indentCreationRepository.save(indentCreation);

        return mapToResponseDTO(indentCreation);
    }

    public IndentCreationResponseDTO updateIndent(String indentId, IndentCreationRequestDTO indentRequestDTO){
            //,String uploadingPriorApprovalsFileName,String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName) {
        IndentCreation indentCreation = indentCreationRepository.findById(indentId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "indent not found for the provided indent ID.")
                ));

        indentCreation.setIndentorName(indentRequestDTO.getIndentorName());
        indentCreation.setIndentorMobileNo(indentRequestDTO.getIndentorMobileNo());
        indentCreation.setIndentorEmailAddress(indentRequestDTO.getIndentorEmailAddress());
        indentCreation.setConsignesLocation(indentRequestDTO.getConsignesLocation());
        indentCreation.setProjectName(indentRequestDTO.getProjectName());
        indentCreation.setIsPreBitMeetingRequired(indentRequestDTO.getIsPreBidMeetingRequired());
        String Date = indentRequestDTO.getPreBidMeetingDate();
        indentCreation.setPreBidMeetingDate(CommonUtils.convertStringToDateObject(Date));
        indentCreation.setPreBidMeetingVenue(indentRequestDTO.getPreBidMeetingVenue());
        indentCreation.setIsItARateContractIndent(indentRequestDTO.getIsItARateContractIndent());
        indentCreation.setEstimatedRate(indentRequestDTO.getEstimatedRate());
        indentCreation.setPeriodOfContract(indentRequestDTO.getPeriodOfContract());
        indentCreation.setSingleAndMultipleJob(indentRequestDTO.getSingleAndMultipleJob());
        indentCreation.setUploadTenderDocumentsFileName(indentRequestDTO.getUploadTenderDocumentsFileName());
        indentCreation.setUploadGOIOrRFPFileName(indentRequestDTO.getUploadGOIOrRFPFileName());
        indentCreation.setUploadPACOrBrandPACFileName(indentRequestDTO.getUploadPACOrBrandPACFileName());
        indentCreation.setUploadingPriorApprovalsFileName(indentRequestDTO.getUploadingPriorApprovalsFileName());
        indentCreation.setFileType(indentRequestDTO.getFileType());
      /*
        handleFileUpload(indentCreation, indentRequestDTO.getUploadingPriorApprovals(),
                indentCreation::setUploadingPriorApprovals);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadTenderDocuments(),
                indentCreation::setUploadTenderDocuments);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadGOIOrRFP(),
                indentCreation::setUploadGOIOrRFP);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadPACOrBrandPAC(),
                indentCreation::setUploadPACOrBrandPAC);
       */
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());

        //  Delete old material details
        materialDetailsRepository.deleteAll(indentCreation.getMaterialDetails());

        // Add the new/updated material details
        List<MaterialDetails> materialDetailsList = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {
            MaterialDetails material = new MaterialDetails();
            material.setMaterialCode(materialRequest.getMaterialCode());
            material.setIndentId(indentRequestDTO.getIndentId());
            material.setMaterialDescription(materialRequest.getMaterialDescription());
            material.setQuantity(materialRequest.getQuantity());
            material.setUnitPrice(materialRequest.getUnitPrice());
            material.setUom(materialRequest.getUom());
            // Calculate total price
            BigDecimal totalPrice = materialRequest.getQuantity().multiply(materialRequest.getUnitPrice());
            material.setTotalPrice(totalPrice);
            material.setBudgetCode(materialRequest.getBudgetCode());
            material.setMaterialCategory(materialRequest.getMaterialCategory());
            material.setMaterialSubCategory(materialRequest.getMaterialSubCategory());
            material.setMaterialAndJob(materialRequest.getMaterialAndJob());
            material.setIndentCreation(indentCreation);
            return material;
        }).collect(Collectors.toList());

        indentCreation.getMaterialDetails().clear();
        indentCreation.getMaterialDetails().addAll(materialDetailsList);


        // Save indent with updated materials
        indentCreationRepository.save(indentCreation);
        return mapToResponseDTO(indentCreation);
    }


    public IndentCreationResponseDTO getIndentById(String indentId) {
            IndentCreation indentCreation = indentCreationRepository.findById(indentId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Indent not found for the provided Indent ID.")
                ));
        return mapToResponseDTO(indentCreation);
        }

        // Get All Indents
        public List<IndentCreationResponseDTO> getAllIndents() {
            List<IndentCreation> indentList = indentCreationRepository.findAll();
            return indentList.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
        }



        private IndentCreationResponseDTO mapToResponseDTO(IndentCreation indentCreation) {
            IndentCreationResponseDTO response = new IndentCreationResponseDTO();
            response.setIndentorName(indentCreation.getIndentorName());
            response.setIndentId(indentCreation.getIndentId());
            response.setIndentorMobileNo(indentCreation.getIndentorMobileNo());
            response.setIndentorEmailAddress(indentCreation.getIndentorEmailAddress());
            response.setConsignesLocation(indentCreation.getConsignesLocation());
            response.setUploadingPriorApprovalsFileName(indentCreation.getUploadingPriorApprovalsFileName());
            response.setProjectName(indentCreation.getProjectName());
            response.setIsPreBidMeetingRequired(indentCreation.getIsPreBitMeetingRequired());
            LocalDate Date = indentCreation.getPreBidMeetingDate();
            response.setPreBidMeetingDate(CommonUtils.convertDateToString(Date));
            response.setPreBidMeetingVenue(indentCreation.getPreBidMeetingVenue());
            response.setIsItARateContractIndent(indentCreation.getIsItARateContractIndent());
            response.setEstimatedRate(indentCreation.getEstimatedRate());
            response.setPeriodOfContract(indentCreation.getPeriodOfContract());
            response.setSingleAndMultipleJob(indentCreation.getSingleAndMultipleJob());
         response.setUploadTenderDocumentsFileName(indentCreation.getUploadTenderDocumentsFileName());
         response.setUploadGOIOrRFPFileName(indentCreation.getUploadGOIOrRFPFileName());
           response.setUploadPACOrBrandPACFileName(indentCreation.getUploadPACOrBrandPACFileName());
           response.setFileType(indentCreation.getFileType());
          /*  String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            response.setUploadingPriorApprovalsFile(
                    indentCreation.getUploadingPriorApprovalsFileName() != null ?
                            baseUrl + "/api/indents/download-file/" + indentCreation.getIndentId() + "/priorApproval" : null
            );

            response.setUploadTenderDocumentsFile(
                    indentCreation.getUploadTenderDocumentsFileName() != null ?
                            baseUrl + "/api/indents/download-file/" + indentCreation.getIndentId() + "/tenderDocument" : null
            );

            response.setUploadGOIOrRFPFile(
                    indentCreation.getUploadGOIOrRFPFileName() != null ?
                            baseUrl + "/api/indents/download-file/" + indentCreation.getIndentId() + "/goiOrRFP" : null
            );

            response.setUploadPACOrBrandPACFile(
                    indentCreation.getUploadPACOrBrandPACFileName() != null ?
                            baseUrl + "/api/indents/download-file/" + indentCreation.getIndentId() + "/pacOrBrandPAC" : null
            );



           */
            response.setCreatedBy(indentCreation.getCreatedBy());
            response.setUpdatedBy(indentCreation.getUpdatedBy());

            // Set the filenames (if available)
          //  response.setUploadingPriorApprovalsFileName(indentCreation.getUploadingPriorApprovals() != null ? indentCreation.getUploadingPriorApprovals().getOriginalFilename() : null);
          //  response.setUploadTenderDocumentsFileName(indentCreation.getUploadTenderDocuments() != null ? indentCreation.getUploadTenderDocuments().getOriginalFilename() : null);
          //  response.setUploadGOIOrRFPFileName(indentCreation.getUploadGOIOrRFP() != null ? indentCreation.getUploadGOIOrRFP().getOriginalFilename() : null);
          //  response.setUploadPACOrBrandPACFileName(indentCreation.getUploadPACOrBrandPAC() != null ? indentCreation.getUploadPACOrBrandPAC().getOriginalFilename() : null);


            String materialCategory = indentCreation.getMaterialDetails().stream()
                    .map(MaterialDetails::getMaterialCategory)
                    .findFirst()
                    .orElse(null);
            // Converting "Capital" or "Consumable" to "Normal"
            if ("Capital".equalsIgnoreCase(materialCategory) || "Consumable".equalsIgnoreCase(materialCategory)) {
                materialCategory = "Normal";
            }
            response.setMaterialCategory(materialCategory);  //set material category to indent response

            // Map material details
            List<MaterialDetailsResponseDTO> materialDetailsResponse = indentCreation.getMaterialDetails().stream().map(material -> {
                MaterialDetailsResponseDTO materialResponse = new MaterialDetailsResponseDTO();
                materialResponse.setMaterialCode(material.getMaterialCode());
                materialResponse.setMaterialDescription(material.getMaterialDescription());
                materialResponse.setQuantity(material.getQuantity());
                materialResponse.setUnitPrice(material.getUnitPrice());
                materialResponse.setUom(material.getUom());
                materialResponse.setTotalPrice(material.getTotalPrice());
                materialResponse.setBudgetCode(material.getBudgetCode());
                materialResponse.setMaterialCategory(material.getMaterialCategory());
                materialResponse.setMaterialSubCategory(material.getMaterialSubCategory());
                materialResponse.setMaterialAndJob(material.getMaterialAndJob());
                return materialResponse;
            }).collect(Collectors.toList());

            // Calculate total price of all materials
            BigDecimal totalPriceOfAllMaterials = materialDetailsResponse.stream()
                    .map(MaterialDetailsResponseDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String projectName = indentCreation.getProjectName();
            BigDecimal allocatedAmount = projectMasterRepository
                    .findByProjectNameDescription(projectName)
                    .map(ProjectMaster::getAllocatedAmount)
                    .orElse(BigDecimal.ZERO);
            response.setProjectLimit(allocatedAmount);
            System.out.println("allocatedAmount: " + allocatedAmount);
            response.setTotalPriceOfAllMaterials(totalPriceOfAllMaterials);

            response.setMaterialDetails(materialDetailsResponse);

            return response;
        }
    @Override
    public void deleteIndent(String indentId) {

       IndentCreation  indent=indentCreationRepository.findById(indentId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Indent not found for the provided ID."
                        )
                ));
        try {
            indentCreationRepository.delete(indent);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  Indent."
                    ),
                    ex
            );
        }
    }
/*
    @Override
    public List<IndentReportDetailsDTO> getIndentReport(String startDate, String endDate) {

        List<Object[]> results = indentCreationRepository.fetchIndentReportDetails(
                CommonUtils.convertStringToDateObject(startDate),
                CommonUtils.convertStringToDateObject(endDate)
        );

        return results.stream().map(result -> {
            IndentReportDetailsDTO dto = new IndentReportDetailsDTO();
            dto.setIndentId((String) result[0]);
            dto.setApprovedDate((Date) result[1]);
            dto.setAssignedTo((String) result[2]);
            dto.setTenderRequest((String) result[3]);
            dto.setModeOfTendering((String) result[4]);
            dto.setCorrespondingPoSo((String) result[5]);
            dto.setStatusOfPoSo((String) result[6]);
            dto.setSubmittedDate((Date) result[7]);
            dto.setPendingApprovalWith((String) result[8]);
            dto.setPoSoApprovedDate((Date) result[9] );
            dto.setMaterial((String) result[10]);
            dto.setMaterialCategory((String) result[11]);
            dto.setMaterialSubCategory((String) result[12]);
            dto.setVendorName((String) result[13]);
            dto.setIndentorName((String) result[14]);
            dto.setValueOfIndent(result[15] != null ? ((BigDecimal) result[15]).doubleValue() : null);
            dto.setValueOfPo(result[16] != null ? ((BigDecimal) result[16]).doubleValue() : null);
            dto.setProject((String) result[17]);
            dto.setGrinNo((String) result[18]);
            dto.setInvoiceNo((String) result[19]);
            dto.setGissNo((String) result[20]);
            dto.setValuePendingToBePaid(result[21] != null ? ((BigDecimal) result[21]).doubleValue() : null);
            dto.setCurrentStageOfIndent((String) result[22]);
            dto.setShortClosedAndCancelled((String) result[23]);
            dto.setReasonForShortClosure((String) result[24]);

            return dto;
        }).collect(Collectors.toList());
    }


 */
@Override
public List<IndentReportDetailsDTO> getIndentReport(String startDate, String endDate) {
    // Convert String dates to LocalDate
    LocalDate startLocalDate = CommonUtils.convertStringToDateObject(startDate);
    LocalDate endLocalDate = CommonUtils.convertStringToDateObject(endDate);

    // Fetch results from the repository
    List<Object[]> results = indentCreationRepository.fetchIndentReportDetails(startLocalDate, endLocalDate);

    // Map results to DTO
    return results.stream().map(result -> {

        // Map each column to the DTO fields
        return new IndentReportDetailsDTO(
                (String) result[0],                          // indentId
                result[1] != null ? (Date) result[1] : null, // approvedDate
                (String) result[2],                          // assignedTo
                (String) result[3],                          // tenderRequest
                (String) result[4],                          // modeOfTendering
                (String) result[5],                          // correspondingPoSo
                (String) result[6],                          // statusOfPoSo
                result[7] != null ? (Date) result[7] : null, // submittedDate
                (String) result[8],                          // pendingApprovalWith
                result[9] != null ? (Date) result[9] : null, // poSoApprovedDate
                (String) result[10],                         // material
                (String) result[11],                         // materialCategory
                (String) result[12],                         // materialSubCategory
                (String) result[13],                         // vendorName
                (String) result[14],                         // indentorName
                result[15] != null ? ((BigDecimal) result[15]).doubleValue() : null, // valueOfIndent
                result[16] != null ? ((BigDecimal) result[16]).doubleValue() : null, // valueOfPo
                (String) result[17],                        // project
              //  (String) result[18],                        // grinNo
                (String) result[18],                         // invoiceNo
                (String) result[19],                        // gissNo
                result[20] != null ? ((BigDecimal) result[20]).doubleValue() : null, // valuePendingToBePaid
                (String) result[21],                        // currentStageOfIndent
                (String) result[22],                        // shortClosedAndCancelled
                (String) result[23]                          // reasonForShortClosure
        );
    }).collect(Collectors.toList());
}
    public List<TechnoMomReportDTO> getTechnoMomReport(String startDate, String endDate) {
        List<Object[]> results =indentCreationRepository.getTechnoMomReport(CommonUtils.convertStringToDateObject(startDate), CommonUtils.convertStringToDateObject(endDate));

        return results.stream().map(obj -> new TechnoMomReportDTO(
                (Date) obj[0],
                (String) obj[1],
                (String) obj[2],
                obj[3] != null ? new BigDecimal(obj[3].toString()) : BigDecimal.ZERO, // value
                (String) obj[4]
        )).collect(Collectors.toList());

    }

    public void handleFileUpload(IndentCreation indentCreation, MultipartFile file, Consumer<byte[]> fileSetter) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileSetter.accept(fileBytes); // Set file content (byte[])

            } catch (IOException e) {
                throw new InvalidInputException(new ErrorDetails(500, 3, "File Processing Error",
                        "Error while processing the uploaded file. Please try again."));
            }
        } else {
            fileSetter.accept(null);  // Handle gracefully if no file is uploaded
        }
    }


}
