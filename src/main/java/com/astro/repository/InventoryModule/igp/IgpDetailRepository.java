package com.astro.repository.InventoryModule.igp;

import com.astro.entity.InventoryModule.IgpDetailEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IgpDetailRepository extends JpaRepository<IgpDetailEntity, Integer> {
    List<IgpDetailEntity> findByIgpSubProcessId(Integer IgpSubProcessId);
}