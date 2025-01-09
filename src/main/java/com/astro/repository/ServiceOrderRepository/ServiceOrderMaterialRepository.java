package com.astro.repository.ServiceOrderRepository;

import com.astro.entity.ServiceOrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderMaterialRepository extends JpaRepository<ServiceOrderMaterial,Long> {
}
