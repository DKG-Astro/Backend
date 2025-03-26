package com.astro.repository.InventoryModule.isn;

import com.astro.entity.InventoryModule.IssueRegisterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.astro.entity.InventoryModule.IsnMasterEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IssueNoteMasterRepository extends JpaRepository<IsnMasterEntity, Integer> {
    // Add custom query methods if needed

    @Query(value = """
        SELECT 
            inm.issue_note_id AS issueID,
            inm.issue_date AS dateOfIssue,
            am.material_desc AS itemDescription,
            mm.category AS category,
            mm.sub_category AS subCategory,
            ind.quantity AS quantityIssued,
            am.uom_id AS unitOfMeasure,
            inm.location_id AS location,
            CAST(NULL AS CHAR)  AS issuedTo,
            CAST(NULL AS CHAR)  AS issuedBy,
            CAST(NULL AS CHAR)  AS purpose
        FROM issue_note_master inm
        JOIN issue_note_detail ind ON inm.issue_note_id = ind.issue_note_id
        JOIN asset_master am ON ind.asset_id = am.asset_id
        JOIN material_master mm ON am.material_code = mm.material_code
        WHERE inm.issue_date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    List<Object[]> getIssueRegisterData(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}