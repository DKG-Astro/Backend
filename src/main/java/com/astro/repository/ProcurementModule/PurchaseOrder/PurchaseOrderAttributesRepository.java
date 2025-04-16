package com.astro.repository.ProcurementModule.PurchaseOrder;

import com.astro.entity.ProcurementModule.PurchaseOrderAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface PurchaseOrderAttributesRepository extends JpaRepository<PurchaseOrderAttributes, Long> {

    @Query("SELECT poa.quantity FROM PurchaseOrderAttributes poa WHERE poa.poId = :poId AND poa.materialCode = :materialCode")
Optional<BigDecimal> findQuantityByPoIdAndMaterialCode(@Param("poId") String poId, @Param("materialCode") String materialCode);

}
