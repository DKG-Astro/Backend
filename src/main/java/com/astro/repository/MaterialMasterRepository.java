package com.astro.repository;

import com.astro.entity.MaterialMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialMasterRepository extends JpaRepository<MaterialMaster, String> {


}
