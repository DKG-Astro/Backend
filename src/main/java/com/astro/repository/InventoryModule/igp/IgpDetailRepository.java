package com.astro.repository.InventoryModule.igp;

import com.astro.dto.workflow.InventoryModule.igp.IgpDetailReportDto;
import com.astro.entity.InventoryModule.IgpDetailEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IgpDetailRepository extends JpaRepository<IgpDetailEntity, Integer> {
    List<IgpDetailEntity> findByIgpSubProcessId(Integer IgpSubProcessId);
    @Query(value = """
        SELECT 
            d.detail_id,
            d.igp_sub_process_id,
            d.material_code,
            d.material_desc,
            NULL as asset_id,
            NULL as locator_id,
            d.uom_id,
            d.quantity,
            'MATERIAL' as type
        FROM igp_po_detail d
        UNION ALL
        SELECT 
            d.detail_id,
            d.igp_sub_process_id,
            NULL as material_code,
            NULL as material_desc,
            d.asset_id,
            d.locator_id,
            am.uom_id,
            d.quantity,
            'ASSET' as type
        FROM igp_detail d
        LEFT JOIN asset_master am ON d.asset_id = am.asset_id
        """, 
        nativeQuery = true)
    List<Object[]> findAllIgpDetails();
    @Query(value = """
        SELECT 
            detail_id as detailId,
            igp_sub_process_id as igpSubProcessId,
            material_code as materialCode,
            material_desc as materialDesc,
            NULL as assetId,
            NULL as locatorId,
            uom_id as uomId,
            quantity,
            'MATERIAL' as type
        FROM igp_po_detail 
        WHERE igp_sub_process_id = :igpSubProcessId
        UNION ALL
        SELECT 
            detail_id,
            igp_sub_process_id,
            NULL as materialCode,
            NULL as materialDesc,
            asset_id,
            locator_id,
            (SELECT uom_id FROM asset_master WHERE asset_id = id.asset_id) as uomId,
            quantity,
            'ASSET' as type
        FROM igp_detail id
        WHERE igp_sub_process_id = :igpSubProcessId
        """, nativeQuery = true)
    List<IgpDetailReportDto> findAllDetailsByIgpSubProcessId(@Param("igpSubProcessId") Integer igpSubProcessId);
}