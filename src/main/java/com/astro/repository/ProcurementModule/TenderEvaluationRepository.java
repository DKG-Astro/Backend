package com.astro.repository.ProcurementModule;

import com.astro.entity.ProcurementModule.TenderEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderEvaluationRepository extends JpaRepository<TenderEvaluation,String> {



}
