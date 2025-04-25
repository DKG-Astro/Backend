package com.astro.repository.InventoryModule;

import com.astro.entity.InventoryModule.OhqMasterConsumableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OhqMasterConsumableRepository extends JpaRepository<OhqMasterConsumableEntity, Integer> {
    
    List<OhqMasterConsumableEntity> findByMaterialCode(String materialCode);
    
    List<OhqMasterConsumableEntity> findByLocatorId(Integer locatorId);
    
    Optional<OhqMasterConsumableEntity> findByMaterialCodeAndLocatorId(String materialCode, Integer locatorId);
}