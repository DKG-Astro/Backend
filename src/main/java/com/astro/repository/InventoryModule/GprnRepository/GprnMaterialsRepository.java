package com.astro.repository.InventoryModule.GprnRepository;

import com.astro.entity.InventoryModule.GprnMaterials;
import com.astro.entity.InventoryModule.GprnMaterialsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GprnMaterialsRepository extends JpaRepository<GprnMaterials,Long> {

}
