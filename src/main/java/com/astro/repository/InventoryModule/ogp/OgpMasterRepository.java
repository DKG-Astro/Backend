package com.astro.repository.InventoryModule.ogp;

import com.astro.entity.InventoryModule.OgpMasterEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OgpMasterRepository extends JpaRepository<OgpMasterEntity, Integer> {
    boolean existsByIssueNoteId(Integer issueNoteId);
    
    Optional<OgpMasterEntity> findById(Integer ogpSubProcessId);
}
