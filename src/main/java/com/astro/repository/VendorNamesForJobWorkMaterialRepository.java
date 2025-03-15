package com.astro.repository;

import com.astro.entity.VendorNamesForJobWorkMaterial;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorNamesForJobWorkMaterialRepository extends JpaRepository<VendorNamesForJobWorkMaterial,Long> {

    List<VendorNamesForJobWorkMaterial> findByJobCode(String jobCode);

    List<VendorNamesForJobWorkMaterial> findByMaterialCode(String materialCode);

    List<VendorNamesForJobWorkMaterial> findByWorkCode(String workCode);
}
