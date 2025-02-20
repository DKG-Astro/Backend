package com.astro.repository.ProcurementModule;

import com.astro.entity.ProcurementModule.IndentId;
import com.astro.entity.ProcurementModule.TenderRequest;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndentIdRepository extends JpaRepository<IndentId,String> {
    @Query("SELECT i.indentId FROM IndentId i WHERE i.tenderRequest.tenderId = :tenderId")
 List<String> findTenderWithIndent(@Param("tenderId") String tenderId);

}
