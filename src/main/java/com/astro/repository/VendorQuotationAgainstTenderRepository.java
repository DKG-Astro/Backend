package com.astro.repository;

import com.astro.entity.VendorQuotationAgainstTender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorQuotationAgainstTenderRepository extends JpaRepository<VendorQuotationAgainstTender,Long> {

    List<VendorQuotationAgainstTender> findByTenderId(String tenderId);
}
