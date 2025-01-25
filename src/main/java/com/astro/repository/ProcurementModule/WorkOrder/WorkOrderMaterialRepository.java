package com.astro.repository.ProcurementModule.WorkOrder;

import com.astro.entity.ProcurementModule.WorkOrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderMaterialRepository extends JpaRepository<WorkOrderMaterial,String> {

}
