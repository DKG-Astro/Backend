package com.astro.repository.ProcurementModule.IndentCreation;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentReportDetailsDTO;
import com.astro.dto.workflow.ProcurementDtos.TechnoMomReportDTO;
import com.astro.entity.ProcurementModule.IndentCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IndentCreationRepository extends JpaRepository<IndentCreation,String> {

    @Query("SELECT DISTINCT i.projectName FROM IndentCreation i WHERE i.indentId IN :indentIds")
    List<String> findDistinctProjectNames(@Param("indentIds") List<String> indentIds);

    @Query(value = """
            SELECT
                ic.indent_id AS `Indent Id`,
              
                -- Approved Date (only if the current role is Reporting Officer)
                (SELECT wt.createdDate
                 FROM workflow_transition wt
                 WHERE wt.requestId = ic.indent_id AND wt.CURRENTROLE = 'Reporting Officer'
                 ORDER BY wt.createdDate DESC LIMIT 1) AS `Approved Date`,
             
                -- Assigned To (latest next role from workflow transition table)
               (SELECT wt.nextRole
                 FROM workflow_transition wt
                  WHERE wt.requestId = ic.indent_id
                  ORDER BY wt.createdDate DESC LIMIT 1) AS `Assigned To`,
             
                tr.tender_id AS `Tender Request`,
                tr.mode_of_procurement AS `Mode of Tendering`,
             
                -- Corresponding PO/SO (taken from PO/SO table via Tender)
                (SELECT COALESCE(po.po_id, so.so_id)
                 FROM tender_request tr2
                 LEFT JOIN purchase_order po ON tr2.tender_id = po.tender_id
                 LEFT JOIN service_order so ON tr2.tender_id = so.tender_id
                 WHERE tr2.tender_id = tr.tender_id
                 ORDER BY po.created_date DESC, so.created_date DESC LIMIT 1) AS `Corresponding PO/ SO`,
              
                -- Status of PO/SO (latest status from workflow transition)
                (SELECT wt.status
                 FROM workflow_transition wt
                 WHERE wt.requestId= (SELECT COALESCE(po.po_id, so.so_id)
                                        FROM purchase_order po
                                        LEFT JOIN service_order so ON po.tender_id = so.tender_id
                                        WHERE po.tender_id = tr.tender_id OR so.tender_id = tr.tender_id
                                        ORDER BY po.created_date DESC, so.created_date DESC LIMIT 1)
                 ORDER BY wt.createdDate DESC LIMIT 1) AS `Status of PO/ SO`,
            
                ic.created_date AS `Submitted Date`,
              
                -- Pending Approval With & Pending From (latest next role for indent)
                (SELECT wt.nextRole
                 FROM workflow_transition wt
                 WHERE wt.requestId = ic.indent_id
                 ORDER BY wt.createdDate DESC LIMIT 1) AS `Pending Approval With & Pending From`,
               
                -- PO/SO Approved Date (2nd transaction create date for that request id)
                (SELECT wt.createdDate
                 FROM workflow_transition wt
                 WHERE wt.requestId = (SELECT COALESCE(po.po_id, so.so_id)
                                        FROM purchase_order po
                                        LEFT JOIN service_order so ON po.tender_id = so.tender_id
                                        WHERE po.tender_id = tr.tender_id OR so.tender_id = tr.tender_id
                                        ORDER BY po.created_date DESC, so.created_date DESC LIMIT 1)
                 ORDER BY wt.createdDate ASC LIMIT 1 OFFSET 1) AS `PO/ SO Approved Date`,
              
              md.material_description AS `Material`,
              md.material_category AS `Material Category`,
              md.material_sub_category AS `Material Sub-Category`,
             
                -- Vendor Name (latest from PO/SO)
                (SELECT COALESCE(po.vendor_name, so.vendor_name)
                 FROM purchase_order po
                 LEFT JOIN service_order so ON po.tender_id = so.tender_id
                 WHERE po.tender_id = tr.tender_id OR so.tender_id = tr.tender_id
                 ORDER BY po.created_date DESC, so.created_date DESC LIMIT 1) AS `Vendor Name`,
            
                ic.indentor_name AS `Indentor Name`,
           
                -- Sum of value of all materials in indent
            --  (SELECT SUM(ic2.value) FROM indent_creation ic2 WHERE ic2.indent_id = ic.indent_id) AS `Value of Indent`,
              (SELECT SUM(md.total_price) FROM material_details md WHERE md.indent_id = ic.indent_id) AS `Value of Indent`,
              -- Value of PO (linked via Tender)
              (SELECT po.total_value_of_po FROM purchase_order po WHERE po.tender_id = tr.tender_id ORDER BY po.created_date DESC LIMIT 1) AS `Value of PO`,
              -- GRIN No (latest GRIN entry)
              -- (SELECT gr.grin_no FROM goods_receipt_inspection gr WHERE gr.indent_id = ic.indent_id ORDER BY gr.create_date DESC LIMIT 1) AS `GRIN No`,
              ic.project_name AS `Project`,
           
              CAST(NULL AS CHAR) AS 'invoiceNo',
              CAST(NULL AS CHAR) AS 'gissNo',
              CAST(NULL AS DECIMAL(19, 2)) AS 'valuePendingToBePaid',
               
               
            
                -- Current Stage of Indent (latest next role)
                (SELECT wt.nextRole
                 FROM workflow_transition wt
                 WHERE wt.requestId = ic.indent_id
                 ORDER BY wt.createdDate DESC LIMIT 1) AS `Current Stage of the Indent`,
               
                -- Short-closed and cancelled through amendment
                (SELECT CASE WHEN wt.action = 'Rejected' THEN 'Short-closed and cancelled through amendment' ELSE NULL END
                 FROM workflow_transition wt
                 WHERE wt.requestId = ic.indent_id
                 ORDER BY wt.createdDate DESC LIMIT 1) AS `Short-Closed and Cancelled Through Amendment`,
   
                -- Reason for short closure & cancellation
                (SELECT wt.remarks
                 FROM workflow_transition wt
                 WHERE wt.requestId = ic.indent_id AND wt.action = 'Rejected'
                 ORDER BY wt.createdDate DESC LIMIT 1) AS `Reason for Short-Closure & Cancellation`
                        
           FROM indent_creation ic
              LEFT JOIN indent_id iid ON ic.indent_id = iid.indent_id
              LEFT JOIN tender_request tr ON iid.tender_id = tr.tender_id
              LEFT JOIN  material_details md ON ic.indent_id = md.indent_id
              WHERE ic.created_date BETWEEN :startDate AND :endDate
                 
                 """, nativeQuery = true)
    List<Object[]> fetchIndentReportDetails(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query(value = """
            SELECT
                indent.created_date AS 'Date',
                CAST(NULL AS CHAR) AS 'Uploaded Techno Commercial MoM Reports',
                po.po_id AS 'PO/ WO No',
                po.total_value_of_po AS 'Value',
                indent.indent_id AS 'Corresponding Indent Number'
            FROM indent_creation AS indent
            LEFT JOIN purchase_order AS po ON indent.indent_id = po.indent_id
            WHERE indent.created_date BETWEEN :startDate AND :endDate
            ORDER BY
                indent.created_date;
     """, nativeQuery = true)
    List<Object[]> getTechnoMomReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    IndentCreation getByIndentId(String indentId);
}
