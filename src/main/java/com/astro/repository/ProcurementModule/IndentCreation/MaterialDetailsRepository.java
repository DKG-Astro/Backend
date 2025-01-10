package com.astro.repository.ProcurementModule.IndentCreation;

import com.astro.entity.ProcurementModule.MaterialDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialDetailsRepository extends JpaRepository<MaterialDetails,Long> {
}
