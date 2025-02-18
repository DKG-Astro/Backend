package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsResponseDTO;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.entity.ProcurementModule.IndentMaterialMapping;
import com.astro.entity.ProcurementModule.MaterialDetails;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.repository.ProcurementModule.IndentCreation.IndentMaterialMappingRepository;
import com.astro.repository.ProcurementModule.IndentCreation.MaterialDetailsRepository;
import com.astro.service.IndentCreationService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO,String uploadingPriorApprovalsFileName,
                                                  String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName) {
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
        indentCreation.setUploadingPriorApprovalsFileName(uploadingPriorApprovalsFileName);
        indentCreation.setProjectName(indentRequestDTO.getProjectName());
        indentCreation.setIsPreBitMeetingRequired(indentRequestDTO.getIsPreBidMeetingRequired());
        String Date = indentRequestDTO.getPreBidMeetingDate();
        indentCreation.setPreBidMeetingDate(CommonUtils.convertStringToDateObject(Date));
        indentCreation.setPreBidMeetingVenue(indentRequestDTO.getPreBidMeetingVenue());
        indentCreation.setIsItARateContractIndent(indentRequestDTO.getIsItARateContractIndent());
        indentCreation.setEstimatedRate(indentRequestDTO.getEstimatedRate());
        indentCreation.setPeriodOfContract(indentRequestDTO.getPeriodOfContract());
        indentCreation.setSingleAndMultipleJob(indentRequestDTO.getSingleAndMultipleJob());
    indentCreation.setUploadTenderDocumentsFileName(uploadTenderDocumentsFileName);
    indentCreation.setUploadGOIOrRFPFileName(uploadGOIOrRFPFileName);
    indentCreation.setUploadPACOrBrandPACFileName(uploadPACOrBrandPACFileName);

        handleFileUpload(indentCreation, indentRequestDTO.getUploadingPriorApprovals(),
                indentCreation::setUploadingPriorApprovals);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadTenderDocuments(),
                indentCreation::setUploadTenderDocuments);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadGOIOrRFP(),
                indentCreation::setUploadGOIOrRFP);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadPACOrBrandPAC(),
                indentCreation::setUploadPACOrBrandPAC);

        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
     //   indentCreationRepository.save(indentCreation);

        // Save MaterialDetails entities and link them to the indentCreation
        List<MaterialDetails> materialDetailsList = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {
            MaterialDetails material = materialDetailsRepository.findByMaterialCode(materialRequest.getMaterialCode())
                    .orElse(new MaterialDetails());  // Fetch existing or create a new MaterialDetails object

           // MaterialDetails material = new MaterialDetails();
            material.setMaterialCode(materialRequest.getMaterialCode());
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
            indentCreation.getMaterialDetails().add(material);  // Add material to indent
            material.getIndentCreations().add(indentCreation);
            // ADD Indent to Material
          //  material.setIndentCreations(indentCreation);  // Associate with the current indentCreation
            return material;
        }).collect(Collectors.toList());

       // materialDetailsRepository.saveAll(materialDetailsList);  // Save all material details

        //indentCreation.setMaterialDetails(materialDetailsList);  // Set the list of material details on indentCreation
        // Save MaterialDetails with updated Many-to-Many relation
        indentCreationRepository.save(indentCreation);
        materialDetailsRepository.saveAll(materialDetailsList);
        indentCreation.setMaterialDetails(materialDetailsList);
        for (MaterialDetails material : materialDetailsList) {
            material.getIndentCreations().add(indentCreation);
        }
        // Associate materials with the indent
      //  indentCreation.setMaterialDetails(materialDetailsList);
        // Add the materials to the indent creation entity
       // indentCreation.getMaterialDetails().addAll(materialDetailsList);

        // Save IndentCreation again with updated material relationship
        indentCreationRepository.save(indentCreation);
        return mapToResponseDTO(indentCreation);
    }
/*

    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO, String uploadingPriorApprovalsFileName,
                                                 String uploadTenderDocumentsFileName, String uploadGOIOrRFPFileName, String uploadPACOrBrandPACFileName) {
        // Check if the indentId already exists
        if (indentCreationRepository.existsById(indentRequestDTO.getIndentId())) {
            throw new InvalidInputException(new ErrorDetails(400, 1, "Duplicate Indent ID", "Indent ID " + indentRequestDTO.getIndentId() + " already exists."));
        }

        // Validate material category consistency
        String materialCategory = null;
        for (MaterialDetailsRequestDTO materialRequest : indentRequestDTO.getMaterialDetails()) {
            if (materialCategory == null) {
                materialCategory = materialRequest.getMaterialCategory();
            } else if (!materialCategory.equals(materialRequest.getMaterialCategory())) {
                throw new InvalidInputException(new ErrorDetails(400, 2, "Inconsistent Material Category", "All materials must have the same material category."));
            }
        }

        // Create and populate IndentCreation entity
        IndentCreation indentCreation = new IndentCreation();
        indentCreation.setIndentId(indentRequestDTO.getIndentId());
        indentCreation.setIndentorName(indentRequestDTO.getIndentorName());
        indentCreation.setIndentorMobileNo(indentRequestDTO.getIndentorMobileNo());
        indentCreation.setIndentorEmailAddress(indentRequestDTO.getIndentorEmailAddress());
        indentCreation.setConsignesLocation(indentRequestDTO.getConsignesLocation());
        indentCreation.setProjectName(indentRequestDTO.getProjectName());
        indentCreation.setIsPreBitMeetingRequired(indentRequestDTO.getIsPreBidMeetingRequired());
        indentCreation.setPreBidMeetingDate(CommonUtils.convertStringToDateObject(indentRequestDTO.getPreBidMeetingDate()));
        indentCreation.setPreBidMeetingVenue(indentRequestDTO.getPreBidMeetingVenue());
        indentCreation.setIsItARateContractIndent(indentRequestDTO.getIsItARateContractIndent());
        indentCreation.setEstimatedRate(indentRequestDTO.getEstimatedRate());
        indentCreation.setPeriodOfContract(indentRequestDTO.getPeriodOfContract());
        indentCreation.setSingleAndMultipleJob(indentRequestDTO.getSingleAndMultipleJob());
        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
        indentCreation.setUploadingPriorApprovalsFileName(uploadingPriorApprovalsFileName);
        indentCreation.setUploadTenderDocumentsFileName(uploadTenderDocumentsFileName);
        indentCreation.setUploadGOIOrRFPFileName(uploadGOIOrRFPFileName);
        indentCreation.setUploadPACOrBrandPACFileName(uploadPACOrBrandPACFileName);

        indentCreationRepository.save(indentCreation);

        // Process MaterialDetails and create mappings
        List<IndentMaterialMapping> mappings = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {
            MaterialDetails material = materialDetailsRepository.findById(materialRequest.getMaterialCode())
                    .orElse(new MaterialDetails());

            material.setMaterialCode(materialRequest.getMaterialCode());
            material.setMaterialDescription(materialRequest.getMaterialDescription());
            material.setUom(materialRequest.getUom());
            material.setBudgetCode(materialRequest.getBudgetCode());
            material.setMaterialCategory(materialRequest.getMaterialCategory());
            material.setMaterialSubCategory(materialRequest.getMaterialSubCategory());
            material.setMaterialAndJob(materialRequest.getMaterialAndJob());

            materialDetailsRepository.save(material); // Save updated or new MaterialDetails

            // Create IndentMaterialMapping
            IndentMaterialMapping mapping = new IndentMaterialMapping();
            mapping.setIndentCreation(indentCreation);
            mapping.setMaterialDetails(material);
            mapping.setQuantity(materialRequest.getQuantity());
            mapping.setTotalPrice(materialRequest.getQuantity().multiply(materialRequest.getUnitPrice()));
            return mapping;
        }).collect(Collectors.toList());

        indentMaterialMappingRepository.saveAll(mappings); // Save mappings
        return mapToResponseDTO(indentCreation);
    }
*/
    public IndentCreationResponseDTO updateIndent(String indentId, IndentCreationRequestDTO indentRequestDTO,String uploadingPriorApprovalsFileName,
                                                  String uploadTenderDocumentsFileName,String uploadGOIOrRFPFileName,String uploadPACOrBrandPACFileName) {
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
        indentCreation.setUploadTenderDocumentsFileName(uploadTenderDocumentsFileName);
        indentCreation.setUploadGOIOrRFPFileName(uploadGOIOrRFPFileName);
        indentCreation.setUploadPACOrBrandPACFileName(uploadPACOrBrandPACFileName);
        indentCreation.setUploadingPriorApprovalsFileName(uploadingPriorApprovalsFileName);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadingPriorApprovals(),
                indentCreation::setUploadingPriorApprovals);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadTenderDocuments(),
                indentCreation::setUploadTenderDocuments);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadGOIOrRFP(),
                indentCreation::setUploadGOIOrRFP);
        handleFileUpload(indentCreation, indentRequestDTO.getUploadPACOrBrandPAC(),
                indentCreation::setUploadPACOrBrandPAC);
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());
       // indentCreationRepository.save(indentCreation);

        // Update MaterialDetails
       // List<MaterialDetails> existingMaterialDetails = indentCreation.getMaterialDetails();

        // Remove old material details
        //materialDetailsRepository.deleteAll(existingMaterialDetails);
        //  Remove old material relationships before deleting
        for (MaterialDetails material : indentCreation.getMaterialDetails()) {
            material.getIndentCreations().remove(indentCreation);
        }

        //  Delete old material details
        materialDetailsRepository.deleteAll(indentCreation.getMaterialDetails());

        // Add the new/updated material details
        List<MaterialDetails> materialDetailsList = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {
            MaterialDetails material = new MaterialDetails();
            material.setMaterialCode(materialRequest.getMaterialCode());
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
         //   material.setIndentCreation(indentCreation);
            return material;
        }).collect(Collectors.toList());

      //  materialDetailsRepository.saveAll(materialDetailsList);  // Save all material details
      //  indentCreation.setMaterialDetails(materialDetailsList);  // Set the list of material details on indentCreation
        materialDetailsList = materialDetailsRepository.saveAll(materialDetailsList); // Save materials

        // Re-establish relationship
        indentCreation.setMaterialDetails(materialDetailsList);
        for (MaterialDetails material : materialDetailsList) {
            material.getIndentCreations().add(indentCreation);
        }

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
