package com.astro.repository.ProcurementModule;

import com.astro.entity.ProcurementModule.ContigencyPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContigencyPurchaseRepository extends JpaRepository<ContigencyPurchase,Long> {



}
