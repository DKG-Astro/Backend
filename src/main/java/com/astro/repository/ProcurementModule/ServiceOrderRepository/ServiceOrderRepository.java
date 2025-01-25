package com.astro.repository.ProcurementModule.ServiceOrderRepository;

import com.astro.entity.ProcurementModule.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder,String> {
}
