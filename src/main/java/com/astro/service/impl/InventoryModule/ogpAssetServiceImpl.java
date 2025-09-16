package com.astro.service.impl.InventoryModule;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.InventoryModule.AssetDisposalDetailDto;
import com.astro.dto.workflow.InventoryModule.AssetDisposalDto;
import com.astro.entity.InventoryModule.OgpAssetDisposal;
import com.astro.entity.InventoryModule.OgpAssetDisposalDetail;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.repository.InventoryModule.OgpAssetDisposalDetailRepository;
import com.astro.repository.InventoryModule.OgpAssetDisposalRepository;
import com.astro.service.InventoryModule.ogpAssetService;
import com.astro.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ogpAssetServiceImpl implements ogpAssetService {
    @Autowired
    private OgpAssetDisposalRepository ogpAssetDisposalRepository;
    @Autowired
    private OgpAssetDisposalDetailRepository ogpAssetDisposalDetailRepository;


    @Override
    @Transactional
    public String saveAssetDisposalOgp(AssetDisposalDto request) {
        // 1. Create and save OgpAssetDisposal master record
        OgpAssetDisposal ogpMaster = new OgpAssetDisposal();
        String Date = request.getDisposalDate();
        ogpMaster.setDisposalDate(CommonUtils.convertStringToDateObject(Date));
        ogpMaster.setCustodianId(request.getCustodianId());
        ogpMaster.setCreatedBy(request.getCreatedBy());
        ogpMaster.setCreateDate(LocalDateTime.now());
        ogpMaster.setLocationId(request.getLocationId());
        ogpMaster.setStatus("Awaiting For Approval"); // or any default status
        ogpMaster.setAction(request.getAction());
        ogpMaster.setAuctionId(request.getAuctionId());
        ogpMaster.setAuctionDate(CommonUtils.convertStringToDateObject(request.getAuctionDate()));
        ogpMaster.setReservePrice(request.getReservePrice());
        ogpMaster.setAuctionPrice(request.getAuctionPrice());
        ogpMaster.setVendorName(request.getVendorName());

        // Save master to get ID
        ogpMaster = ogpAssetDisposalRepository.save(ogpMaster);

        // 2. Save details
        List<OgpAssetDisposalDetail> detailList = new ArrayList<>();
        if (request.getMaterialDtlList() != null) {
            for (AssetDisposalDetailDto detailDto : request.getMaterialDtlList()) {
                OgpAssetDisposalDetail detail = new OgpAssetDisposalDetail();
                detail.setDisposalOgpId(ogpMaster.getDisposalOgpId());
                detail.setAssetId(detailDto.getAssetId());
                detail.setAssetDesc(detailDto.getAssetDesc());
                detail.setDisposalQuantity(detailDto.getQuantity());
                detail.setDisposalCategory(detailDto.getDisposalCategory());
                detail.setDisposalMode(detailDto.getDisposalMode());
                detail.setSalesNoteFilename(detailDto.getSalesNoteFilename());
                detail.setOhqId(detailDto.getOhqId());
                detail.setLocatorId(detailDto.getLocatorId());
                detail.setBookValue(detailDto.getBookValue());
                detail.setDepriciationRate(detailDto.getDepriciationRate());
                detail.setUnitPrice(detailDto.getUnitPrice());
                detail.setCustodianId(detailDto.getCustodianId());
                detail.setPoValue(detailDto.getPoValue());

                detailList.add(detail);
            }
            ogpAssetDisposalDetailRepository.saveAll(detailList);
        }


        return "INV/" + ogpMaster.getDisposalOgpId();
    }

    public List<AssetDisposalDto> getPendingApprovals() {
        List<OgpAssetDisposal> pendingList = ogpAssetDisposalRepository.findByStatus("Awaiting For Approval");

        return pendingList.stream().map(disposal -> {
            AssetDisposalDto dto = new AssetDisposalDto();
            dto.setDisposalId(disposal.getDisposalOgpId());
            dto.setDisposalDate(disposal.getDisposalDate() != null ? disposal.getDisposalDate().toString() : null);
            dto.setCreatedBy(disposal.getCreatedBy());
            dto.setLocationId(disposal.getLocationId());
            dto.setCustodianId(disposal.getCustodianId());
            dto.setStatus(disposal.getStatus());
            dto.setAction(disposal.getAction());
            dto.setAuctionId(disposal.getAuctionId());
            dto.setAuctionDate(disposal.getAuctionDate() != null ? disposal.getAuctionDate().toString() : null);
            dto.setReservePrice(disposal.getReservePrice());
            dto.setAuctionPrice(disposal.getAuctionPrice());
            dto.setVendorName(disposal.getVendorName());

            // Fetch details for this disposal
            List<OgpAssetDisposalDetail> details = ogpAssetDisposalDetailRepository.findByDisposalOgpId(disposal.getDisposalOgpId());
            List<AssetDisposalDetailDto> detailDtos = details.stream().map(detail -> {
                AssetDisposalDetailDto dDto = new AssetDisposalDetailDto();
                dDto.setAssetId(detail.getAssetId());
                dDto.setAssetDesc(detail.getAssetDesc());
                dDto.setQuantity(detail.getDisposalQuantity());
                dDto.setDisposalCategory(detail.getDisposalCategory());
                dDto.setDisposalMode(detail.getDisposalMode());
                dDto.setSalesNoteFilename(detail.getSalesNoteFilename());
                dDto.setLocatorId(detail.getLocatorId());
                dDto.setOhqId(detail.getOhqId());
                dDto.setBookValue(detail.getBookValue());
                dDto.setDepriciationRate(detail.getDepriciationRate());
                dDto.setUnitPrice(detail.getUnitPrice());
                dDto.setCustodianId(detail.getCustodianId());
                dDto.setPoValue(detail.getPoValue());
                return dDto;
            }).collect(Collectors.toList());

            dto.setMaterialDtlList(detailDtos);
            return dto;
        }).collect(Collectors.toList());
    }

    public String approveOgpAssetDisposal(Integer disposalId) {
        OgpAssetDisposal disposal = ogpAssetDisposalRepository.findById(disposalId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Asset disposal not found for the provided process number.")));

        disposal.setStatus("APPROVED");
        ogpAssetDisposalRepository.save(disposal);
        return "Asset Disposal OGP approved successfully.";
    }

    public String rejectOgpAssetDisposal(Integer disposalId) {
        OgpAssetDisposal disposal = ogpAssetDisposalRepository.findById(disposalId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                "Asset disposal not found for the provided process number.")));

        disposal.setStatus("REJECTED");

        ogpAssetDisposalRepository.save(disposal);
        return "Asset Disposal OGP rejected successfully.";
    }
}
