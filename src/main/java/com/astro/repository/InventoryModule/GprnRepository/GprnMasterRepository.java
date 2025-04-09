package com.astro.repository.InventoryModule.GprnRepository;

import com.astro.entity.InventoryModule.GprnMasterEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GprnMasterRepository extends JpaRepository<GprnMasterEntity,Integer> {

    @Query(value = """
    SELECT po.po_id
    FROM purchase_order po
    LEFT JOIN purchase_order_attributes poa ON po.po_id = poa.po_id
    LEFT JOIN (
        SELECT gm.po_id, gmd.material_code, SUM(gmd.received_quantity) AS total_received
        FROM gprn_master gm
        JOIN gprn_material_detail gmd ON gm.sub_process_id = gmd.sub_process_id
        GROUP BY gm.po_id, gmd.material_code
    ) gprn_data ON po.po_id = gprn_data.po_id AND poa.material_code = gprn_data.material_code
    GROUP BY po.po_id
    HAVING 
        COUNT(DISTINCT poa.material_code) > COUNT(DISTINCT gprn_data.material_code)
        OR 
        SUM(CASE 
                WHEN gprn_data.total_received IS NULL THEN 1 
                WHEN gprn_data.total_received < poa.quantity THEN 1 
                ELSE 0 
            END) > 0
    """, nativeQuery = true)
    List<String> findPoIdsWithIncompleteGprn();
}
