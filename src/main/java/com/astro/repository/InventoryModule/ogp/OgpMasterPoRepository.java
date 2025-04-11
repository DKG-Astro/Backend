package com.astro.repository.InventoryModule.ogp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.astro.entity.InventoryModule.OgpMasterPoEntity;

@Repository
public interface OgpMasterPoRepository extends JpaRepository<OgpMasterPoEntity, Integer> {
    boolean existsByPoId(String poId);
    
}
