package com.astro.repository;

import com.astro.entity.MaterialMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialMasterRepository extends JpaRepository<MaterialMaster, String> {


    @Query("SELECT m.uom FROM MaterialMaster m WHERE m.materialCode = :materialCode")
    String findUomByMaterialCode(@Param("materialCode") String materialCode);
}
