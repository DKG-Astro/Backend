package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationRequestDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsResponseDTO;
import com.astro.entity.InventoryModule.Gprn;
import com.astro.entity.ProcurementModule.IndentCreation;
import com.astro.entity.ProcurementModule.MaterialDetails;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.ProcurementModule.IndentCreation.IndentCreationRepository;
import com.astro.repository.ProcurementModule.IndentCreation.MaterialDetailsRepository;
import com.astro.service.IndentCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndentCreationServiceImpl implements IndentCreationService {

        @Autowired
        private IndentCreationRepository indentCreationRepository;

        @Autowired
        private MaterialDetailsRepository materialDetailsRepository;

    public IndentCreationResponseDTO createIndent(IndentCreationRequestDTO indentRequestDTO) {
        // Save IndentCreation entity
        IndentCreation indentCreation = new IndentCreation();
        indentCreation.setIndentorName(indentRequestDTO.getIndentorName());
        indentCreation.setIndentorId(indentRequestDTO.getIndentorId());
        indentCreation.setIndentorMobileNo(indentRequestDTO.getIndentorMobileNo());
        indentCreation.setIndentorEmailAddress(indentRequestDTO.getIndentorEmailAddress());
        indentCreation.setConsignesLocation(indentRequestDTO.getConsignesLocation());
        indentCreation.setUploadingPriorApprovals(indentRequestDTO.getUploadingPriorApprovals());
        indentCreation.setProjectName(indentRequestDTO.getProjectName());
        indentCreation.setIsPreBitMeetingRequired(indentRequestDTO.getIsPreBidMeetingRequired());
        indentCreation.setPreBidMeetingDate(indentRequestDTO.getPreBidMeetingDate());
        indentCreation.setPreBidMeetingVenue(indentRequestDTO.getPreBidMeetingVenue());
        indentCreation.setIsItARateContractIndent(indentRequestDTO.getIsItARateContractIndent());
        indentCreation.setEstimatedRate(indentRequestDTO.getEstimatedRate());
        indentCreation.setPeriodOfContract(indentRequestDTO.getPeriodOfContract());
        indentCreation.setSingleAndMultipleJob(indentRequestDTO.getSingleAndMultipleJob());
        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
        indentCreationRepository.save(indentCreation);

        // Save MaterialDetails entities and link them to the indentCreation
        List<MaterialDetails> materialDetailsList = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {
            MaterialDetails material = new MaterialDetails();
            material.setMaterialCode(materialRequest.getMaterialCode());
            material.setMaterialDescription(materialRequest.getMaterialDescription());
            material.setQuantity(materialRequest.getQuantity());
            material.setUnitPrice(materialRequest.getUnitPrice());
            material.setUom(materialRequest.getUom());
            material.setTotalPrice(materialRequest.getTotalPrize());
            material.setBudgetCode(materialRequest.getBudgetCode());
            material.setMaterialCategory(materialRequest.getMaterialCategory());
            material.setMaterialSubCategory(materialRequest.getMaterialSubCategory());
            material.setMaterialAndJob(materialRequest.getMaterialAndJob());
            material.setIndentCreation(indentCreation);  // Associate with the current indentCreation
            return material;
        }).collect(Collectors.toList());

        materialDetailsRepository.saveAll(materialDetailsList);  // Save all material details

        indentCreation.setMaterialDetails(materialDetailsList);  // Set the list of material details on indentCreation

        return mapToResponseDTO(indentCreation);
    }

    public IndentCreationResponseDTO updateIndent(Long indentId, IndentCreationRequestDTO indentRequestDTO) {
        IndentCreation indentCreation = indentCreationRepository.findById(indentId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "indent not found for the provided asset ID.")
                ));

        indentCreation.setIndentorName(indentRequestDTO.getIndentorName());
        indentCreation.setIndentorId(indentRequestDTO.getIndentorId());
        indentCreation.setIndentorMobileNo(indentRequestDTO.getIndentorMobileNo());
        indentCreation.setIndentorEmailAddress(indentRequestDTO.getIndentorEmailAddress());
        indentCreation.setConsignesLocation(indentRequestDTO.getConsignesLocation());
        indentCreation.setUploadingPriorApprovals(indentRequestDTO.getUploadingPriorApprovals());
        indentCreation.setProjectName(indentRequestDTO.getProjectName());
        indentCreation.setIsPreBitMeetingRequired(indentRequestDTO.getIsPreBidMeetingRequired());
        indentCreation.setPreBidMeetingDate(indentRequestDTO.getPreBidMeetingDate());
        indentCreation.setPreBidMeetingVenue(indentRequestDTO.getPreBidMeetingVenue());
        indentCreation.setIsItARateContractIndent(indentRequestDTO.getIsItARateContractIndent());
        indentCreation.setEstimatedRate(indentRequestDTO.getEstimatedRate());
        indentCreation.setPeriodOfContract(indentRequestDTO.getPeriodOfContract());
        indentCreation.setSingleAndMultipleJob(indentRequestDTO.getSingleAndMultipleJob());
        indentCreation.setUpdatedBy(indentRequestDTO.getUpdatedBy());
        indentCreation.setCreatedBy(indentRequestDTO.getCreatedBy());
        indentCreationRepository.save(indentCreation);

        // Update MaterialDetails
        List<MaterialDetails> existingMaterialDetails = indentCreation.getMaterialDetails();

        // Remove old material details
        materialDetailsRepository.deleteAll(existingMaterialDetails);

        // Add the new/updated material details
        List<MaterialDetails> materialDetailsList = indentRequestDTO.getMaterialDetails().stream().map(materialRequest -> {
            MaterialDetails material = new MaterialDetails();
            material.setMaterialCode(materialRequest.getMaterialCode());
            material.setMaterialDescription(materialRequest.getMaterialDescription());
            material.setQuantity(materialRequest.getQuantity());
            material.setUnitPrice(materialRequest.getUnitPrice());
            material.setUom(materialRequest.getUom());
            material.setTotalPrice(materialRequest.getTotalPrize());
            material.setBudgetCode(materialRequest.getBudgetCode());
            material.setMaterialCategory(materialRequest.getMaterialCategory());
            material.setMaterialSubCategory(materialRequest.getMaterialSubCategory());
            material.setMaterialAndJob(materialRequest.getMaterialAndJob());
            material.setIndentCreation(indentCreation);
            return material;
        }).collect(Collectors.toList());

        materialDetailsRepository.saveAll(materialDetailsList);  // Save all material details
        indentCreation.setMaterialDetails(materialDetailsList);  // Set the list of material details on indentCreation

        return mapToResponseDTO(indentCreation);
    }


    public IndentCreationResponseDTO getIndentById(Long indentId) {
            IndentCreation indentCreation = indentCreationRepository.findById(indentId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Indent not found for the provided asset ID.")
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
            response.setId(indentCreation.getId());
            response.setIndentorName(indentCreation.getIndentorName());
            response.setIndentorId(indentCreation.getIndentorId());
            response.setIndentorMobileNo(indentCreation.getIndentorMobileNo());
            response.setIndentorEmailAddress(indentCreation.getIndentorEmailAddress());
            response.setConsignesLocation(indentCreation.getConsignesLocation());
            response.setUploadingPriorApprovals(indentCreation.getUploadingPriorApprovals());
            response.setProjectName(indentCreation.getProjectName());
            response.setIsPreBidMeetingRequired(indentCreation.getIsPreBitMeetingRequired());
            response.setPreBidMeetingDate(indentCreation.getPreBidMeetingDate());
            response.setPreBidMeetingVenue(indentCreation.getPreBidMeetingVenue());
            response.setIsItARateContractIndent(indentCreation.getIsItARateContractIndent());
            response.setEstimatedRate(indentCreation.getEstimatedRate());
            response.setPeriodOfContract(indentCreation.getPeriodOfContract());
            response.setSingleAndMultipleJob(indentCreation.getSingleAndMultipleJob());
            response.setCreatedBy(indentCreation.getCreatedBy());
            response.setUpdatedBy(indentCreation.getUpdatedBy());
            // Map material details
            List<MaterialDetailsResponseDTO> materialDetailsResponse = indentCreation.getMaterialDetails().stream().map(material -> {
                MaterialDetailsResponseDTO materialResponse = new MaterialDetailsResponseDTO();
                materialResponse.setMaterialCode(material.getMaterialCode());
                materialResponse.setMaterialDescription(material.getMaterialDescription());
                materialResponse.setQuantity(material.getQuantity());
                materialResponse.setUnitPrice(material.getUnitPrice());
                materialResponse.setUom(material.getUom());
                materialResponse.setTotalPrize(material.getTotalPrice());
                materialResponse.setBudgetCode(material.getBudgetCode());
                materialResponse.setMaterialCategory(material.getMaterialCategory());
                materialResponse.setMaterialSubCategory(material.getMaterialSubCategory());
                materialResponse.setMaterialAndJob(material.getMaterialAndJob());
                return materialResponse;
            }).collect(Collectors.toList());

            response.setMaterialDetails(materialDetailsResponse);

            return response;
        }
    @Override
    public void deleteIndent(Long id) {

       IndentCreation  gprn=indentCreationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Indent not found for the provided ID."
                        )
                ));
        try {
            indentCreationRepository.delete(gprn);
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
    }
