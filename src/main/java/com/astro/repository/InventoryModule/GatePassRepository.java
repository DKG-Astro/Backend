package com.astro.repository.InventoryModule;

import com.astro.entity.InventoryModule.GatepassOutAndIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatePassRepository extends JpaRepository<GatepassOutAndIn,String> {
}
