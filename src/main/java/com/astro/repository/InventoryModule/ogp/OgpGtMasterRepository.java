package com.astro.repository.InventoryModule.ogp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.astro.entity.InventoryModule.OgpGtMasterEntity;
import java.util.List;


@Repository
public interface OgpGtMasterRepository extends JpaRepository<OgpGtMasterEntity,Long> {
    List<OgpGtMasterEntity> findByStatus(String status);
}
