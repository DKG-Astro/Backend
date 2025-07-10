package com.astro.repository;

import com.astro.entity.VendorQuotationAgainstTender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorQuotationAgainstTenderRepository extends JpaRepository<VendorQuotationAgainstTender,Long> {

    List<VendorQuotationAgainstTender> findByTenderId(String tenderId);
  @Query("SELECT v.vendorId FROM VendorQuotationAgainstTender v WHERE v.tenderId = :tenderId")
  List<String> findVendorIdsByTenderId(@Param("tenderId") String tenderId);

   // Optional<VendorQuotationAgainstTender> findByTenderIdAndVendorId(String tenderId, String vendorId);
    List<VendorQuotationAgainstTender> findAllByTenderIdAndVendorId(String tenderId, String vendorId);

   // List<VendorQuotationAgainstTender> findLatestNonRejectedQuotations(String tenderId);
   @Query("SELECT v FROM VendorQuotationAgainstTender v WHERE v.tenderId = :tenderId AND v.isLatest = true AND v.status <> 'Rejected'")
   List<VendorQuotationAgainstTender> findLatestNonRejectedQuotations(@Param("tenderId") String tenderId);
    List<VendorQuotationAgainstTender> findByTenderIdAndVendorId(String tenderId, String vendorId);


  Optional<VendorQuotationAgainstTender> findTopByTenderIdAndVendorIdAndIsLatestTrueOrderByVersionDesc(String tenderId, String vendorId);

    List<VendorQuotationAgainstTender> findAllByTenderIdAndVendorIdOrderByCreatedDateDesc(String tenderId, String vendorId);
}
