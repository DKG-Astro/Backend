package com.astro.repository.IndentCreation;

import com.astro.entity.MaterialDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialDetailsRepository extends JpaRepository<MaterialDetails,Long> {
}
