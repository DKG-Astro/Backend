package com.astro.repository.InventoryModule.isn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.astro.entity.InventoryModule.IsnMasterEntity;

@Repository
public interface IssueNoteMasterRepository extends JpaRepository<IsnMasterEntity, Integer> {
    // Add custom query methods if needed
}