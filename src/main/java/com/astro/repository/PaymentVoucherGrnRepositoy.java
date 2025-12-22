package com.astro.repository;

import com.astro.entity.PaymentVoucherGrn;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentVoucherGrnRepositoy extends JpaRepository<PaymentVoucherGrn, Long> {
    @Query("""
        select coalesce(sum(p.paidAmount), 0)
        from PaymentVoucherGrn p
        where p.grnNumber in :grnNumbers
    """)
    BigDecimal getTotalPaidForGrns(@Param("grnNumbers") List<String> grnNumbers);

    @Query("""
        select coalesce(sum(p.advanceAdjusted), 0)
        from PaymentVoucherGrn p
        where p.grnNumber in :grnNumbers
    """)
    BigDecimal getTotalAdvanceAdjustedForGrns(@Param("grnNumbers") List<String> grnNumbers);


    boolean existsByGrnNumberAndPaymentVouchertype(
            String grnNumber,
            String paymentVouchertype
    );

}
