package com.astro.repository.InventoryModule;

import com.astro.entity.InventoryModule.Gprn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GprnRepository extends JpaRepository<Gprn, Long> {
}
