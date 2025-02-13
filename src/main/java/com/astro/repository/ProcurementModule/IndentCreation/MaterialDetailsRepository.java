package com.astro.repository.ProcurementModule.IndentCreation;

import com.astro.entity.ProcurementModule.MaterialDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialDetailsRepository extends JpaRepository<MaterialDetails,String> {
    Optional<MaterialDetails> findByMaterialCode(String materialCode);
}
