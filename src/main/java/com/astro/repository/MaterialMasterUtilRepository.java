package com.astro.repository;

import com.astro.dto.workflow.MaterialMasterUtilResponseDto;
import com.astro.entity.MaterialMasterUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialMasterUtilRepository extends JpaRepository<MaterialMasterUtil, String> {


    List<MaterialMasterUtil> findByApprovalStatus(MaterialMasterUtil.ApprovalStatus approvalStatus);
}
