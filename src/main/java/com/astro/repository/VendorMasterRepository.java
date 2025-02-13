package com.astro.repository;

import com.astro.entity.VendorMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendorMasterRepository extends JpaRepository<VendorMaster, String> {
    @Query(value = """
    SELECT
    order_table.po_id AS 'Order Id',  -- Changed 'order_id' to 'po_id'
    procurement.mode AS 'Mode of Procurement',
    NULL AS 'Under AMC',
    NULL AS 'AMC Expiry Date',
    NULL AS 'AMC for',
    NULL AS 'End User',
    NULL AS 'No. of Participants',
    order_table.total_value_of_po AS 'Value',  -- Changed 'value' to 'total_value_of_po'
    location_master.location_name AS 'Location',
    vendor_master.vendor_name AS 'Vendor Name',
    NULL AS 'Previously Renewed AMCs for the Same Orders',
    security.category AS 'Category of Security',
    security.validity AS 'Validity of Security'
    FROM purchase_order AS order_table
    LEFT JOIN transition_master AS procurement ON order_table.procurement_id = procurement.transition_id
    LEFT JOIN location_master ON order_table.location_id = location_master.location_id
    LEFT JOIN vendor_master ON order_table.vendor_id = vendor_master.vendor_id
    LEFT JOIN workflow_transition AS security ON order_table.order_id = security.transition_id
    WHERE order_table.created_date BETWEEN :startDate AND :endDate
    ORDER BY
    order_table.created_date;
     """, nativeQuery = true)
    List<Object[]> findVendorContracts(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);


}
