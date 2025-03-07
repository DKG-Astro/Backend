package com.astro.repository.InventoryModule.GprnRepository;

import com.astro.entity.InventoryModule.GprnMaterialDetails;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
@Registered
public interface GprnMaterialDetailsRepository extends JpaRepository<GprnMaterialDetails, Integer> {
}
