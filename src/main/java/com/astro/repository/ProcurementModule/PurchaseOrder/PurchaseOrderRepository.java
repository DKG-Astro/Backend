package com.astro.repository.ProcurementModule.PurchaseOrder;

import com.astro.dto.workflow.ProcurementDtos.ProcurementActivityReportResponse;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {

    @Query(value = """
          select
          po.po_id AS orderId,
                    tr.mode_of_procurement AS modeOfProcurement,
                    '' AS underAMC,
                    '' AS amcFor,
                    '' AS endUser,
                     CAST(NULL AS UNSIGNED) AS noOfParticipants,
                    po.total_value_of_po AS value,
                    po.consignes_address AS location,
                    po.vendor_name AS vendorName,
                    '' AS previouslyRenewedAMCs,
                    '' AS categoryOfSecurity,
                    '' AS validityOfSecurity
                FROM purchase_order AS po
                JOIN tender_request AS tr ON po.tender_id = tr.tender_id
        WHERE po.created_date BETWEEN :startDate AND :endDate
        ORDER BY po.created_date
    """, nativeQuery = true)
    List<Object[]> getVendorContractDetails(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
            SELECT
            po.po_id AS 'Order Id',
            CASE
            WHEN vm.registered_platform = 'Yes' THEN 'GeM'
            WHEN vm.registered_platform = 'No' THEN 'Non-GeM'
            ELSE 'Unknown'
            END AS 'GeM / Non-GeM',
            '' As 'Indentor',
            po.total_value_of_po AS 'Value',
            '' AS 'Description of goods/services',
            vm.vendor_name AS 'Vendor Name'
            FROM purchase_order AS po
            LEFT JOIN vendor_master AS vm ON po.vendor_name = vm.vendor_name
            WHERE po.created_date BETWEEN :startDate AND :endDate
        ORDER BY po.created_date
             """, nativeQuery = true)
    List<Object[]> getProcurementActivityReport(@Param("startDate") LocalDate startDate,
                                                                         @Param("endDate") LocalDate endDate);

    PurchaseOrder findByPoId(String poId);

    List<PurchaseOrder> findByVendorId(String vendorId);

    // PurchaseOrder getByPoId(String poId);
}
