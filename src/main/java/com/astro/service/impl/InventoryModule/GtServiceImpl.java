package com.astro.service.impl.InventoryModule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.GoodsTransfer.GtDtl;
import com.astro.dto.workflow.InventoryModule.GoodsTransfer.GtMasterDto;
import com.astro.entity.MaterialMaster;
import com.astro.entity.InventoryModule.GtDtlEntity;
import com.astro.entity.InventoryModule.GtMasterEntity;
import com.astro.entity.InventoryModule.OhqMasterConsumableEntity;
import com.astro.entity.InventoryModule.OhqMasterEntity;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.MaterialMasterRepository;
import com.astro.repository.InventoryModule.OhqMasterConsumableRepository;
import com.astro.repository.InventoryModule.GoodsTransfer.GtDtlRepository;
import com.astro.repository.InventoryModule.GoodsTransfer.GtMasterRepository;
import com.astro.repository.ohq.OhqMasterRepository;
import com.astro.service.InventoryModule.GtService;
import com.astro.util.CommonUtils;

@Service
public class GtServiceImpl implements GtService {
    @Autowired
    private GtMasterRepository gtmr;

    @Autowired
    private GtDtlRepository gtdr;

    @Autowired
    private OhqMasterConsumableRepository omcr;

    @Autowired
    private OhqMasterRepository ohqmr;

    @Override
    @Transactional
    public String createGt(GtMasterDto gtMasterDto) {
        GtMasterEntity gtMasterEntity = new GtMasterEntity();
        gtMasterEntity.setSenderLocationId(gtMasterDto.getSenderLocationId());
        gtMasterEntity.setReceiverLocationId(gtMasterDto.getReceiverLocationId());
        gtMasterEntity.setReceiverCustodianId(gtMasterDto.getReceiverCustodianId());
        gtMasterEntity.setSenderCustodianId(gtMasterDto.getSenderCustodianId());
        gtMasterEntity.setGtDate(CommonUtils.convertStringToDateObject(gtMasterDto.getGtDate()));
        gtMasterEntity.setCreatedBy(gtMasterDto.getCreatedBy());
        gtMasterEntity.setCreateDate(LocalDateTime.now());
        gtMasterEntity.setStatus("AWAITING APPROVAL");
        gtMasterEntity = gtmr.save(gtMasterEntity);

        for (GtDtl gtDtl : gtMasterDto.getMaterialDtlList()) {
            GtDtlEntity gtDtlEntity = new GtDtlEntity();
            gtDtlEntity.setGtId(gtMasterEntity.getId());
            gtDtlEntity.setAssetId(gtDtl.getAssetId());
            gtDtlEntity.setAssetDesc(gtDtl.getAssetDesc());
            gtDtlEntity.setMaterialCode(gtDtl.getMaterialCode());
            gtDtlEntity.setUnitPrice(gtDtl.getUnitPrice());
            gtDtlEntity.setDepriciationRate(gtDtl.getDepriciationRate());
            gtDtlEntity.setBookValue(gtDtl.getBookValue());
            gtDtlEntity.setMaterialDesc(gtDtl.getMaterialDesc());
            gtDtlEntity.setQuantity(gtDtl.getQuantity());
            gtDtlEntity.setReceiverLocatorId(gtDtl.getReceiverLocatorId());
            gtDtlEntity.setSenderLocatorId(gtDtl.getSenderLocatorId());
            gtdr.save(gtDtlEntity);
        }

        return "INV/" + gtMasterEntity.getId();

    }

    @Override
    @Transactional
    public void rejectGt(String gtId){
        Long id = Long.valueOf(gtId.split("/")[1]);
        GtMasterEntity gtMasterEntity = gtmr.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Goods Transfer not found for the provided process number.")));
        gtMasterEntity.setStatus("REJECTED");
        gtmr.save(gtMasterEntity);
    }

    @Override
    @Transactional
    public void approveGt(String gtId) {
        Long id = Long.valueOf(gtId.split("/")[1]);
        GtMasterEntity gtMasterEntity = gtmr.findById(id)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Goods Transfer not found for the provided process number.")));
        gtMasterEntity.setStatus("APPROVED");

        List<GtDtlEntity> gtDtlEntityList = gtdr.findByGtId(id);
        for (GtDtlEntity gtDtlEntity : gtDtlEntityList) {
            if (Objects.isNull(gtDtlEntity.getAssetId())) {
                addToConsumableOhq(gtDtlEntity, gtMasterEntity.getReceiverCustodianId());
                reduceFromConsumable(gtDtlEntity, gtMasterEntity);
            } else {
                addToCapitalOhq(gtDtlEntity, gtMasterEntity.getReceiverCustodianId());
                reduceFromCapital(gtDtlEntity, gtMasterEntity);
            }

        }

        gtmr.save(gtMasterEntity);
    }

    private void reduceFromConsumable(GtDtlEntity gtDtlEntity, GtMasterEntity gtMasterEntity){
        Optional<OhqMasterConsumableEntity> existingOhq = omcr.findByMaterialCodeAndLocatorIdAndCustodianId(
                gtDtlEntity.getMaterialCode(), gtDtlEntity.getSenderLocatorId(), gtMasterEntity.getSenderCustodianId().toString());
        if(existingOhq.isPresent()){
            OhqMasterConsumableEntity ohq = existingOhq.get();
            BigDecimal currentQty = ohq.getQuantity() != null ? ohq.getQuantity() : BigDecimal.ZERO;
            ohq.setQuantity(currentQty.subtract(gtDtlEntity.getQuantity()));
            omcr.save(ohq);
        }
    }

    private void reduceFromCapital(GtDtlEntity gtDtlEntity, GtMasterEntity gtMasterEntity){
        Optional<OhqMasterEntity> existingOhq = ohqmr.findByAssetIdAndLocatorIdAndCustodianId(
                gtDtlEntity.getAssetId(),
                gtDtlEntity.getSenderLocatorId(),
                gtMasterEntity.getSenderCustodianId().toString());
        if(existingOhq.isPresent()){
            OhqMasterEntity ohq = existingOhq.get();
            BigDecimal currentQty = ohq.getQuantity() != null ? ohq.getQuantity() : BigDecimal.ZERO;
            ohq.setQuantity(currentQty.subtract(gtDtlEntity.getQuantity()));
            ohqmr.save(ohq);
        }
    }

    private void addToConsumableOhq(GtDtlEntity gtDtlEntity, Integer custodianId) {
        Optional<OhqMasterConsumableEntity> existingOhq = omcr.findByMaterialCodeAndLocatorIdAndCustodianId(
                gtDtlEntity.getMaterialCode(), gtDtlEntity.getReceiverLocatorId(), custodianId.toString());
        OhqMasterConsumableEntity ohq;
        if (existingOhq.isPresent()) {
            ohq = existingOhq.get();
            System.out.println("PRESENT BC: " + ohq.getQuantity() + gtDtlEntity.getQuantity() );
            BigDecimal currentQty = ohq.getQuantity() != null ? ohq.getQuantity() : BigDecimal.ZERO;
            ohq.setQuantity(currentQty.add(gtDtlEntity.getQuantity()));
        }
        else {  
                System.out.println("NOT PRESENT BC: " + gtDtlEntity.getQuantity() );
                    ohq = new OhqMasterConsumableEntity();
                    ohq.setCustodianId(custodianId.toString());
                    ohq.setMaterialCode(gtDtlEntity.getMaterialCode());
                    ohq.setLocatorId(gtDtlEntity.getReceiverLocatorId());
                    ohq.setQuantity(gtDtlEntity.getQuantity());
                    ohq.setBookValue(gtDtlEntity.getBookValue());
                    ohq.setDepriciationRate(gtDtlEntity.getDepriciationRate());
                    ohq.setUnitPrice(gtDtlEntity.getUnitPrice());
                }
        omcr.save(ohq);
    }

    private void addToCapitalOhq(GtDtlEntity gtDtlEntity, Integer custodianId) {
         Optional<OhqMasterEntity> existingOhq = ohqmr.findByAssetIdAndLocatorIdAndCustodianId(
                gtDtlEntity.getAssetId(),
                gtDtlEntity.getReceiverLocatorId(),
                custodianId.toString());


         OhqMasterEntity ohq;
        if (existingOhq.isPresent()) {
            System.out.println("EXISTING OHQ PRESENT");
            ohq = existingOhq.get();
            BigDecimal currentQty = ohq.getQuantity() != null ? ohq.getQuantity() : BigDecimal.ZERO;
            ohq.setQuantity(currentQty.add(gtDtlEntity.getQuantity()));
        } else {
            ohq = new OhqMasterEntity();
            ohq.setCustodianId(custodianId.toString());
            ohq.setAssetId(gtDtlEntity.getAssetId());
            ohq.setLocatorId(gtDtlEntity.getReceiverLocatorId());
            ohq.setQuantity(gtDtlEntity.getQuantity());
            ohq.setBookValue(gtDtlEntity.getBookValue());
            ohq.setDepriciationRate(gtDtlEntity.getDepriciationRate());
            ohq.setUnitPrice(gtDtlEntity.getUnitPrice());
        }
        ohqmr.save(ohq);
    }

    @Override
    public List<GtMasterDto> getPendingGt(){
        List<GtMasterEntity> gtMasterEntityList = gtmr.findByStatus("AWAITING APPROVAL");
        List<GtMasterDto> gtMasterDtoList = new ArrayList<>();
        for (GtMasterEntity gtMasterEntity : gtMasterEntityList) {
            GtMasterDto gtMasterDto = new GtMasterDto();
            gtMasterDto.setId("INV/" + gtMasterEntity.getId());
            gtMasterDto.setGtDate(CommonUtils.convertDateToString(gtMasterEntity.getGtDate()));
            gtMasterDto.setSenderLocationId(gtMasterEntity.getSenderLocationId());
            gtMasterDto.setReceiverLocationId(gtMasterEntity.getReceiverLocationId());
            gtMasterDto.setSenderCustodianId(gtMasterEntity.getSenderCustodianId());
            gtMasterDto.setReceiverCustodianId(gtMasterEntity.getReceiverCustodianId());
            List<GtDtlEntity> gtDtlEntityList = gtdr.findByGtId(gtMasterEntity.getId());
            List<GtDtl> gtDtlList = new ArrayList<>();
            for (GtDtlEntity gtDtlEntity : gtDtlEntityList) {
                GtDtl gtDtl = new GtDtl();
                gtDtl.setAssetId(gtDtlEntity.getAssetId());
                gtDtl.setAssetDesc(gtDtlEntity.getAssetDesc());
                gtDtl.setMaterialCode(gtDtlEntity.getMaterialCode());
                gtDtl.setMaterialDesc(gtDtlEntity.getMaterialDesc());
                gtDtl.setQuantity(gtDtlEntity.getQuantity());
                gtDtl.setReceiverLocatorId(gtDtlEntity.getReceiverLocatorId());
                gtDtl.setSenderLocatorId(gtDtlEntity.getSenderLocatorId());
                gtDtl.setUnitPrice(gtDtlEntity.getUnitPrice());
                gtDtl.setDepriciationRate(gtDtlEntity.getDepriciationRate());
                gtDtl.setBookValue(gtDtlEntity.getBookValue());
                gtDtlList.add(gtDtl);
            }
            gtMasterDto.setMaterialDtlList(gtDtlList);
            gtMasterDtoList.add(gtMasterDto);

        }
        return gtMasterDtoList;
    }
}
