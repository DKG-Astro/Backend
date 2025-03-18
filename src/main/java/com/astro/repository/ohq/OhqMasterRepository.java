package com.astro.repository.ohq;

import com.astro.entity.InventoryModule.OhqMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OhqMasterRepository extends JpaRepository<OhqMasterEntity, Integer> {
    List<OhqMasterEntity> findByAssetId(Integer assetId);
    List<OhqMasterEntity> findByLocatorId(Integer locatorId);

    Optional<OhqMasterEntity> findByAssetIdAndLocatorId(Integer assetId, Integer locatorId);
}