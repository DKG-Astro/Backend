package com.astro.repository;

import com.astro.entity.TransitionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitionMasterRepository extends JpaRepository<TransitionMaster, Integer> {
}
