package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.PaymentVoucherMaterialDto;
import com.astro.dto.workflow.PaymentVoucherReportDto;
import com.astro.dto.workflow.paymentVoucherMaterialRequestDto;
import com.astro.dto.workflow.paymentVoucherRequestDto;
import com.astro.entity.InventoryModule.GrnConsumableDtlEntity;
import com.astro.entity.InventoryModule.GrnMaterialDtlEntity;
import com.astro.entity.PaymentVoucher;
import com.astro.entity.PaymentVoucherGrn;
import com.astro.entity.PaymentVoucherMaterials;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.PaymentVoucherMaterialsRepository;
import com.astro.repository.InventoryModule.PaymentVoucherReposiotry;
import com.astro.repository.InventoryModule.grn.GrnConsumableDtlRepository;
import com.astro.repository.InventoryModule.grn.GrnMaterialDtlRepository;
import com.astro.repository.PaymentVoucherGrnRepositoy;
import com.astro.service.PaymentVoucherService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentVoucherServiceImpl implements PaymentVoucherService {
    @Autowired
    private PaymentVoucherReposiotry paymentVoucherReposiotry;
    @Autowired
    private PaymentVoucherMaterialsRepository paymentVoucherMaterialsRepository;
    @Autowired
    private GrnMaterialDtlRepository getGrnMaterialRepository;
    @Autowired
    private GrnConsumableDtlRepository grnConsumableDtlRepository;
    @Autowired
    private PaymentVoucherGrnRepositoy paymentVoucherGrnRepositoy;



    @Override
    @Transactional
    public String createPaymentVoucher(paymentVoucherRequestDto dto) {


        if ("Advance".equalsIgnoreCase(dto.getPaymentVoucherType()) &&
                dto.getGrnNumbers() != null && !dto.getGrnNumbers().isEmpty()) {

            throw new BusinessException(new ErrorDetails(
                    AppConstant.ERROR_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_CODE_RESOURCE,
                    AppConstant.ERROR_TYPE_RESOURCE,
                    "Items are received. Advance cannot be raised now."
            ));
        }

        if (("Partial".equalsIgnoreCase(dto.getPaymentVoucherType()) ||
                "Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) &&
                (dto.getGrnNumbers() == null || dto.getGrnNumbers().isEmpty())) {

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
       // voucher.setGrnNumber(dto.getGrnNumber());
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
                paymentVoucherReposiotry.getTotalAdvanceAppliedForGrns(dto.getGrnNumbers());
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

     //   BigDecimal effectiveAmount = grnValue.subtract(totalAdvanceAppliedForGrn).subtract(advanceAppliedToThisGRN);

        BigDecimal effectiveAmount;

        if ("Full Payment".equalsIgnoreCase(dto.getPaymentVoucherType())) {
            // GRN fetch API already gives remaining balance
            effectiveAmount = grnValue.subtract(totalAdvanceAppliedForGrn);
        } else {
            // Partial payment → deduct advance now
            effectiveAmount = grnValue
                    .subtract(totalAdvanceAppliedForGrn)
                    .subtract(advanceAppliedToThisGRN);
        }
        if (effectiveAmount.compareTo(BigDecimal.ZERO) < 0)
            effectiveAmount = BigDecimal.ZERO;

     //  BigDecimal totalPartialPaid = paymentVoucherReposiotry.getTotalPartialPaidForGrns(dto.getGrnNumbers());
        BigDecimal totalPartialPaid = paymentVoucherGrnRepositoy.getTotalPaidForGrns(dto.getGrnNumbers());

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
          //  voucher.setPaidAmount(partial);
            BigDecimal tds = dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO;

            BigDecimal net = partial.subtract(tds);
            if (net.compareTo(BigDecimal.ZERO) < 0)
                net = BigDecimal.ZERO;

            voucher.setPaidAmount(partial);
            voucher.setTdsAmount(tds);
            voucher.setPaymentVoucherNetAmount(net);
        }
/*
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
        }*/

        else if ("Full Payment".equalsIgnoreCase(type)) {

            // Remaining is already final payable amount
            BigDecimal payable = effectiveAmount.subtract(totalPartialPaid);

            if (payable.compareTo(BigDecimal.ZERO) < 0)
                payable = BigDecimal.ZERO;

            BigDecimal tds = dto.getTdsAmount() != null
                    ? dto.getTdsAmount()
                    : BigDecimal.ZERO;

            BigDecimal net = payable.subtract(tds);
            BigDecimal nett = net.subtract(advanceAppliedToThisGRN);
            if (net.compareTo(BigDecimal.ZERO) < 0)
                net = BigDecimal.ZERO;
            voucher.setPaidAmount(payable.subtract(advanceAppliedToThisGRN));
            voucher.setTdsAmount(tds);
            voucher.setPaymentVoucherNetAmount(nett);
        }



        else if ("Advance".equalsIgnoreCase(type)) {

            BigDecimal adv = dto.getAdvanceAmount() != null ? dto.getAdvanceAmount() : BigDecimal.ZERO;

            // Save only current advance
          //  voucher.setPaidAmount(adv);



            BigDecimal tds = dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO;

            BigDecimal net = adv.subtract(tds);
            if (net.compareTo(BigDecimal.ZERO) < 0)
                net = BigDecimal.ZERO;

            // Save Gross Advance
            voucher.setPaidAmount(adv);

            // Save TDS & Net Advance Amount
            voucher.setTdsAmount(tds);
            voucher.setPaymentVoucherNetAmount(net);

            // Advance should not adjust any GRN
            voucher.setAdvanceAdjustedAmount(BigDecimal.ZERO);
        }

        BigDecimal distributableAmount = BigDecimal.ZERO;

        if ("Partial".equalsIgnoreCase(type) || "Full Payment".equalsIgnoreCase(type)) {
            distributableAmount = voucher.getPaidAmount() != null
                    ? voucher.getPaidAmount()
                    : BigDecimal.ZERO;
        } else if ("Advance".equalsIgnoreCase(type)) {

            BigDecimal adv = dto.getAdvanceAmount() != null ? dto.getAdvanceAmount() : BigDecimal.ZERO;

            // Save only current advance
            //  voucher.setPaidAmount(adv);



            BigDecimal tds = dto.getTdsAmount() != null ? dto.getTdsAmount() : BigDecimal.ZERO;

            BigDecimal net = adv.subtract(tds);
            if (net.compareTo(BigDecimal.ZERO) < 0)
                net = BigDecimal.ZERO;

            // Save Gross Advance
            voucher.setPaidAmount(adv);


            voucher.setTdsAmount(tds);
            voucher.setPaymentVoucherNetAmount(net);


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


        Map<String, BigDecimal> grnValueMap =
                getGrnValueMapFromDto(dto.getMaterials());
        // Example: {INV1147/131=90, INV1147/163=51.75}

        // REPLACE: GRN-wise proportional distribution
        BigDecimal totalGrnValue = grnValueMap.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Validate GRN totals match voucher total
        BigDecimal totalFromMaterials = grnValueMap.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        List<PaymentVoucherGrn> grnMappings = new ArrayList<>();

        BigDecimal allocatedSoFar = BigDecimal.ZERO;
        int index = 0;
        int size = dto.getGrnNumbers().size();

        for (String grn : dto.getGrnNumbers()) {

            PaymentVoucherGrn g = new PaymentVoucherGrn();
            g.setGrnNumber(grn);
            g.setPaymentVouchertype(voucher.getPaymentVoucherType());
            g.setPaymentVoucher(voucher);


            BigDecimal thisGrnValue = grnValueMap.getOrDefault(grn, BigDecimal.ZERO);


            if (distributableAmount.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal grnPaid;

                boolean isSingleGrn = size == 1;
                boolean isFullPayment = "Full Payment".equalsIgnoreCase(type);
                boolean isPartialPayment = "Partial".equalsIgnoreCase(type);


                if (isSingleGrn && (isFullPayment || isPartialPayment)) {

                    grnPaid = distributableAmount;   // full amount to single GRN
                }

                else if (totalGrnValue.compareTo(BigDecimal.ZERO) > 0) {

                    BigDecimal ratio = thisGrnValue
                            .divide(totalGrnValue, 6, RoundingMode.HALF_UP);


                    if (index == size - 1) {
                        grnPaid = distributableAmount.subtract(allocatedSoFar);
                    } else {
                        grnPaid = distributableAmount
                                .multiply(ratio)
                                .setScale(2, RoundingMode.HALF_UP);
                        allocatedSoFar = allocatedSoFar.add(grnPaid);
                    }
                }
                else {
                    grnPaid = BigDecimal.ZERO;
                }

                g.setPaidAmount(grnPaid);

                if (voucher.getAdvanceAdjustedAmount() != null
                        && voucher.getAdvanceAdjustedAmount().compareTo(BigDecimal.ZERO) > 0) {

                    BigDecimal grnAdvance;

                  //  int size = dto.getGrnNumbers().size();
                   // boolean isSingleGrn = size == 1;

                    // SINGLE GRN → full advance to that GRN
                    if (isSingleGrn) {
                        grnAdvance = voucher.getAdvanceAdjustedAmount();
                    }

                    else if (totalGrnValue.compareTo(BigDecimal.ZERO) > 0) {

                        BigDecimal advRatio = thisGrnValue
                                .divide(totalGrnValue, 6, RoundingMode.HALF_UP);

                        grnAdvance = voucher.getAdvanceAdjustedAmount()
                                .multiply(advRatio)
                                .setScale(2, RoundingMode.HALF_UP);
                    }
                    else {
                        grnAdvance = BigDecimal.ZERO;
                    }

                    g.setAdvanceAdjusted(grnAdvance);
                }

            }

            grnMappings.add(g);
            index++;
        }



        voucher.setGrnList(grnMappings);


        PaymentVoucher pv = paymentVoucherReposiotry.save(voucher);

        if (pv.getPaymentVoucherType().equalsIgnoreCase("Advance"))
            return "INV/" + pv.getId();

        return "INV/" + pv.getId();
    }

    private Map<String, BigDecimal> getGrnValueMapFromDto(
            List<paymentVoucherMaterialRequestDto> materials) {

        Map<String, BigDecimal> grnValueMap = new HashMap<>();

        for (paymentVoucherMaterialRequestDto m : materials) {

            if (m.getGrnNumber() == null) continue;

            BigDecimal qty = m.getQuantity() != null ? m.getQuantity() : BigDecimal.ZERO;
            BigDecimal price = m.getUnitPrice() != null ? m.getUnitPrice() : BigDecimal.ZERO;

            BigDecimal amount = qty.multiply(price);

            grnValueMap.merge(m.getGrnNumber(), amount, BigDecimal::add);
        }

        return grnValueMap;
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
     //   dto.setGrnNumber(entity.getGrnNumber());
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
        dto.setAdvanceAdjustedAmount(entity.getAdvanceAdjustedAmount());
        dto.setPaidAmount(entity.getPaidAmount());
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
            dto.setTdsAmount(voucher.getTdsAmount());
            dto.setAdvanceAdjustedAmount(voucher.getAdvanceAdjustedAmount());
            dto.setPaymentVoucherNetAmount(voucher.getPaymentVoucherNetAmount());
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
