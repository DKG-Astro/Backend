package com.astro.repository.InventoryModule.igp;

import com.astro.entity.InventoryModule.IgpMasterEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IgpMasterRepository extends JpaRepository<IgpMasterEntity, Integer> {
    List<IgpMasterEntity> findByOgpSubProcessId(Integer ogpSubProcessId);
}