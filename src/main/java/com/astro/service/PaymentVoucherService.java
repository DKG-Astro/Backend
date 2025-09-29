package com.astro.service;

import com.astro.dto.workflow.paymentVoucherRequestDto;
import com.astro.entity.PaymentVoucher;
import org.springframework.stereotype.Service;

@Service
public interface PaymentVoucherService {

    public String createPaymentVoucher(paymentVoucherRequestDto dto);

    public paymentVoucherRequestDto getVoucherByProcessNo(String processNo);
}
