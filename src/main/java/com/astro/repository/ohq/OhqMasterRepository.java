package com.astro.repository.ohq;

import com.astro.entity.InventoryModule.OhqMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OhqMasterRepository extends JpaRepository<OhqMasterEntity, Integer> {
    List<OhqMasterEntity> findByAssetId(Integer assetId);
    List<OhqMasterEntity> findByLocatorId(Integer locatorId);
    Optional<OhqMasterEntity> findByAssetIdAndLocatorId(Integer assetId, Integer locatorId);

    @Query(value = """
        SELECT 
            ohq.asset_id,
            am.asset_desc,
            am.material_desc,
            am.uom_id,
            SUM(ohq.quantity) as total_quantity,
            ohq.book_value,
            ohq.depriciation_rate,
            ohq.unit_price,
            COALESCE(JSON_ARRAYAGG(
                JSON_OBJECT(
                    'locatorId', ohq.locator_id,
                    'locatorDesc', lm.locator_desc,
                    'quantity', ohq.quantity
                )
            ), '[]') as locator_details
        FROM ohq_master ohq
        JOIN asset_master am ON ohq.asset_id = am.asset_id
        JOIN locator_master lm ON ohq.locator_id = lm.locator_id
        WHERE ohq.quantity > 0
        GROUP BY ohq.asset_id, am.asset_desc, am.material_desc, am.uom_id, 
                ohq.book_value, ohq.depriciation_rate, ohq.unit_price
    """, nativeQuery = true)
    List<Object[]> getOhqReport();
}