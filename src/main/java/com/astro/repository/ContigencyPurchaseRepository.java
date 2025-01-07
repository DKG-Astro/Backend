package com.astro.repository;

import com.astro.entity.ContigencyPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContigencyPurchaseRepository extends JpaRepository<ContigencyPurchase,Long> {



}
