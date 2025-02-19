package com.astro.repository.ProcurementModule.IndentCreation;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentReportDetailsDTO;
import com.astro.entity.ProcurementModule.IndentCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IndentCreationRepository extends JpaRepository<IndentCreation,String> {

    @Query(value = """
                   SELECT
                       ic.indent_id,
                       ic.created_date AS "Approved date",
                       wt.current_Role AS "Assigned to",
                       tender_request.tender_id AS "Tender Request",
                       tender_request.mode_of_procurement,
                       po.po_id AS "Corresponding PO/ SO",
                       po.status AS "Status of PO/ SO",
                       po.created_date AS "Submitted date",
                       po.approval_pending_with AS "Pending approval with & pending from",
                       po.approval_date AS "PO/ SO approved date",
                       mm.description AS "Material",
                       mm.category AS "Material category",
                       mm.sub_category AS "Material sub-category",
                       po.vendor_name AS "Vendor name",
                       ic.indentor_name AS "Indentor name",
                       md.total_price AS "Value of indent",
                       po.total_value_of_po AS "Value of PO",
                       pm.project_name_description AS "Project",
                       gd.grin_no,
                       id.invoice_no,
                       gsd.giss_no,
                       po.pending_payment_value AS "Value pending to be paid",
                       wt.status AS "Current stage of the indent",
                       CASE WHEN wt.action = 'Rejected' THEN 'Yes' ELSE 'No' END AS "Short-closed and cancelled through amendment",
                       wt.remark AS "Reason for short-closure & cancellation"
                   FROM
                       indent_creation ic
                   LEFT JOIN
                       material_details md ON ic.indent_id = md.indent_creation_id
                   LEFT JOIN
                       material_master mm ON md.material_code = mm.material_code
                   LEFT JOIN
                       purchase_order po ON ic.indent_id = po.indent_id
                   LEFT JOIN tender_request ON ic.indent_id = tender_request.indent_id
                   LEFT JOIN
                       project_master pm ON ic.project_name = pm.project_code
                   LEFT JOIN
                       workflow_transaction wt ON ic.indent_id = wt.request_id
                   WHERE
                       ic.created_date BETWEEN :startDate AND :endDate
                   """, nativeQuery = true)
    List<IndentReportDetailsDTO> fetchIndentReportDetails(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
