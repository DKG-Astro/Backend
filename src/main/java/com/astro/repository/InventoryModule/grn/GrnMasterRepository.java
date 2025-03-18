package com.astro.repository.InventoryModule.grn;

import com.astro.entity.InventoryModule.GrnMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrnMasterRepository extends JpaRepository<GrnMasterEntity, Integer> {
    Optional<GrnMasterEntity> findByGrnProcessId(String grnProcessId);
}
