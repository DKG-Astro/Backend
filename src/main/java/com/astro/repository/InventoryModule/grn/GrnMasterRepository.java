package com.astro.repository.InventoryModule.grn;

import com.astro.entity.InventoryModule.GiWorkflowStatus;
import com.astro.entity.InventoryModule.GrnMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrnMasterRepository extends JpaRepository<GrnMasterEntity, Integer> {
    Optional<GrnMasterEntity> findByGrnProcessId(String grnProcessId);
    List<GrnMasterEntity> findByStatusIn(List<String> statuses);

    Optional<GrnMasterEntity> findByGrnSubProcessId(Integer subProcessId);


}
