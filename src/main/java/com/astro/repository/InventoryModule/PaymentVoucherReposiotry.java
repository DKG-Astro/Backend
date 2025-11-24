package com.astro.repository.InventoryModule;

import com.astro.entity.PaymentVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentVoucherReposiotry extends JpaRepository<PaymentVoucher, Long> {

    PaymentVoucher findByGrnNumber(String grnNumber);

    //Optional<PaymentVoucher> findByGrnNumber(String grnNumber);
    Optional<PaymentVoucher> findTopByGrnNumberOrderByIdDesc(String grnNumber);

    boolean existsByGrnNumberAndPaymentVoucherType(String grnNumber, String paymentVoucherType);

    Optional<PaymentVoucher> findTopByServiceOrderDetailsOrderByIdDesc(String soId);

    List<PaymentVoucher> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    Optional<PaymentVoucher> findTopByPurchaseOrderIdOrderByIdDesc(String poId);

    @Query("SELECT COALESCE(SUM(p.paidAmount),0) FROM PaymentVoucher p WHERE p.purchaseOrderId = :poId AND p.paymentVoucherType = 'Advance'")
    BigDecimal getTotalAdvancePaid(@Param("poId") String poId);

    @Query("SELECT COALESCE(SUM(p.advanceAdjustedAmount),0) FROM PaymentVoucher p WHERE p.purchaseOrderId = :poId AND p.paymentVoucherType <> 'Full Payment'")
    BigDecimal getUsedAdvance(@Param("poId") String poId);


    @Query("SELECT COALESCE(SUM(p.paidAmount),0) FROM PaymentVoucher p WHERE p.grnNumber = :grn AND p.paymentVoucherType <> 'Full Payment'")
   BigDecimal getTotalPaidForGrn(@Param("grn") String grn);


    @Query("SELECT COALESCE(SUM(p.advanceAdjustedAmount),0) FROM PaymentVoucher p WHERE p.grnNumber = :grn")
    BigDecimal getAdvanceAppliedForGrn(@Param("grn") String grn);

    @Query("SELECT COALESCE(SUM(p.advanceAdjustedAmount), 0) " +
            "FROM PaymentVoucher p " +
            "WHERE p.purchaseOrderId = :poId")
    BigDecimal getTotalAdvanceAdjusted(@Param("poId") String poId);

  @Query("SELECT COALESCE(SUM(p.paidAmount), 0) " +
            "FROM PaymentVoucher p " +
            "WHERE p.grnNumber = :grnNumber " +
            "AND p.paymentVoucherType = 'Partial'")
    BigDecimal getTotalPartialPaidForGrn(@Param("grnNumber") String grnNumber);


    @Query("SELECT COALESCE(SUM(p.advanceAdjustedAmount), 0) " +
            "FROM PaymentVoucher p " +
            "WHERE p.grnNumber = :grnNumber " +
            "AND p.advanceAdjustedAmount IS NOT NULL")
    BigDecimal getTotalAdvanceAppliedForGrn(@Param("grnNumber") String grnNumber);

}
