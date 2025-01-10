package com.astro.repository.ProcurementModule;

import com.astro.entity.ProcurementModule.TenderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderRequestRepository extends JpaRepository<TenderRequest, Long> {
}
