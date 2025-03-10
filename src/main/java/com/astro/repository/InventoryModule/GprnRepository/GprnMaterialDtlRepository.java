package com.astro.repository.InventoryModule.GprnRepository;

import com.astro.entity.InventoryModule.GprnMaterialDtlEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GprnMaterialDtlRepository extends JpaRepository<GprnMaterialDtlEntity, Integer> {
    List<GprnMaterialDtlEntity> findByPoIdAndMaterialCode(String poId, String materialCode);

    List<GprnMaterialDtlEntity> findBySubProcessId(Integer subProcessId);
}
