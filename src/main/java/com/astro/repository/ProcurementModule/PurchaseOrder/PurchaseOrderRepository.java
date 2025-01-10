package com.astro.repository.ProcurementModule.PurchaseOrder;

import com.astro.entity.ProcurementModule.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {


}
