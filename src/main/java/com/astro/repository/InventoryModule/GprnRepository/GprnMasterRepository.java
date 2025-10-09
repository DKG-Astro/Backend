package com.astro.repository.InventoryModule.GprnRepository;

import com.astro.dto.workflow.InventoryModule.GprnPoVendorDto;
import com.astro.entity.InventoryModule.GprnMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    GprnMasterEntity findByProcessId(String gprnProcessId);

    GprnMasterEntity findBySubProcessId(Integer gprnSubProcessId);

    @Query("SELECT DISTINCT g FROM GprnMasterEntity g " +
            "LEFT JOIN GprnMaterialDtlEntity m ON g.subProcessId = m.subProcessId " +
            "LEFT JOIN GiMasterEntity gi ON g.subProcessId = gi.gprnSubProcessId " +
            "WHERE gi.gprnSubProcessId IS NULL")
    List<GprnMasterEntity> findPendingGprnsWithMaterial();

    @Query("SELECT new com.astro.dto.workflow.InventoryModule.GprnPoVendorDto(g.poId, g.vendorId) " +
            "FROM GprnMasterEntity g WHERE g.subProcessId = :subProcessId")
    GprnPoVendorDto findPoIdAndVendorIdBySubProcessId(@Param("subProcessId") Integer subProcessId);
}
