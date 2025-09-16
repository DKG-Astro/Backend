package com.astro.service.InventoryModule;

import com.astro.dto.workflow.InventoryModule.AssetDisposalDto;
import com.astro.dto.workflow.InventoryModule.AssetMasterDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ogpAssetService {
    String saveAssetDisposalOgp(AssetDisposalDto request);
    public List<AssetDisposalDto> getPendingApprovals();
    public String approveOgpAssetDisposal(Integer disposalId);
    public String rejectOgpAssetDisposal(Integer disposalId);
}
