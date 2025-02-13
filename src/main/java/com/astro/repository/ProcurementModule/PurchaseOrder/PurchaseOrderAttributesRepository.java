package com.astro.repository.ProcurementModule.PurchaseOrder;

import com.astro.entity.ProcurementModule.PurchaseOrderAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderAttributesRepository extends JpaRepository<PurchaseOrderAttributes, String> {
    Optional<PurchaseOrderAttributes> findByMaterialCode(String materialCode);
}
