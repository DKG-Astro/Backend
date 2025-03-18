package com.astro.repository.InventoryModule.GiRepository;

import com.astro.entity.InventoryModule.GiMaterialDtlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiMaterialDtlRepository extends JpaRepository<GiMaterialDtlEntity, Integer> {
    
    List<GiMaterialDtlEntity> findByInspectionSubProcessId(Integer inspectionSubProcessId);
    
    List<GiMaterialDtlEntity> findByMaterialCode(String materialCode);

    Optional<GiMaterialDtlEntity> findByGprnSubProcessIdAndMaterialCode(Integer subProcessId, String materialCode);

}