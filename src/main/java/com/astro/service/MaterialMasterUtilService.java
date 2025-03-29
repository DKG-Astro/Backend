package com.astro.service;

import com.astro.dto.workflow.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialMasterUtilService{

    public MaterialMasterUtilResponseDto createMaterial(MaterialMasterUtilRequestDto dto);


    public List<MaterialMasterUtilResponseDto> getAllAwaitingApprovalMaterials();

    public List<MaterialMasterUtilResponseDto> getAllChangeRequestMaterials();

    public String performActionForMaterial(ApprovalAndRejectionRequestDTO request);

    List<MaterialTransitionHistory> getMaterialStatusByCode(String materialCode);


}
