package com.astro.repository.InventoryModule;

import com.astro.entity.InventoryModule.AssetMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetMasterRepository extends JpaRepository<AssetMasterEntity, Integer> {
    
    List<AssetMasterEntity> findByMaterialCode(String materialCode);
    
    Optional<AssetMasterEntity> findBySerialNo(String serialNo);
    
    List<AssetMasterEntity> findByComponentId(Integer componentId);
    
    List<AssetMasterEntity> findByMaterialCodeAndUomId(String materialCode, String uomId);
    
    boolean existsBySerialNo(String serialNo);

    Optional<AssetMasterEntity> findByMaterialCodeAndMaterialDescAndMakeNoAndModelNoAndSerialNoAndUomId(String materialCode, String materialDesc, String makeNo, String modelNo, String serialNo, String uomId);
    boolean existsByMaterialCodeAndMaterialDescAndMakeNoAndModelNoAndSerialNoAndUomId(
            @Param("materialCode") String materialCode,
            @Param("materialDesc") String materialDesc,
            @Param("makeNo") String makeNo,
            @Param("modelNo") String modelNo,
            @Param("serialNo") String serialNo,
            @Param("uomId") String uomId
    );
    
    @Query(value = """
        SELECT 
            asset_id, material_code, material_desc, asset_desc, make_no, 
            serial_no, model_no, init_quantity, unit_price, uom_id,
            depriciation_rate, end_of_life, stock_levels, condition_of_goods,
            shelf_life, component_name, component_id, create_date, created_by,
            updated_date, updated_by
        FROM asset_master
        """, nativeQuery = true)
    List<Object[]> getAssetReport();
}