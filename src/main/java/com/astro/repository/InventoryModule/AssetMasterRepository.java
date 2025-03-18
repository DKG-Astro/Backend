package com.astro.repository.InventoryModule;

import com.astro.entity.InventoryModule.AssetMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetMasterRepository extends JpaRepository<AssetMasterEntity, Integer> {
    
    List<AssetMasterEntity> findByMaterialCode(String materialCode);
    
    Optional<AssetMasterEntity> findBySerialNo(String serialNo);
    
    List<AssetMasterEntity> findByComponentId(Integer componentId);
    
    List<AssetMasterEntity> findByMaterialCodeAndUomId(String materialCode, String uomId);
    
    boolean existsBySerialNo(String serialNo);

    Optional<AssetMasterEntity> findByMaterialCodeAndMaterialDescAndMakeNoAndModelNoAndSerialNoAndUomId(String materialCode, String materialDesc, String makeNo, String modelNo, String serialNo, String uomId);
}