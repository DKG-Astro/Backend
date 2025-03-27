package com.astro.repository.InventoryModule;

import org.springframework.data.jpa.repository.JpaRepository;
import com.astro.entity.InventoryModule.AssetDisposalMasterEntity;

public interface AssetDisposalMasterRepository extends JpaRepository<AssetDisposalMasterEntity, Integer> {
}