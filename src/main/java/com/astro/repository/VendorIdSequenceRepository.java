package com.astro.repository;

import com.astro.entity.VendorIdSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorIdSequenceRepository extends JpaRepository<VendorIdSequence,Long> {

    @Query("SELECT MAX(i.vendorId) FROM VendorIdSequence i")
    Integer findMaxVendorId();
}
