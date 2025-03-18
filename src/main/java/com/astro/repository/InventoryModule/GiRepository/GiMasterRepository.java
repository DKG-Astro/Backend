package com.astro.repository.InventoryModule.GiRepository;

import com.astro.entity.InventoryModule.GiMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiMasterRepository extends JpaRepository<GiMasterEntity, Integer> {
    
    List<GiMasterEntity> findByGprnProcessId(String gprnProcessId);
    
    Optional<GiMasterEntity> findByGprnProcessIdAndGprnSubProcessId(String gprnProcessId, Integer gprnSubProcessId);
}