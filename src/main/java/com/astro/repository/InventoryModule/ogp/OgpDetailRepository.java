package com.astro.repository.InventoryModule.ogp;

import com.astro.entity.InventoryModule.OgpDetailEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OgpDetailRepository extends JpaRepository<OgpDetailEntity, Integer> {
    boolean existsByIssueNoteIdAndAssetIdAndLocatorId(Integer issueNoteId, Integer assetId, Integer locatorId);
    List<OgpDetailEntity> findByOgpSubProcessId(Integer ogpSubProcessId);
    Optional<BigDecimal> findQuantityByOgpSubProcessIdAndAssetIdAndLocatorId(
        Integer ogpSubProcessId, Integer assetId, Integer locatorId);
}