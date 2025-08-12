package com.astro.repository.InventoryModule.GoodsTransfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.astro.entity.InventoryModule.GtMasterEntity;

import java.util.List;

@Repository
public interface GtMasterRepository extends JpaRepository<GtMasterEntity,Long> {
    List<GtMasterEntity> findByStatus(String status);
}

