package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ApprovalAndRejectionRequestDTO;
import com.astro.dto.workflow.MaterialMasterUtilRequestDto;
import com.astro.dto.workflow.MaterialMasterUtilResponseDto;
import com.astro.dto.workflow.MaterialTransitionHistory;
import com.astro.entity.*;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.MaterialMasterRepository;
import com.astro.repository.MaterialMasterUtilRepository;
import com.astro.repository.MaterialStatusRepository;
import com.astro.repository.UserRoleMasterRepository;
import com.astro.service.MaterialMasterUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialMasterUtilServiceImpl implements MaterialMasterUtilService {

    @Autowired
    private MaterialMasterUtilRepository materialMasterUtilRepository;

    @Autowired
    private UserRoleMasterRepository userRoleMasterRepository;

    @Autowired
    private MaterialMasterRepository materialMasterRepository;
    @Autowired
    private MaterialStatusRepository materialStatusRepository;

    @Override
    public MaterialMasterUtilResponseDto createMaterial(MaterialMasterUtilRequestDto dto) {
        MaterialMasterUtil material = new MaterialMasterUtil();

        String materialId = "M" + System.currentTimeMillis();

        material.setMaterialCode(materialId);
        material.setCategory(dto.getCategory());
        material.setSubCategory(dto.getSubCategory());
        material.setDescription(dto.getDescription());
        material.setUom(dto.getUom());
        material.setUnitPrice(dto.getUnitPrice());
        material.setCurrency(dto.getCurrency());
        material.setEstimatedPriceWithCcy(dto.getEstimatedPriceWithCcy());
        material.setUploadImageName(dto.getUploadImageFileName());
        material.setIndigenousOrImported(dto.getIndigenousOrImported());
        material.setApprovalStatus(MaterialMasterUtil.ApprovalStatus.AWAITING_APPROVAL);
        material.setComments(null);
        material.setCreatedBy(dto.getCreatedBy());
        material.setUpdatedBy(dto.getUpdatedBy());

        material = materialMasterUtilRepository.save(material);

       saveMaterialTracking(material.getMaterialCode(), "CREATED", material.getApprovalStatus().name(), material.getComments() , dto.getCreatedBy());

       // saveMaterialTracking(material.getMaterialCode(), "CREATED", request.getAction(), "Material created in MasterUtil", request.getActionBy());

        return mapToResponse(material);
    }



    @Override
    public List<MaterialMasterUtilResponseDto> getAllAwaitingApprovalMaterials() {
        List<MaterialMasterUtil> materials =materialMasterUtilRepository.findByApprovalStatus(MaterialMasterUtil.ApprovalStatus.AWAITING_APPROVAL);

        return materials.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<MaterialMasterUtilResponseDto> getAllChangeRequestMaterials() {
        List<MaterialMasterUtil> materials =materialMasterUtilRepository.findByApprovalStatus(MaterialMasterUtil.ApprovalStatus.CHANGE_REQUEST);

        return materials.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    private MaterialMasterUtilResponseDto mapToResponse(MaterialMasterUtil material) {

        MaterialMasterUtilResponseDto response = new MaterialMasterUtilResponseDto();

        response.setMaterialCode(material.getMaterialCode());
        response.setCategory(material.getCategory());
        response.setSubCategory(material.getSubCategory());
        response.setDescription(material.getDescription());
        response.setUom(material.getUom());
        response.setUnitPrice(material.getUnitPrice());
        response.setCurrency(material.getCurrency());
        response.setEstimatedPriceWithCcy(material.getEstimatedPriceWithCcy());
        response.setUploadImageFileName(material.getUploadImageName());
        response.setIndigenousOrImported(material.getIndigenousOrImported());
        response.setApprovalStatus(material.getApprovalStatus().name());
        response.setComments(material.getComments());
        response.setCreatedBy(material.getCreatedBy());
        response.setUpdatedBy(material.getUpdatedBy());
        response.setCreatedDate(material.getCreatedDate());
        response.setUpdatedDate(material.getUpdatedDate());

        return response;
    }

   /* @Override
    public String performActionForMaterial(ApprovalAndRejectionRequestDTO request) {

        validateUserRoles(request.getActionBy());

        // Fetch vendor (Ensure it exists)
        MaterialMasterUtil material = materialMasterUtilRepository
                .findById(request.getRequestId())
                .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION,
                        "Material ID not found!"
                )));


        if ("APPROVED".equalsIgnoreCase(request.getAction())) {
            int actionBy =request.getActionBy();
            String remarks = request.getRemarks();
            return approveMaterial(material, actionBy, remarks);
        } else if ("REJECTED".equalsIgnoreCase(request.getAction())) {
            int actionBy =request.getActionBy();
            return rejectMaterial(material, request.getRemarks(),actionBy);
        } else if ("CHANGE REQUEST".equalsIgnoreCase(request.getAction())) {
            return changeRequestVendor(material, request.getRemarks(), request.getActionBy());

        } else {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.INVALID_ACTION,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Invalid action. Use 'APPROVED' or 'REJECTED'."
            ));
        }
    }


    */
   @Override
   public String performActionForMaterial(ApprovalAndRejectionRequestDTO request) {
       // Fetch roleId from UserRoleMaster
       Integer roleId = userRoleMasterRepository.findRoleIdByUserId(request.getActionBy())
               .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                       AppConstant.ERROR_TYPE_CODE_VALIDATION,
                       AppConstant.ERROR_TYPE_CODE_VALIDATION,
                       AppConstant.ERROR_TYPE_VALIDATION,
                       "Unauthorised User!"
               )));
       MaterialMasterUtil material = materialMasterUtilRepository
               .findById(request.getRequestId())
               .orElseThrow(() -> new InvalidInputException(new ErrorDetails(
                       AppConstant.ERROR_CODE_RESOURCE,
                       AppConstant.ERROR_TYPE_CODE_VALIDATION,
                       AppConstant.ERROR_TYPE_VALIDATION,
                       "Material ID not found!"
               )));

       int actionBy = request.getActionBy();
       String remarks = request.getRemarks();

       switch (request.getAction().toUpperCase()) {
           case "APPROVED":
               if (roleId == 11) {  // Store Purchase Officer
                   return approveMaterial(material, actionBy, remarks);
               } else if (roleId == 1 && material.getApprovalStatus() == MaterialMasterUtil.ApprovalStatus.CHANGE_REQUEST) {
                   material.setComments(remarks);
                   material.setApprovalStatus(MaterialMasterUtil.ApprovalStatus.AWAITING_APPROVAL);
                   materialMasterUtilRepository.save(material);
                   saveMaterialTracking(material.getMaterialCode(), "PENDING", "APPROVED", remarks, actionBy);


                   return "Material " + material.getMaterialCode() + " has been updated to AWAITING APPROVAL.";
               }
               break;

           case "REJECTED":
               return rejectMaterial(material, remarks, actionBy);

           case "CHANGE REQUEST":
               if (roleId == 11) {  // Store Purchase Officer
                   return changeRequestVendor(material, remarks, actionBy);
               }
               break;

           default:
               throw new InvalidInputException(new ErrorDetails(
                       AppConstant.INVALID_ACTION,
                       AppConstant.ERROR_TYPE_CODE_VALIDATION,
                       AppConstant.ERROR_TYPE_VALIDATION,
                       "Invalid action. Use 'APPROVED', 'REJECTED', or 'CHANGE REQUEST'."
               ));
       }

       return "Invalid operation for user role.";
   }




    private String changeRequestVendor(MaterialMasterUtil material, String remarks, Integer actionBy) {

        material.setApprovalStatus(MaterialMasterUtil.ApprovalStatus.CHANGE_REQUEST);
        material.setComments(remarks);

        materialMasterUtilRepository.save(material);
        saveMaterialTracking(material.getMaterialCode(), "PENDING","CHANGE_REQUEST", remarks, actionBy);

        return "Vendor " + material.getMaterialCode() + " Change Requested.";

    }

    private String rejectMaterial(MaterialMasterUtil material, String remarks, Integer actionBy) {

        material.setApprovalStatus(MaterialMasterUtil.ApprovalStatus.REJECTED);
        material.setComments(remarks);

        materialMasterUtilRepository.save(material);
        saveMaterialTracking(material.getMaterialCode(), "REJECTED","REJECTED", remarks, actionBy);

        return "Material " + material.getMaterialCode() + " has been REJECTED.";


    }

    private String approveMaterial(MaterialMasterUtil material,int actionBy, String remarks ) {

        MaterialMaster materialMaster= new MaterialMaster();
        materialMaster.setMaterialCode(material.getMaterialCode());
        materialMaster.setCategory(material.getCategory());
        materialMaster.setSubCategory(material.getSubCategory());
        materialMaster.setDescription(material.getDescription());
        materialMaster.setUom(material.getUom());
        materialMaster.setUnitPrice(material.getUnitPrice());
        materialMaster.setCurrency(material.getCurrency());
        materialMaster.setUploadImageName(material.getUploadImageName());
        materialMaster.setIndigenousOrImported(material.getIndigenousOrImported());
        materialMaster.setEstimatedPriceWithCcy(material.getEstimatedPriceWithCcy());
        materialMaster.setRemarks(material.getComments());
        materialMaster.setStatus("APPROVED");
        materialMaster.setCreatedBy(actionBy);
        materialMaster.setUpdatedBy(material.getUpdatedBy());
        materialMasterRepository.save(materialMaster);
        saveMaterialTracking(material.getMaterialCode(), "COMPLETED","APPROVED", remarks, actionBy);

        //approved materials are saved in material master before deleting in material master util
        materialMasterUtilRepository.deleteById(material.getMaterialCode());
        return "Material " + material.getMaterialCode() + " has been APPROVED and added the material data to material master, deleted from material master util.";

    }
/*
    private void saveMaterialTracking(String materialCode,String Status, String action, String remarks, int actionBy) {
            MaterialStatus materialStatus= new MaterialStatus();
            materialStatus.setMaterialCode(materialCode);
            materialStatus.setStatus(status);
            materialStatus.setAction(action);
            materialStatus.setComments(remarks);
            materialStatus.setCreatedBy(actionBy);


            materialStatusRepository.save(materialStatus);

    }

 */
private void saveMaterialTracking(String materialCode, String status, String action, String remarks, int actionBy) {
    MaterialStatus materialStatus = new MaterialStatus();
    materialStatus.setMaterialCode(materialCode);
    materialStatus.setStatus(status);
    materialStatus.setAction(action);
    materialStatus.setComments(remarks);
    materialStatus.setCreatedBy(actionBy);

    materialStatusRepository.save(materialStatus);
}



    private void validateUserRoles(Integer userId) {
        // Check if the user has role 11 (Store Purchase Officer) or role 1 (Indent Creator)
        boolean hasValidRole = userRoleMasterRepository.existsByRoleIdAndUserId(11, userId) ||
                userRoleMasterRepository.existsByRoleIdAndUserId(1, userId);

        if (!hasValidRole) {
            throw new InvalidInputException(new ErrorDetails(
                    AppConstant.UNAUTHORIZED_ACTION,
                    AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION,
                    "Unauthorized user"
            ));
        }
    }

    @Override
    public List<MaterialTransitionHistory> getMaterialStatusByCode(String materialCode) {
        List<MaterialStatus> materialStatusList = materialStatusRepository.findByMaterialCode(materialCode);

        return materialStatusList.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialMasterUtilResponseDto updateMaterialMasterUtil(String materialCode, MaterialMasterUtilRequestDto dto) {

        MaterialMasterUtil material = materialMasterUtilRepository.findById(materialCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Materail master not found for the provided Material code.")
                ));

        System.out.println("SubCa:"+ dto.getSubCategory());
        material.setCategory(dto.getCategory());
        material.setSubCategory(dto.getSubCategory());
        material.setDescription(dto.getDescription());
        material.setUom(dto.getUom());
        material.setUnitPrice(dto.getUnitPrice());
        material.setCurrency(dto.getCurrency());
        material.setEstimatedPriceWithCcy(dto.getEstimatedPriceWithCcy());
        material.setUploadImageName(dto.getUploadImageFileName());
        material.setIndigenousOrImported(dto.getIndigenousOrImported());
        material.setApprovalStatus(MaterialMasterUtil.ApprovalStatus.AWAITING_APPROVAL);
        material.setComments(null);
        material.setCreatedBy(dto.getCreatedBy());
        material.setUpdatedBy(dto.getUpdatedBy());

        materialMasterUtilRepository.save(material);

        Integer actionBy = dto.getCreatedBy();
        saveMaterialTracking(material.getMaterialCode(), "PENDING","UPDATED", null, actionBy );


        return mapToResponse(material);
    }

    @Override
    public MaterialMasterUtilResponseDto getMaterialMasterUtilById(String materialCode) {

        MaterialMasterUtil material= materialMasterUtilRepository.findById(materialCode)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "material master not found for the provided materialcode.")
                ));
        return mapToResponse(material);
    }

    private MaterialTransitionHistory mapToResponseDto(MaterialStatus status) {

        MaterialTransitionHistory history = new MaterialTransitionHistory();

        history.setId(status.getId());
        history.setMaterialCode(status.getMaterialCode());
        history.setAction(status.getAction());
        history.setStatus(status.getStatus());
        history.setComments(status.getComments());
        history.setCreatedBy(status.getCreatedBy());
        history.setUpdatedBy(status.getUpdatedBy());
        history.setCreatedDate(status.getCreatedDate());
        history.setUpdatedDate(status.getUpdatedDate());

        return history;
    }



}
