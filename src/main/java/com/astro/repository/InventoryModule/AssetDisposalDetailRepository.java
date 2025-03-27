package com.astro.repository.InventoryModule;

import org.springframework.data.jpa.repository.JpaRepository;
import com.astro.entity.InventoryModule.AssetDisposalDetailEntity;
import java.util.List;

public interface AssetDisposalDetailRepository extends JpaRepository<AssetDisposalDetailEntity, Integer> {
    List<AssetDisposalDetailEntity> findByDisposalId(Integer disposalId);
}