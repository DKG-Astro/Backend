package com.astro.repository.ProcurementModule.IndentCreation;

import com.astro.entity.ProcurementModule.IndentCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndentCreationRepository extends JpaRepository<IndentCreation,String> {
/*
    @Query(value = """  SELECT
    i.indent_id AS "Indent Id",
    i.approved_date AS "Approved Date",
    e.employee_name AS "Assigned To",
    i.tender_request AS "Tender Request",
    i.mode_of_tendering AS "Mode of Tendering",
    po.po_no AS "Corresponding PO/ SO",
    po.status AS "Status of PO/ SO",
    po.submitted_date AS "Submitted Date",
    po.pending_approval_with AS "Pending Approval With",
    po.approved_date AS "PO/ SO Approved Date",
    m.material_name AS "Material",
    m.material_category AS "Material Category",
    m.material_sub_category AS "Material Sub-Category",
    v.vendor_name AS "Vendor Name",
    e2.employee_name AS "Indentor Name",
    i.value_of_indent AS "Value of Indent",
    po.value AS "Value of PO",
    p.project_name AS "Project",
    gri.grin_no AS "GRIN No",
    inv.invoice_no AS "Invoice No",
    giss.giss_no AS "GISS No",
    po.value_pending_to_paid AS "Value Pending to be Paid",
    i.current_stage AS "Current Stage of the Indent",
    i.short_closed_cancelled AS "Short-Closed and Cancelled Through Amendment",
    i.short_closure_reason AS "Reason for Short-Closure & Cancellation"
    FROM indent i
    LEFT JOIN employee_department_master e ON i.assigned_to = e.employee_id
    LEFT JOIN purchase_order po ON i.indent_id = po.indent_id
    LEFT JOIN material_master m ON i.material_id = m.material_id
    LEFT JOIN vendor_master v ON i.vendor_id = v.vendor_id
    LEFT JOIN employee_department_master e2 ON i.indentor_id = e2.employee_id
    LEFT JOIN project_master p ON i.project_id = p.project_id
    LEFT JOIN goods_receipt_inspection gri ON po.po_no = gri.po_no
    LEFT JOIN invoice inv ON po.po_no = inv.po_no
    LEFT JOIN giss ON po.po_no = giss.po_no
    WHERE i.create_date BETWEEN '2024-01-01' AND '2024-12-31'
    ORDER BY
    i.create_date;
       """, nativeQuery = true)
    List<IndentReportDetailsDTO> fetchIndentReportDetails();

 */

}
