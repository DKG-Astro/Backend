package com.astro.service.InventoryModule;

import java.time.LocalDateTime;
import java.util.List;

import com.astro.dto.workflow.InventoryModule.ogp.*;
import com.astro.util.CommonUtils;

public interface OgpService {
    public String saveOgp(OgpDto req);

    public OgpDto getOgpDtls(String processNo);

    public List<OgpReportDto> getOgpReport(String startDate, String endDate);

    public String savePoOgp(OgpPoDto request);

    public OgpPoResponseDto getPoOgp(String processNo);

    void approveOgp(GprApprovalDto req);

    void rejectOgp(GprApprovalDto req);

    public String saveOgpRejectedGi(OgpMasterRejectedGiDto req);
    public List<OgpMasterRejectedGiDto> getAwaitingRejectedGi();
    public void approveGiOgp(String ogpId);
public void rejectGiOgp(String ogpId);
    public List<OgpRejectedGiReportDto> getOgpRejectedGiReport(String startDate, String endDate);

    }
