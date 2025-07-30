package com.astro.controller.ProcurementModuleController;

import com.astro.dto.workflow.ProcurementDtos.IndentDto.IndentCreationResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.IndentDto.MaterialDetailsResponseDTO;
import com.astro.dto.workflow.ProcurementDtos.TenderWithIndentResponseDTO;
import com.astro.entity.VendorMaster;
import com.astro.repository.VendorMasterRepository;
import com.astro.service.TenderRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TenderPdfCreator {
    @Autowired
    private TenderRequestService TRService;
    @Autowired
    private VendorMasterRepository vendorMasterRepository;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/tender-format/{tenderId}")
    public String generateTenderFormat(@PathVariable String tenderId, ModelMap model) throws IOException {
        TenderWithIndentResponseDTO tenderData = TRService.getTenderRequestById(tenderId);
        List<MaterialDetailsResponseDTO> allMaterials = new ArrayList<>();
        if (tenderData.getIndentResponseDTO() != null) {
            for (IndentCreationResponseDTO indent : tenderData.getIndentResponseDTO()) {
                if (indent.getMaterialDetails() != null) {
                    allMaterials.addAll(indent.getMaterialDetails());
                }
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate opening = LocalDate.parse(tenderData.getOpeningDate(), formatter);
            LocalDate closing = LocalDate.parse(tenderData.getClosingDate(), formatter);
            long days = Duration.between(opening.atStartOfDay(), closing.atStartOfDay()).toDays();
            tenderData.setValidityPeriod(days + " Days");
        } catch (Exception e) {
            tenderData.setValidityPeriod("____ Days");
        }


        VendorMaster vendorData = vendorMasterRepository.findByVendorId("V1012")
                .orElseGet(() -> {
                    VendorMaster emptyVendor = new VendorMaster();
                    emptyVendor.setVendorName("__________"); // fallback value
                    return emptyVendor;
                });

        model.addAttribute("vendor", vendorData); // ✅ always set vendor
        model.addAttribute("tender", tenderData);
        model.addAttribute("materials", allMaterials);
        return "Tender-Format";
    }

}
