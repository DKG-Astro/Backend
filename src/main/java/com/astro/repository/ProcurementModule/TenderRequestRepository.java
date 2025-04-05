package com.astro.repository.ProcurementModule;

import com.astro.entity.ProcurementModule.TenderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenderRequestRepository extends JpaRepository<TenderRequest, String> {
    Optional<TenderRequest> findByTenderId(String tenderId);

    @Query("SELECT MAX(t.tenderNumber) FROM TenderRequest t")
    Integer findMaxTenderNumber();

    //  TenderRequest getByTenderId(String tenderId);
}
