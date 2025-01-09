package com.astro.repository.IndentCreation;

import com.astro.entity.IndentCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndentCreationRepository extends JpaRepository<IndentCreation,Long> {
}
