package com.astro.repository.InventoryModule.igp;

import com.astro.entity.InventoryModule.IgpMasterEntity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IgpMasterRepository extends JpaRepository<IgpMasterEntity, Integer> {
    List<IgpMasterEntity> findByOgpSubProcessId(Integer ogpSubProcessId);
    
    @Query(value = """
            SELECT 
                im.igp_process_id,
                im.igp_sub_process_id,
                im.ogp_sub_process_id,
                im.igp_date,
                im.location_id,
                im.created_by,
                im.create_date,
                JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'detailId', id.detail_id,
                        'assetId', id.asset_id,
                        'assetDesc', am.asset_desc,
                        'materialDesc', am.material_desc,
                        'locatorId', id.locator_id,
                        'locatorDesc', lm.locator_desc,
                        'quantity', id.quantity,
                        'uomId', am.uom_id
                    )
                ) as igp_details
            FROM igp_master im
            JOIN igp_detail id ON im.igp_sub_process_id = id.igp_sub_process_id
            JOIN asset_master am ON id.asset_id = am.asset_id
            JOIN locator_master lm ON id.locator_id = lm.locator_id
            WHERE im.igp_date BETWEEN :startDate AND :endDate
            GROUP BY im.igp_sub_process_id
            ORDER BY im.igp_date DESC
            """, nativeQuery = true)
        List<Object[]> getIgpReport(LocalDateTime startDate, LocalDateTime endDate);
}