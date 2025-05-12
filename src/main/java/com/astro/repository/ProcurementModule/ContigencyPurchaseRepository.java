package com.astro.repository.ProcurementModule;

import com.astro.entity.ProcurementModule.ContigencyPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.hql.internal.antlr.HqlTokenTypes.AS;
import static org.hibernate.hql.internal.antlr.HqlTokenTypes.WHERE;
import static org.hibernate.loader.Loader.SELECT;
import static org.springframework.data.repository.query.parser.Part.Type.BETWEEN;

@Repository
public interface ContigencyPurchaseRepository extends JpaRepository<ContigencyPurchase,String> {

   /* @Query(value = """
        SELECT
            cp.contigency_id AS Id,
            cp.material_description AS Material,
            md.category AS Material_category,
            md.sub_category AS Material_sub_category,
            cp.remarks_for_purchase AS End_user,
            cp.amount_to_be_paid AS Value,
            cp.vendors_name AS Paid_to,
            cp.vendors_name AS Vendor_name,
            cp.project_name AS Project
        FROM
            contigency_purchase cp
        JOIN
            material_master md
        ON
            cp.material_code = md.material_code
        WHERE
            cp.Date BETWEEN :startDate AND :endDate
        ORDER BY
            cp.Date
    """, nativeQuery = true)*/
   @Query(value = """
         
                 SELECT
                     cp.contigency_id AS id,
                     GROUP_CONCAT(cm.material_description SEPARATOR ', ') AS material,
                     GROUP_CONCAT(cm.material_category SEPARATOR ', ') AS materialCategory,
                     GROUP_CONCAT(cm.material_sub_category SEPARATOR ', ') AS materialSubCategory,
                     cp.remarks_for_purchase AS endUser,
                     SUM(cm.total_price) AS value,
                     cp.vendors_name AS paidTo,
                     cp.vendors_name AS vendorName,
                     cp.project_name AS project
                 FROM
                     contigency_purchase cp
                 JOIN
                     cp_materials cm ON cp.contigency_id = cm.contigency_id
                 WHERE
                     cp.Date BETWEEN :startDate AND :endDate
                 GROUP BY
                     cp.contigency_id, cp.remarks_for_purchase, cp.vendors_name, cp.project_name
                 ORDER BY
                     cp.Date
        """, nativeQuery = true)
        List<Object[]> findContigencyPurchaseReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT MAX(i.cpNumber) FROM ContigencyPurchase i")
    Integer findMaxCpNumber();

    // ContigencyPurchase findByContigencyId(String contigencyId);

   // ContigencyPurchase getByCpId(String contigencyId);
}
