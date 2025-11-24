package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.PaymentVoucherMaterialDto;
import com.astro.dto.workflow.PaymentVoucherReportDto;
import com.astro.dto.workflow.paymentVoucherMaterialRequestDto;
import com.astro.dto.workflow.paymentVoucherRequestDto;
import com.astro.entity.PaymentVoucher;
import com.astro.entity.PaymentVoucherMaterials;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.PaymentVoucherMaterialsRepository;
import com.astro.repository.InventoryModule.PaymentVoucherReposiotry;
import com.astro.service.PaymentVoucherService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentVoucherServiceImpl implements PaymentVoucherService {
    @Autowired
    private PaymentVoucherReposiotry paymentVoucherReposiotry;
    @Autowired
    private PaymentVoucherMaterialsRepository paymentVoucherMaterialsRepository;
/*
    @Override
    @Transactional
    public String createPaymentVoucher(paymentVoucherRequestDto dto) {

        if ("Advance".equalsIgnoreCase(dto.getPaymentVoucherType()) &&
                dto.getGrnNumber() != null &&
                !dto.getGrnNumber().trim().isEmpty()) {

            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.ERROR_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_RESOURCE,
                            "Items are received by user. Advance payment cannot be raised now. Please use Partial or Full Payment."
                    )
            );
        }

        if (("Partial".equalsIgnoreCase(dto.getPaymentVoucherType()) ||
                "Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) &&
                (dto.getGrnNumber() == null || dto.getGrnNumber().trim().isEmpty())) {

            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.ERROR_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_CODE_RESOURCE,
                            AppConstant.ERROR_TYPE_RESOURCE,
                            "GRN Number is required for Partial or Full Payment vouchers."
                    )
            );
        }
        PaymentVoucher voucher = new PaymentVoucher();
        voucher.setPaymentVoucherNumber("INV/1");
        voucher.setPaymentVoucherDate(dto.getPaymentVoucherDate());
        voucher.setPaymentVoucherIsFor(dto.getPaymentVoucherIsFor());
        voucher.setPurchaseOrderId(dto.getPurchaseOrderId());
        voucher.setGrnNumber(dto.getGrnNumber());
        voucher.setServiceOrderDetails(dto.getServiceOrderDetails());
        voucher.setPaymentVoucherType(dto.getPaymentVoucherType());
        voucher.setVendorName(dto.getVendorName());
        voucher.setVendorInvoiceNumber(dto.getVendorInvoiceNumber());
        voucher.setVendorInvoiceDate(dto.getVendorInvoiceDate());
        voucher.setCurrency(dto.getCurrency());
        voucher.setExchangeRate(dto.getExchangeRate());
        voucher.setStatus(dto.getStatus());
        voucher.setRemarks(dto.getRemarks());
        voucher.setTotalAmount(dto.getTotalAmount());
        voucher.setPartialAmount(dto.getPartialAmount());
        voucher.setAdvanceAmount(dto.getAdvanceAmount());
        voucher.setSoId(dto.getServiceOrderDetails());
        voucher.setCreatedBy(dto.getCreatedBy());
        voucher.setTdsAmount(dto.getTdsAmount());
        voucher.setPaymentVoucherNetAmount(dto.getPaymentVoucherNetAmount());

      //  Optional<PaymentVoucher> existingVoucherOpt = paymentVoucherReposiotry.findTopByGrnNumberOrderByIdDesc(dto.getGrnNumber());

      /* Optional<PaymentVoucher> existingVoucherOpt;

        if (dto.getGrnNumber() != null && !dto.getGrnNumber().trim().isEmpty()) {

            existingVoucherOpt = paymentVoucherReposiotry
                    .findTopByGrnNumberOrderByIdDesc(dto.getGrnNumber());
        } else {

            existingVoucherOpt = paymentVoucherReposiotry
                    .findTopByPurchaseOrderIdOrderByIdDesc(dto.getPurchaseOrderId());
        }
        if (existingVoucherOpt.isPresent()) {
            PaymentVoucher existingVoucher = existingVoucherOpt.get();
            String type = existingVoucher.getPaymentVoucherType();

            if ("Partial".equalsIgnoreCase(type)) {
                BigDecimal paid = existingVoucher.getPaidAmount() != null
                        ? existingVoucher.getPaidAmount()
                        : BigDecimal.ZERO;
                BigDecimal partial = dto.getPartialAmount() != null
                        ? dto.getPartialAmount()
                        : BigDecimal.ZERO;
                voucher.setPaidAmount(paid.add(partial));
            } else if ("Advance".equalsIgnoreCase(type)) {
                BigDecimal paid = existingVoucher.getPaidAmount() != null
                        ? existingVoucher.getPaidAmount()
                        : BigDecimal.ZERO;
                BigDecimal partial = dto.getAdvanceAmount() != null
                        ? dto.getAdvanceAmount()
                        : BigDecimal.ZERO;
                voucher.setPaidAmount(paid.add(partial));
            }
        }else{
          if(dto.getPartialAmount()!=null){
              voucher.setPaidAmount(dto.getPartialAmount());
          }else{
              voucher.setPaidAmount(dto.getAdvanceAmount());
          }
        }

        // =========================================================
// 1) FETCH TOTAL ADVANCE FOR THIS PO
// =========================================================
        BigDecimal totalAdvancePaid = paymentVoucherReposiotry
                .getTotalAdvancePaid(dto.getPurchaseOrderId()); // PO total advance

        BigDecimal totalAdvanceUsed = paymentVoucherReposiotry
                .getUsedAdvance(dto.getPurchaseOrderId()); // already adjusted advance

        BigDecimal remainingAdvance = totalAdvancePaid.subtract(totalAdvanceUsed);


// =========================================================
// 2) IF PAYMENT IS PARTIAL OR FULL → APPLY ADVANCE TO THIS GRN
// =========================================================
      //  BigDecimal advanceAppliedToThisGRN = BigDecimal.ZERO;

        BigDecimal grnValue = dto.getTotalAmount();   // GRN total value

        // =========================================================
// ERP RULE: ADVANCE MUST BE APPLIED ONLY ON FIRST PAYMENT
// =========================================================

// Check if advance already applied for this GRN
        BigDecimal previousAdvanceAppliedForGrn =
                paymentVoucherReposiotry.getAdvanceAppliedForGrn(dto.getGrnNumber());

// Default = no advance applied
        BigDecimal advanceAppliedToThisGRN = BigDecimal.ZERO;

        if (!"Advance".equalsIgnoreCase(dto.getPaymentVoucherType())) {

            if (previousAdvanceAppliedForGrn.compareTo(BigDecimal.ZERO) > 0) {

                // =========================================================
                // ERP RULE: Advance already consumed for this GRN earlier.
                // → Do NOT apply advance again.
                // =========================================================
                advanceAppliedToThisGRN = BigDecimal.ZERO;
                voucher.setAdvanceAdjustedAmount(BigDecimal.ZERO);

            } else {

                // =========================================================
                // FIRST payment voucher for this GRN → apply advance now
                // =========================================================
                if (remainingAdvance.compareTo(BigDecimal.ZERO) > 0) {
                    advanceAppliedToThisGRN = remainingAdvance.min(grnValue);
                } else {
                    advanceAppliedToThisGRN = BigDecimal.ZERO;
                }

                voucher.setAdvanceAdjustedAmount(advanceAppliedToThisGRN);
            }
        }



// =========================================================
// 3) EFFECTIVE GRN AMOUNT AFTER ADVANCE ADJUSTMENT
// =========================================================
        BigDecimal effectiveAmount = grnValue.subtract(advanceAppliedToThisGRN);

// =========================================================
// 4) GET EXISTING PAID AMOUNT FOR THIS GRN (excluding Full Payment rows)
// =========================================================
        BigDecimal existingPaidForGrn = paymentVoucherReposiotry
                .getTotalPaidForGrn(dto.getGrnNumber()); // only partials


// =========================================================
// 5) SET PAID AMOUNT BASED ON TYPE (with safe validations)
// =========================================================

// PARTIAL PAYMENT
        if ("Partial".equalsIgnoreCase(dto.getPaymentVoucherType())) {

            if (effectiveAmount.compareTo(BigDecimal.ZERO) == 0) {
                throw new BusinessException(
                        new ErrorDetails(AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Advance already covers full GRN value. No payment required.")
                );
            }

            BigDecimal partial = dto.getPartialAmount() != null
                    ? dto.getPartialAmount()
                    : BigDecimal.ZERO;

            BigDecimal newTotalPaid = existingPaidForGrn.add(partial);

            System.out.print("effective"+ effectiveAmount);
            System.out.print("newTotalPaid"+ newTotalPaid);
            if (newTotalPaid.compareTo(effectiveAmount) > 0) {
                throw new BusinessException(
                        new ErrorDetails(AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Partial payment exceeds GRN payable amount.")
                );
            }

            voucher.setPaidAmount(newTotalPaid);
        }


// FULL PAYMENT
        else if ("Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) {

            BigDecimal remainingToPay = effectiveAmount.subtract(existingPaidForGrn);

            if (remainingToPay.compareTo(BigDecimal.ZERO) < 0) {
                remainingToPay = BigDecimal.ZERO; // No negative
            }

            // Apply TDS only if remainingToPay > TDS
            BigDecimal net = remainingToPay.subtract(
                    dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO);

            if (net.compareTo(BigDecimal.ZERO) < 0) {
                net = BigDecimal.ZERO;
            }

            voucher.setPaidAmount(existingPaidForGrn);
            voucher.setPaymentVoucherNetAmount(net);
        }


// ADVANCE PAYMENT
        else if ("Advance".equalsIgnoreCase(dto.getPaymentVoucherType())) {
            voucher.setPaidAmount(dto.getAdvanceAmount());
        }





        List<PaymentVoucherMaterials> materialsList = dto.getMaterials().stream().map(m -> {
            PaymentVoucherMaterials material = new PaymentVoucherMaterials();
            material.setMaterialCode(m.getMaterialCode());
            material.setMaterialDescription(m.getMaterialDescription());
            material.setQuantity(m.getQuantity());
            material.setUnitPrice(m.getUnitPrice());
            material.setCurrency(m.getCurrency());
            material.setExchangeRate(m.getExchangeRate());
            material.setGst(m.getGst());
            material.setPaymentVoucher(voucher);
            return material;
        }).collect(Collectors.toList());

        voucher.setMaterialsList(materialsList);

     PaymentVoucher pv=   paymentVoucherReposiotry.save(voucher);

     if(pv.getPaymentVoucherType().equalsIgnoreCase("Advance")){
         return "INV"+ "/" + pv.getId();
     }
        return dto.getGrnNumber() + "/" + pv.getId();
    }*/
/*
    @Override
    @Transactional
    public String createPaymentVoucher(paymentVoucherRequestDto dto) {
        // Basic validations (unchanged)
        if ("Advance".equalsIgnoreCase(dto.getPaymentVoucherType()) &&
                dto.getGrnNumber() != null &&
                !dto.getGrnNumber().trim().isEmpty()) {

            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Items are received by user. Advance payment cannot be raised now. Please use Partial or Full Payment."
            ));
        }

        if (("Partial".equalsIgnoreCase(dto.getPaymentVoucherType()) ||
                "Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) &&
                (dto.getGrnNumber() == null || dto.getGrnNumber().trim().isEmpty())) {

            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "GRN Number is required for Partial or Full Payment vouchers."
            ));
        }

        // Create entity and copy basic fields
        PaymentVoucher voucher = new PaymentVoucher();
        voucher.setPaymentVoucherNumber("INV/1"); // keep your numbering logic separate later
        voucher.setPaymentVoucherDate(dto.getPaymentVoucherDate());
        voucher.setPaymentVoucherIsFor(dto.getPaymentVoucherIsFor());
        voucher.setPurchaseOrderId(dto.getPurchaseOrderId());
        voucher.setGrnNumber(dto.getGrnNumber());
        voucher.setServiceOrderDetails(dto.getServiceOrderDetails());
        voucher.setPaymentVoucherType(dto.getPaymentVoucherType());
        voucher.setVendorName(dto.getVendorName());
        voucher.setVendorInvoiceNumber(dto.getVendorInvoiceNumber());
        voucher.setVendorInvoiceDate(dto.getVendorInvoiceDate());
        voucher.setCurrency(dto.getCurrency());
        voucher.setExchangeRate(dto.getExchangeRate());
        voucher.setStatus(dto.getStatus());
        voucher.setRemarks(dto.getRemarks());
        voucher.setTotalAmount(dto.getTotalAmount());
        voucher.setPartialAmount(dto.getPartialAmount());
        voucher.setAdvanceAmount(dto.getAdvanceAmount());
        voucher.setSoId(dto.getServiceOrderDetails());
        voucher.setCreatedBy(dto.getCreatedBy());
        // NOTE: TDS & Net will be set only for Full Payment below (ERP rule)
        voucher.setTdsAmount(BigDecimal.ZERO);
        voucher.setPaymentVoucherNetAmount(BigDecimal.ZERO);

        // ---------- 1) PO level advance balances ----------
        BigDecimal totalAdvancePaid = paymentVoucherReposiotry
                .getTotalAdvancePaid(dto.getPurchaseOrderId());
        if (totalAdvancePaid == null) totalAdvancePaid = BigDecimal.ZERO;

        BigDecimal totalAdvanceUsed = paymentVoucherReposiotry
                .getUsedAdvance(dto.getPurchaseOrderId());
        if (totalAdvanceUsed == null) totalAdvanceUsed = BigDecimal.ZERO;

        BigDecimal remainingAdvance = totalAdvancePaid.subtract(totalAdvanceUsed);
        if (remainingAdvance.compareTo(BigDecimal.ZERO) < 0) {
            // safety: shouldn't happen, but guard against negative
            remainingAdvance = BigDecimal.ZERO;
        }

        // ---------- 2) GRN values & prior advance-for-GRN ----------
        BigDecimal grnValue = dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO;

        // How much advance already applied earlier for this GRN (if any)
        BigDecimal previousAdvanceAppliedForGrn = BigDecimal.ZERO;
        if (dto.getGrnNumber() != null && !dto.getGrnNumber().trim().isEmpty()) {
            previousAdvanceAppliedForGrn = paymentVoucherReposiotry.getAdvanceAppliedForGrn(dto.getGrnNumber());
            if (previousAdvanceAppliedForGrn == null) previousAdvanceAppliedForGrn = BigDecimal.ZERO;
        }

        // ---------- 3) Compute system suggested advance for this GRN (first-time only) ----------
        BigDecimal systemSuggestedAdvance;
        if (previousAdvanceAppliedForGrn.compareTo(BigDecimal.ZERO) > 0) {
            // Advance was already applied for this GRN in an earlier voucher → default to that value
            systemSuggestedAdvance = previousAdvanceAppliedForGrn;
        } else {
            // First payment for this GRN → suggest min(remainingAdvance, grnValue)
            systemSuggestedAdvance = remainingAdvance.min(grnValue);
        }

        // ---------- 4) User-editable advanceApplied (dto may override suggestion) ----------
        BigDecimal advanceAppliedToThisGRN = BigDecimal.ZERO;
        if (dto.getAdvanceAdjustedAmount() != null) {
            advanceAppliedToThisGRN = dto.getAdvanceAdjustedAmount();

            // Validation: cannot exceed remainingAdvance or GRN value
            if (advanceAppliedToThisGRN.compareTo(remainingAdvance) > 0) {
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Entered advance exceeds remaining PO advance."
                ));
            }
            if (advanceAppliedToThisGRN.compareTo(grnValue) > 0) {
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Advance cannot exceed GRN total amount."
                ));
            }
           /* // If previousAdvanceAppliedForGrn > 0 and user tries to change, allow only if equals previously applied (or handle per policy)
            if (previousAdvanceAppliedForGrn.compareTo(BigDecimal.ZERO) > 0
                    && advanceAppliedToThisGRN.compareTo(previousAdvanceAppliedForGrn) != 0) {
                // Optional: choose policy — here we allow editing only if equals previous or throw. Using throw for safety.
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Advance for this GRN was already applied earlier; editing is not allowed."
                ));
            }*/
/*

        } else {
            // no user override → use system suggestion
            advanceAppliedToThisGRN = systemSuggestedAdvance;
        }

        // Save the selected advance deduction on voucher (can be zero)
        voucher.setAdvanceAdjustedAmount(advanceAppliedToThisGRN);

        // ---------- 5) Compute effective amount payable for this GRN after advance ----------
        BigDecimal effectiveAmount = grnValue.subtract(advanceAppliedToThisGRN);
        if (effectiveAmount.compareTo(BigDecimal.ZERO) < 0) {
            // safety clamp
            effectiveAmount = BigDecimal.ZERO;
        }

        // ---------- 6) existing paid amount for this GRN (partials already recorded) ----------
        BigDecimal existingPaidForGrn = BigDecimal.ZERO;
        if (dto.getGrnNumber() != null && !dto.getGrnNumber().trim().isEmpty()) {
            existingPaidForGrn = paymentVoucherReposiotry.getTotalPaidForGrn(dto.getGrnNumber());
            if (existingPaidForGrn == null) existingPaidForGrn = BigDecimal.ZERO;
        }

        // ---------- 7) Payment type-specific logic & validations ----------
        String type = dto.getPaymentVoucherType();
        if ("Partial".equalsIgnoreCase(type)) {

            BigDecimal partial = dto.getPartialAmount() != null ? dto.getPartialAmount() : BigDecimal.ZERO;

            // Ensure partial will not exceed effectiveAmount
            BigDecimal newTotalPaid = existingPaidForGrn.add(partial);
            if (newTotalPaid.compareTo(effectiveAmount) > 0) {
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Partial payment exceeds payable amount after advance deduction."
                ));
            }

            // write cumulative paid for GRN into voucher (so DB shows cumulative after saving)
            voucher.setPaidAmount(newTotalPaid);

        } else if ("Full Payment".equalsIgnoreCase(type)) {

            // Remaining to pay after partials and advance
            BigDecimal remainingToPay = effectiveAmount.subtract(existingPaidForGrn);
            if (remainingToPay.compareTo(BigDecimal.ZERO) < 0) {
                remainingToPay = BigDecimal.ZERO;
            }

            BigDecimal tds = dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO;
            BigDecimal net = remainingToPay.subtract(tds);
            if (net.compareTo(BigDecimal.ZERO) < 0) {
                net = BigDecimal.ZERO;
            }

            // Full voucher stores existingPaid (cumulative previous partials) and net amount to be paid now
            voucher.setPaidAmount(existingPaidForGrn);
            voucher.setPaymentVoucherNetAmount(net);
            voucher.setTdsAmount(tds);

        } else if ("Advance".equalsIgnoreCase(type)) {

        BigDecimal adv = dto.getAdvanceAmount() != null ? dto.getAdvanceAmount() : BigDecimal.ZERO;

        // Store only current advance here (not cumulative)
        voucher.setPaidAmount(adv);

        // Advance voucher should not adjust any GRN
        voucher.setAdvanceAdjustedAmount(BigDecimal.ZERO);

        // No TDS, no net amount for advance
        voucher.setTdsAmount(BigDecimal.ZERO);
        voucher.setPaymentVoucherNetAmount(BigDecimal.ZERO);
    } else {
            // Unknown type — defensive
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Unknown payment voucher type."
            ));
        }

        // ---------- 8) map & attach materials (same as your code) ----------
        List<PaymentVoucherMaterials> materialsList = dto.getMaterials().stream().map(m -> {
            PaymentVoucherMaterials material = new PaymentVoucherMaterials();
            material.setMaterialCode(m.getMaterialCode());
            material.setMaterialDescription(m.getMaterialDescription());
            material.setQuantity(m.getQuantity());
            material.setUnitPrice(m.getUnitPrice());
            material.setCurrency(m.getCurrency());
            material.setExchangeRate(m.getExchangeRate());
            material.setGst(m.getGst());
            material.setPaymentVoucher(voucher);
            return material;
        }).collect(Collectors.toList());

        voucher.setMaterialsList(materialsList);

        // ---------- 9) Persist voucher ----------
        PaymentVoucher pv = paymentVoucherReposiotry.save(voucher);

        // Return numbering (you can replace with your proper numbering routine)
        if (pv.getPaymentVoucherType().equalsIgnoreCase("Advance")) {
            return "INV" + "/" + pv.getId();
        }
        return dto.getGrnNumber() + "/" + pv.getId();
    }*/

    /*
@Override
@Transactional
public String createPaymentVoucher(paymentVoucherRequestDto dto) {

    // ======================= BASIC VALIDATIONS ==========================
    if ("Advance".equalsIgnoreCase(dto.getPaymentVoucherType()) &&
            dto.getGrnNumber() != null &&
            !dto.getGrnNumber().trim().isEmpty()) {

        throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Items are received. Advance cannot be raised now."
        ));
    }

    if (("Partial".equalsIgnoreCase(dto.getPaymentVoucherType()) ||
            "Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) &&
            (dto.getGrnNumber() == null || dto.getGrnNumber().trim().isEmpty())) {

        throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "GRN Number is required for Partial or Full Payment."
        ));
    }

    // ======================= CREATE PV OBJECT ==========================
    PaymentVoucher voucher = new PaymentVoucher();
    voucher.setPaymentVoucherNumber("INV/1");
    voucher.setPaymentVoucherDate(dto.getPaymentVoucherDate());
    voucher.setPaymentVoucherIsFor(dto.getPaymentVoucherIsFor());
    voucher.setPurchaseOrderId(dto.getPurchaseOrderId());
    voucher.setGrnNumber(dto.getGrnNumber());
    voucher.setServiceOrderDetails(dto.getServiceOrderDetails());
    voucher.setPaymentVoucherType(dto.getPaymentVoucherType());
    voucher.setVendorName(dto.getVendorName());
    voucher.setVendorInvoiceNumber(dto.getVendorInvoiceNumber());
    voucher.setVendorInvoiceDate(dto.getVendorInvoiceDate());
    voucher.setCurrency(dto.getCurrency());
    voucher.setExchangeRate(dto.getExchangeRate());
    voucher.setStatus(dto.getStatus());
    voucher.setRemarks(dto.getRemarks());
    voucher.setTotalAmount(dto.getTotalAmount());
    voucher.setPartialAmount(dto.getPartialAmount());
    voucher.setAdvanceAmount(dto.getAdvanceAmount());
    voucher.setSoId(dto.getServiceOrderDetails());
    voucher.setCreatedBy(dto.getCreatedBy());
    voucher.setTdsAmount(BigDecimal.ZERO);
    voucher.setPaymentVoucherNetAmount(BigDecimal.ZERO);

    // ======================= 1) ADVANCE BALANCE (PO LEVEL) ==========================
    BigDecimal totalAdvancePaid = paymentVoucherReposiotry.getTotalAdvancePaid(dto.getPurchaseOrderId());
    if (totalAdvancePaid == null) totalAdvancePaid = BigDecimal.ZERO;

    BigDecimal totalAdvanceUsed = paymentVoucherReposiotry.getUsedAdvance(dto.getPurchaseOrderId());
    if (totalAdvanceUsed == null) totalAdvanceUsed = BigDecimal.ZERO;

    BigDecimal remainingAdvance = totalAdvancePaid.subtract(totalAdvanceUsed);
    if (remainingAdvance.compareTo(BigDecimal.ZERO) < 0)
        remainingAdvance = BigDecimal.ZERO;

    // ======================= 2) GRN VALUES ==========================
    BigDecimal grnValue = dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO;

    // Already applied advance for this GRN
    BigDecimal previousAdvanceApplied = paymentVoucherReposiotry.getAdvanceAppliedForGrn(dto.getGrnNumber());
    if (previousAdvanceApplied == null) previousAdvanceApplied = BigDecimal.ZERO;

    // ======================= 3) SUGGESTED ADVANCE ==========================
    BigDecimal systemSuggestedAdvance =
            previousAdvanceApplied.compareTo(BigDecimal.ZERO) > 0
                    ? previousAdvanceApplied
                    : remainingAdvance.min(grnValue);

    // ======================= 4) USER-ENTERED ADVANCE ==========================
    BigDecimal advanceAppliedToThisGRN = dto.getAdvanceAdjustedAmount() != null
            ? dto.getAdvanceAdjustedAmount()
            : systemSuggestedAdvance;

    // Validate advance
    if (advanceAppliedToThisGRN.compareTo(remainingAdvance) > 0)
        throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Entered advance exceeds remaining PO advance"));

    if (advanceAppliedToThisGRN.compareTo(grnValue) > 0)
        throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
            "Advance cannot exceed GRN value"));

    // SAVE advance adjusted on this voucher
    voucher.setAdvanceAdjustedAmount(advanceAppliedToThisGRN);

    // ======================= 5) EFFECTIVE PAYABLE AFTER ADVANCE ==========================
    BigDecimal effectiveAmount = grnValue.subtract(advanceAppliedToThisGRN);
    if (effectiveAmount.compareTo(BigDecimal.ZERO) < 0)
        effectiveAmount = BigDecimal.ZERO;

    // ======================= 6) EXISTING PARTIAL PAID ==========================
    BigDecimal totalPartialPaid = paymentVoucherReposiotry.getTotalPartialPaidForGrn(dto.getGrnNumber());
    if (totalPartialPaid == null) totalPartialPaid = BigDecimal.ZERO;

    // ======================= 7) PAYMENT TYPE LOGIC ==========================
    String type = dto.getPaymentVoucherType();

    // ************* PARTIAL PAYMENT *************
    if ("Partial".equalsIgnoreCase(type)) {

        BigDecimal partial = dto.getPartialAmount() != null ? dto.getPartialAmount() : BigDecimal.ZERO;

        // Check exceeding
        if (totalPartialPaid.add(partial).compareTo(effectiveAmount) > 0)
            throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                    "Partial exceeds remaining amount."
        ));
        // ✔ FIXED: store only current partial (NOT cumulative)
        voucher.setPaidAmount(partial);
    }

    // ************* FULL PAYMENT *************
    else if ("Full Payment".equalsIgnoreCase(type)) {

        BigDecimal remaining = effectiveAmount.subtract(totalPartialPaid);
        if (remaining.compareTo(BigDecimal.ZERO) < 0)
            remaining = BigDecimal.ZERO;

        BigDecimal tds = dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO;
        BigDecimal net = remaining.subtract(tds);
        if (net.compareTo(BigDecimal.ZERO) < 0) net = BigDecimal.ZERO;

        voucher.setPaidAmount(remaining);
        voucher.setPaymentVoucherNetAmount(net);
        voucher.setTdsAmount(tds);
    }

    // ************* ADVANCE PAYMENT *************
    else if ("Advance".equalsIgnoreCase(type)) {

        BigDecimal adv = dto.getAdvanceAmount() != null ? dto.getAdvanceAmount() : BigDecimal.ZERO;

        // ✔ Store ONLY the current advance (not cumulative)
        voucher.setPaidAmount(adv);

        // ✔ Advance vouchers never adjust GRN
        voucher.setAdvanceAdjustedAmount(BigDecimal.ZERO);
    }

    else {
        throw new BusinessException(new ErrorDetails(
                AppConstant.ERROR_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                AppConstant.ERROR_TYPE_RESOURCE,
                "Unknow voucher type"
        ));  }

    // ======================= MATERIALS ==========================
    List<PaymentVoucherMaterials> materialsList = dto.getMaterials().stream().map(m -> {
        PaymentVoucherMaterials material = new PaymentVoucherMaterials();
        material.setMaterialCode(m.getMaterialCode());
        material.setMaterialDescription(m.getMaterialDescription());
        material.setQuantity(m.getQuantity());
        material.setUnitPrice(m.getUnitPrice());
        material.setCurrency(m.getCurrency());
        material.setExchangeRate(m.getExchangeRate());
        material.setGst(m.getGst());
        material.setPaymentVoucher(voucher);
        return material;
    }).collect(Collectors.toList());

    voucher.setMaterialsList(materialsList);

    // ======================= SAVE ==========================
    PaymentVoucher pv = paymentVoucherReposiotry.save(voucher);

    if (pv.getPaymentVoucherType().equalsIgnoreCase("Advance"))
        return "INV/" + pv.getId();

    return dto.getGrnNumber() + "/" + pv.getId();
}
*/
    @Override
    @Transactional
    public String createPaymentVoucher(paymentVoucherRequestDto dto) {


        if ("Advance".equalsIgnoreCase(dto.getPaymentVoucherType()) &&
                dto.getGrnNumber() != null &&
                !dto.getGrnNumber().trim().isEmpty()) {

            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Items are received. Advance cannot be raised now."
            ));
        }

        if (("Partial".equalsIgnoreCase(dto.getPaymentVoucherType()) ||
                "Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) &&
                (dto.getGrnNumber() == null || dto.getGrnNumber().trim().isEmpty())) {

            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "GRN Number is required for Partial or Full Payment."
            ));
        }


        PaymentVoucher voucher = new PaymentVoucher();
        voucher.setPaymentVoucherNumber("INV/1");
        voucher.setPaymentVoucherDate(dto.getPaymentVoucherDate());
        voucher.setPaymentVoucherIsFor(dto.getPaymentVoucherIsFor());
        voucher.setPurchaseOrderId(dto.getPurchaseOrderId());
        voucher.setGrnNumber(dto.getGrnNumber());
        voucher.setServiceOrderDetails(dto.getServiceOrderDetails());
        voucher.setPaymentVoucherType(dto.getPaymentVoucherType());
        voucher.setVendorName(dto.getVendorName());
        voucher.setVendorInvoiceNumber(dto.getVendorInvoiceNumber());
        voucher.setVendorInvoiceDate(dto.getVendorInvoiceDate());
        voucher.setCurrency(dto.getCurrency());
        voucher.setExchangeRate(dto.getExchangeRate());
        voucher.setStatus(dto.getStatus());
        voucher.setRemarks(dto.getRemarks());
        voucher.setTotalAmount(dto.getTotalAmount());
        voucher.setPartialAmount(dto.getPartialAmount());
        voucher.setAdvanceAmount(dto.getAdvanceAmount());
        voucher.setSoId(dto.getServiceOrderDetails());
        voucher.setCreatedBy(dto.getCreatedBy());
        voucher.setTdsAmount(BigDecimal.ZERO);
        voucher.setPaymentVoucherNetAmount(BigDecimal.ZERO);



        BigDecimal totalAdvancePaid = paymentVoucherReposiotry.getTotalAdvancePaid(dto.getPurchaseOrderId());
        if (totalAdvancePaid == null) totalAdvancePaid = BigDecimal.ZERO;

        BigDecimal totalAdvanceUsed = paymentVoucherReposiotry.getUsedAdvance(dto.getPurchaseOrderId());
        if (totalAdvanceUsed == null) totalAdvanceUsed = BigDecimal.ZERO;

        BigDecimal remainingAdvance = totalAdvancePaid.subtract(totalAdvanceUsed);
        if (remainingAdvance.compareTo(BigDecimal.ZERO) < 0)
            remainingAdvance = BigDecimal.ZERO;



        BigDecimal grnValue = dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO;



        BigDecimal totalAdvanceAppliedForGrn =
                paymentVoucherReposiotry.getTotalAdvanceAppliedForGrn(dto.getGrnNumber());
        if (totalAdvanceAppliedForGrn == null) totalAdvanceAppliedForGrn = BigDecimal.ZERO;



        BigDecimal systemSuggestedAdvance =
                totalAdvanceAppliedForGrn.compareTo(BigDecimal.ZERO) > 0
                        ? totalAdvanceAppliedForGrn
                        : remainingAdvance.min(grnValue);

        BigDecimal advanceAppliedToThisGRN = dto.getAdvanceAdjustedAmount() != null
                ? dto.getAdvanceAdjustedAmount()
                : systemSuggestedAdvance;

        if (advanceAppliedToThisGRN.compareTo(remainingAdvance) > 0)
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Entered advance exceeds remaining PO advance"));

        if (advanceAppliedToThisGRN.compareTo(grnValue) > 0)
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Advance cannot exceed GRN value"));

        // Save advance applied on this voucher
        voucher.setAdvanceAdjustedAmount(advanceAppliedToThisGRN);

        BigDecimal effectiveAmount = grnValue.subtract(totalAdvanceAppliedForGrn).subtract(advanceAppliedToThisGRN);


        if (effectiveAmount.compareTo(BigDecimal.ZERO) < 0)
            effectiveAmount = BigDecimal.ZERO;

        BigDecimal totalPartialPaid = paymentVoucherReposiotry.getTotalPartialPaidForGrn(dto.getGrnNumber());
        if (totalPartialPaid == null) totalPartialPaid = BigDecimal.ZERO;

        String type = dto.getPaymentVoucherType();

        if ("Partial".equalsIgnoreCase(type)) {

            BigDecimal partial = dto.getPartialAmount() != null ? dto.getPartialAmount() : BigDecimal.ZERO;

            if (totalPartialPaid.add(partial).compareTo(effectiveAmount) > 0)
                throw new BusinessException(new ErrorDetails(
                        AppConstant.ERROR_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_CODE_RESOURCE,
                        AppConstant.ERROR_TYPE_RESOURCE,
                        "Partial exceeds remaining amount."
                ));

            // Store only current partial
            voucher.setPaidAmount(partial);
        }

        else if ("Full Payment".equalsIgnoreCase(type)) {

            BigDecimal remaining = effectiveAmount.subtract(totalPartialPaid);
            if (remaining.compareTo(BigDecimal.ZERO) < 0)
                remaining = BigDecimal.ZERO;

            BigDecimal tds = dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO;

            BigDecimal net = remaining.subtract(tds);
            if (net.compareTo(BigDecimal.ZERO) < 0)
                net = BigDecimal.ZERO;

            voucher.setPaidAmount(remaining);
            voucher.setPaymentVoucherNetAmount(net);
            voucher.setTdsAmount(tds);
        }

        else if ("Advance".equalsIgnoreCase(type)) {

            BigDecimal adv = dto.getAdvanceAmount() != null ? dto.getAdvanceAmount() : BigDecimal.ZERO;

            // Save only current advance
            voucher.setPaidAmount(adv);

            // Advance should not adjust any GRN
            voucher.setAdvanceAdjustedAmount(BigDecimal.ZERO);
        }

        else {
            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Unknown voucher type"
            ));
        }

        List<PaymentVoucherMaterials> materialsList = dto.getMaterials().stream().map(m -> {
            PaymentVoucherMaterials material = new PaymentVoucherMaterials();
            material.setMaterialCode(m.getMaterialCode());
            material.setMaterialDescription(m.getMaterialDescription());
            material.setQuantity(m.getQuantity());
            material.setUnitPrice(m.getUnitPrice());
            material.setCurrency(m.getCurrency());
            material.setExchangeRate(m.getExchangeRate());
            material.setGst(m.getGst());
            material.setPaymentVoucher(voucher);
            return material;
        }).collect(Collectors.toList());

        voucher.setMaterialsList(materialsList);

        PaymentVoucher pv = paymentVoucherReposiotry.save(voucher);

        if (pv.getPaymentVoucherType().equalsIgnoreCase("Advance"))
            return "INV/" + pv.getId();

        return dto.getGrnNumber() + "/" + pv.getId();
    }



    public paymentVoucherRequestDto getVoucherByProcessNo(String processNo) {

        String[] parts = processNo.split("/");
        Long id = Long.parseLong(parts[parts.length - 1]);
        Optional<PaymentVoucher> entitys = paymentVoucherReposiotry.findById(id);


        PaymentVoucher entity=null;
        if(entitys.isPresent()){
            entity = entitys.get();
        }
        paymentVoucherRequestDto dto = new paymentVoucherRequestDto();
        dto.setPaymentVoucherDate(entity.getPaymentVoucherDate());
        dto.setPaymentVoucherIsFor(entity.getPaymentVoucherIsFor());
        dto.setPurchaseOrderId(entity.getPurchaseOrderId());
        dto.setGrnNumber(entity.getGrnNumber());
        dto.setServiceOrderDetails(entity.getServiceOrderDetails());
        dto.setPaymentVoucherType(entity.getPaymentVoucherType());
        dto.setVendorName(entity.getVendorName());
        dto.setVendorInvoiceNumber(entity.getVendorInvoiceNumber());
        dto.setVendorInvoiceDate(entity.getVendorInvoiceDate());
        dto.setCurrency(entity.getCurrency());
        dto.setExchangeRate(entity.getExchangeRate());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setPartialAmount(entity.getPartialAmount());
        dto.setAdvanceAmount(entity.getAdvanceAmount());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setPaymentVoucherNetAmount(entity.getPaymentVoucherNetAmount());
        dto.setTdsAmount(entity.getTdsAmount());

        // Map materials
        if (entity.getMaterialsList() != null) {
            dto.setMaterials(entity.getMaterialsList().stream().map(this::mapMaterial).collect(Collectors.toList()));
        }

        return dto;
    }

    private paymentVoucherMaterialRequestDto mapMaterial(PaymentVoucherMaterials m) {
        paymentVoucherMaterialRequestDto dto = new paymentVoucherMaterialRequestDto();
        dto.setMaterialCode(m.getMaterialCode());
        dto.setMaterialDescription(m.getMaterialDescription());
        dto.setQuantity(m.getQuantity());
        dto.setUnitPrice(m.getUnitPrice());
        dto.setCurrency(m.getCurrency());
        dto.setExchangeRate(m.getExchangeRate());
        dto.setGst(m.getGst());
        return dto;
    }





    @Override
    public List<PaymentVoucherReportDto> getPaymentVoucherReport(String startDate, String endDate) {


        List<LocalDateTime> range = CommonUtils.getDateRenge(startDate, endDate);
        LocalDateTime start = range.get(0);
        LocalDateTime end = range.get(1);


        List<PaymentVoucher> vouchers = paymentVoucherReposiotry
                .findByCreatedDateBetween(start, end);

        List<PaymentVoucherReportDto> reportList = new ArrayList<>();

        for (PaymentVoucher voucher : vouchers) {
            PaymentVoucherReportDto dto = new PaymentVoucherReportDto();


            String id = voucher.getGrnNumber()+"/"+ voucher.getId();
            dto.setPaymentVoucherNumber(id);
            dto.setPaymentVoucherDate(voucher.getPaymentVoucherDate());
            dto.setPaymentVoucherIsFor(voucher.getPaymentVoucherIsFor());
            dto.setGrnNumber(voucher.getGrnNumber());
            if(voucher.getPaymentVoucherIsFor().equalsIgnoreCase("Purchase Order")){
                String poId = "PO"+voucher.getPurchaseOrderId();
                dto.setPurchaseOrderId(poId);
            }else{
                String soId = "SO"+voucher.getSoId();
                dto.setSoId(voucher.getSoId());
            }

           // dto.setServiceOrderDetails(voucher.getServiceOrderDetails());
            dto.setPaymentVoucherType(voucher.getPaymentVoucherType());
            dto.setVendorName(voucher.getVendorName());
            dto.setVendorInvoiceNumber(voucher.getVendorInvoiceNumber());
            dto.setVendorInvoiceDate(voucher.getVendorInvoiceDate());
            dto.setCurrency(voucher.getCurrency());
            dto.setExchangeRate(voucher.getExchangeRate());
            dto.setRemarks(voucher.getRemarks());
            dto.setTotalAmount(voucher.getTotalAmount());
            dto.setPartialAmount(voucher.getPartialAmount());
            dto.setAdvanceAmount(voucher.getAdvanceAmount());
            dto.setPaidAmount(voucher.getPaidAmount());

            dto.setCreatedBy(voucher.getCreatedBy());
            dto.setCreatedDate(voucher.getCreatedDate());


            List<PaymentVoucherMaterialDto> materialDtos = voucher.getMaterialsList().stream()
                    .map(m -> {
                        PaymentVoucherMaterialDto mdto = new PaymentVoucherMaterialDto();
                        mdto.setMaterialCode(m.getMaterialCode());
                        mdto.setMaterialDescription(m.getMaterialDescription());
                        mdto.setQuantity(m.getQuantity());
                        mdto.setUnitPrice(m.getUnitPrice());
                        mdto.setCurrency(m.getCurrency());
                        mdto.setExchangeRate(m.getExchangeRate());
                        mdto.setGst(m.getGst());
                        return mdto;
                    }).toList();

            dto.setMaterials(materialDtos);
            reportList.add(dto);
        }

        return reportList;
    }






}
