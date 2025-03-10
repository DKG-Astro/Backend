package com.astro.repository.InventoryModule.GprnRepository;

import com.astro.entity.InventoryModule.GprnMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GprnMasterRepository extends JpaRepository<GprnMasterEntity,Integer> {
}
