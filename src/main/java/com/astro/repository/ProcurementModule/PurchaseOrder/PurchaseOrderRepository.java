package com.astro.repository.ProcurementModule.PurchaseOrder;

import com.astro.dto.workflow.ProcurementDtos.ProcurementActivityReportResponse;
import com.astro.entity.ProcurementModule.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {

    @Query(value = """
                  select
                  po.po_id AS orderId,
                            tr.mode_of_procurement AS modeOfProcurement,
                            '' AS underAMC,
                            '' AS amcFor,
                            '' AS endUser,
                             CAST(NULL AS UNSIGNED) AS noOfParticipants,
                            po.total_value_of_po AS value,
                            po.consignes_address AS location,
                            po.vendor_name AS vendorName,
                            '' AS previouslyRenewedAMCs,
                            '' AS categoryOfSecurity,
                            '' AS validityOfSecurity
                        FROM purchase_order AS po
                        JOIN tender_request AS tr ON po.tender_id = tr.tender_id
                WHERE po.created_date BETWEEN :startDate AND :endDate
                ORDER BY po.created_date
            """, nativeQuery = true)
    List<Object[]> getVendorContractDetails(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
                SELECT
                po.po_id AS 'Order Id',
                CASE
                WHEN vm.registered_platform = 'Yes' THEN 'GeM'
                WHEN vm.registered_platform = 'No' THEN 'Non-GeM'
                ELSE 'Unknown'
                END AS 'GeM / Non-GeM',
                '' As 'Indentor',
                po.total_value_of_po AS 'Value',
                '' AS 'Description of goods/services',
                vm.vendor_name AS 'Vendor Name'
                FROM purchase_order AS po
                LEFT JOIN vendor_master AS vm ON po.vendor_name = vm.vendor_name
                WHERE po.created_date BETWEEN :startDate AND :endDate
            ORDER BY po.created_date
                 """, nativeQuery = true)
    List<Object[]> getProcurementActivityReport(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    PurchaseOrder findByPoId(String poId);

    List<PurchaseOrder> findByVendorId(String vendorId);

    PurchaseOrder findByTenderId(String tenderId);

    @Query(value = """
            SELECT
              wt.createdDate                               AS approvedDate,
              po.po_id                                     AS poId,
              po.vendor_name                               AS vendorName,
              po.total_value_of_po                         AS value,
              po.tender_id                                 AS tenderId,
              po.project_name                              AS project,
              po.vendor_id                                 AS vendorId,
              (SELECT GROUP_CONCAT(i.indent_id SEPARATOR ', ')
                 FROM indent_id i WHERE i.tender_id = po.tender_id
              )                                            AS indentIds,
               (SELECT md.mode_of_procurement
                            FROM material_details md
                            WHERE md.indent_id IN (
                            SELECT i.indent_id FROM indent_id i WHERE i.tender_id = po.tender_id
                         ) LIMIT 1) AS modeOfProcurement,
              JSON_ARRAYAGG(
                JSON_OBJECT(
                  'materialCode',       attr.material_code,
                  'materialDescription',attr.material_description,
                  'quantity',           attr.quantity,
                  'rate',               attr.rate,
                  'currency',           attr.currency,
                  'exchangeRate',       attr.exchange_rate,
                  'gst',                attr.gst,
                  'duties',             attr.duties,
                  'freightCharge',      attr.freight_charge,
                  'budgetCode',         attr.budget_code,
                  'receivedQuantity',   attr.received_quantity
                )
              )                                            AS attributesJson
            FROM workflow_transition wt
            JOIN purchase_order po  ON wt.requestId = po.po_id
            JOIN purchase_order_attributes attr 
              ON po.po_id = attr.po_id
            WHERE wt.workflowName = 'PO Workflow'
              AND wt.status        = 'Completed'
              AND wt.nextAction   IS NULL
              AND wt.createdDate BETWEEN :from AND :to
            GROUP BY
              wt.createdDate, po.po_id, po.vendor_name,
              po.total_value_of_po, po.tender_id,
              po.project_name, po.vendor_id
            ORDER BY wt.createdDate, po.po_id
            """, nativeQuery = true)
    List<Object[]> getApprovedPoReport(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(value = """
            SELECT
              po.po_id                                     AS poId,
              po.tender_id                                 AS tenderId,
              (SELECT GROUP_CONCAT(i.indent_id SEPARATOR ', ')
                 FROM indent_id i WHERE i.tender_id = po.tender_id
              )                                            AS indentIds,
              po.total_value_of_po                         AS value,
              po.vendor_name                               AS vendorName,
              wt.createdDate                               AS submittedDate,
              wt.nextRole                                  AS pendingWith,
              wt.modificationDate                          AS pendingFrom,
              wt.status                                    AS status,
              JSON_ARRAYAGG(
                JSON_OBJECT(
                  'materialCode',        attr.material_code,
                  'materialDescription', attr.material_description,
                  'quantity',            attr.quantity,
                  'rate',                attr.rate,
                  'currency',            attr.currency,
                  'exchangeRate',        attr.exchange_rate,
                  'gst',                 attr.gst,
                  'duties',              attr.duties,
                  'freightCharge',       attr.freight_charge,
                  'budgetCode',          attr.budget_code,
                  'receivedQuantity',    attr.received_quantity
                )
              )                                            AS attributesJson
            FROM workflow_transition wt
            JOIN purchase_order po
              ON wt.requestId = po.po_id
            JOIN purchase_order_attributes attr
              ON po.po_id = attr.po_id
            WHERE wt.workflowName = 'PO Workflow'
              AND wt.nextAction = 'Pending'
              AND wt.nextRole IS NOT NULL
              AND wt.createdDate BETWEEN :fromDate AND :toDate
            GROUP BY
              po.po_id, po.tender_id, po.total_value_of_po,
              po.vendor_name, wt.createdDate, wt.nextRole,
              wt.modificationDate, wt.status
            ORDER BY wt.createdDate, po.po_id
            """,
            nativeQuery = true)
    List<Object[]> getPendingPoReport(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );


    // PurchaseOrder getByPoId(String poId);
}
