package com.astro.repository;

import com.astro.entity.SubWorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubWorkflowTransitionRepository extends JpaRepository<SubWorkflowTransition, Integer> {
}
