package com.astro.repository.InventoryModule;

import com.astro.entity.InventoryModule.GoodsInspectionConsumableDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsInspectionConsumableDetailRepository extends JpaRepository<GoodsInspectionConsumableDetailEntity, Integer> {
    
    List<GoodsInspectionConsumableDetailEntity> findByInspectionSubProcessId(Integer inspectionSubProcessId);
    
    List<GoodsInspectionConsumableDetailEntity> findByGprnSubProcessId(Integer gprnSubProcessId);
    
    List<GoodsInspectionConsumableDetailEntity> findByMaterialCode(String materialCode);

    Optional<GoodsInspectionConsumableDetailEntity> findByGprnSubProcessIdAndMaterialCode(Integer subProcessId, String materialCode);

    List<GoodsInspectionConsumableDetailEntity> findByInspectionSubProcessIdAndMaterialCode(Integer inspectionSubProcessId, String materialCode);
}