package com.astro.service.impl;

import com.astro.dto.workflow.ContigencyPurchaseDto;
import com.astro.entity.ContigencyPurchase;
import com.astro.entity.TenderRequest;
import com.astro.repository.ContigencyPurchaseRepository;
import com.astro.service.ContigencyPurchaseService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContigencyPurchaseServiceImpl implements ContigencyPurchaseService {
    @Autowired
    private ContigencyPurchaseRepository CPrepo;

    @Override
    public ContigencyPurchase createTenderRequest(ContigencyPurchaseDto contigencyPurchaseDto) {
        ContigencyPurchase contigencyPurchase= new ContigencyPurchase();

        contigencyPurchase.setVendorsName(contigencyPurchaseDto.getVendorsName());
        contigencyPurchase.setVendorsInvoiceNo(contigencyPurchaseDto.getVendorsInvoiceNo());
        String Date = contigencyPurchaseDto.getDate();
        contigencyPurchase.setDate(CommonUtils.convertStringToDateObject(Date));
        contigencyPurchase.setMaterialCode(contigencyPurchaseDto.getMaterialCode());
        contigencyPurchase.setMaterialDescription(contigencyPurchaseDto.getMaterialDescription());
        contigencyPurchase.setQuantity(contigencyPurchaseDto.getQuantity());
        contigencyPurchase.setUnitPrice(contigencyPurchaseDto.getUnitPrice());
        contigencyPurchase.setRemarksForPurchase(contigencyPurchaseDto.getRemarksForPurchase());
        contigencyPurchase.setAmountToBePaid(contigencyPurchaseDto.getAmountToBePaid());
        contigencyPurchase.setUploadCopyOfInvoice(contigencyPurchaseDto.getUploadCopyOfInvoice());
        contigencyPurchase.setPredifinedPurchaseStatement(contigencyPurchaseDto.getPredifinedPurchaseStatement());
        contigencyPurchase.setProjectDetail(contigencyPurchaseDto.getProjectDetail());
        contigencyPurchase.setUpdateBy(contigencyPurchaseDto.getUpdatedBy());

        CPrepo.save(contigencyPurchase);

        return contigencyPurchase;
    }

    @Override
    public ContigencyPurchase updateTenderRequest(Long ContigencyId, ContigencyPurchaseDto contigencyPurchaseDto) {
        ContigencyPurchase existingCP = CPrepo.findById(ContigencyId)
                .orElseThrow(() -> new RuntimeException("Tender Request not found with id: " + ContigencyId));

        existingCP.setVendorsName(contigencyPurchaseDto.getVendorsName());
        existingCP.setVendorsInvoiceNo(contigencyPurchaseDto.getVendorsInvoiceNo());
        String Date = contigencyPurchaseDto.getDate();
        existingCP.setDate(CommonUtils.convertStringToDateObject(Date));
        existingCP.setMaterialCode(contigencyPurchaseDto.getMaterialCode());
        existingCP.setMaterialDescription(contigencyPurchaseDto.getMaterialDescription());
        existingCP.setQuantity(contigencyPurchaseDto.getQuantity());
        existingCP.setUnitPrice(contigencyPurchaseDto.getUnitPrice());
        existingCP.setRemarksForPurchase(contigencyPurchaseDto.getRemarksForPurchase());
        existingCP.setAmountToBePaid(contigencyPurchaseDto.getAmountToBePaid());
        existingCP.setUploadCopyOfInvoice(contigencyPurchaseDto.getUploadCopyOfInvoice());
        existingCP.setPredifinedPurchaseStatement(contigencyPurchaseDto.getPredifinedPurchaseStatement());
        existingCP.setProjectDetail(contigencyPurchaseDto.getProjectDetail());
        existingCP.setUpdateBy(contigencyPurchaseDto.getUpdatedBy());
   CPrepo.save(existingCP);

        return existingCP;
    }

    @Override
    public ContigencyPurchase getTenderRequestById(Long ContigencyId) {
        return CPrepo.findById(ContigencyId).orElseThrow(() -> new RuntimeException("Contigency Purchase not found!"));
    }

    @Override
    public List<ContigencyPurchase> getAllTenderRequests() {
        return CPrepo.findAll();
    }

    @Override
    public void deleteTenderRequest(Long ContigencyId) {
        CPrepo.deleteById(ContigencyId);
    }
}
