package com.astro.repository.ProcurementModule.ServiceOrderRepository;

import com.astro.entity.ProcurementModule.ServiceOrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderMaterialRepository extends JpaRepository<ServiceOrderMaterial,Long> {
}
