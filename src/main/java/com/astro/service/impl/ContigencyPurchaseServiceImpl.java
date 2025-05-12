package com.astro.service.impl;

import com.astro.constant.AppConstant;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseReportDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseRequestDto;
import com.astro.dto.workflow.ProcurementDtos.ContigencyPurchaseResponseDto;

import com.astro.dto.workflow.ProcurementDtos.CpMaterialResponseDto;
import com.astro.entity.ProcurementModule.ContigencyPurchase;

import com.astro.entity.ProcurementModule.CpMaterials;
import com.astro.exception.BusinessException;
import com.astro.exception.ErrorDetails;
import com.astro.exception.InvalidInputException;
import com.astro.repository.ProcurementModule.ContigencyPurchaseRepository;
import com.astro.service.ContigencyPurchaseService;
import com.astro.util.CommonUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ContigencyPurchaseServiceImpl implements ContigencyPurchaseService {
    @Autowired
    private ContigencyPurchaseRepository CPrepo;

    @Override
    public ContigencyPurchaseResponseDto createContigencyPurchase(ContigencyPurchaseRequestDto contigencyPurchaseDto){
            //,String uploadCopyOfInvoiceFileName) {

        // Check if the indentorId already exists
     /*   if (CPrepo.existsById(contigencyPurchaseDto.getContigencyId())) {
            ErrorDetails errorDetails = new ErrorDetails(400, 1, "Duplicate Contigency Purchase ID", "CP ID " + contigencyPurchaseDto.getContigencyId() + " already exists.");
            throw new InvalidInputException(errorDetails);
        }

      */
        Integer maxNumber = CPrepo.findMaxCpNumber();
        int nextNumber = (maxNumber == null) ? 1001 : maxNumber + 1;

        String cpId = "CP" + nextNumber;

       // String cpId = "CP" + System.currentTimeMillis();
        ModelMapper mapper = new ModelMapper();
        ContigencyPurchase cp = mapper.map(contigencyPurchaseDto, ContigencyPurchase.class);
        cp.setContigencyId(cpId);
        cp.setCpNumber(nextNumber);
        cp.setVendorsName(contigencyPurchaseDto.getVendorName());
        cp.setVendorsInvoiceNo(contigencyPurchaseDto.getVendorInvoiceNo());
        cp.setPredifinedPurchaseStatement(contigencyPurchaseDto.getPredifinedPurchaseStatement());
        cp.setRemarksForPurchase(contigencyPurchaseDto.getRemarksForPurchase());
        String Date = contigencyPurchaseDto.getDate();
        if (Date != null) {
            cp.setDate(CommonUtils.convertStringToDateObject(contigencyPurchaseDto.getDate()));
        }else{
            cp.setDate(null);
        }
        cp.setCreatedBy(contigencyPurchaseDto.getCreatedBy());

        List<CpMaterials> materials = contigencyPurchaseDto.getCpMaterials().stream().map(materialDto -> {
            CpMaterials material = mapper.map(materialDto, CpMaterials.class);
            // material.setContigencyId(cpId);
            material.setContigencyPurchase(cp);
            material.setGst(materialDto.getGst());
            return material;
        }).collect(Collectors.toList());

        cp.setCpMaterials(materials);
        CPrepo.save(cp);



        return mapToResponseDTO(cp);
    }



 /*   @Override
    public ContigencyPurchaseResponseDto updateContigencyPurchase(String contigencyId, ContigencyPurchaseRequestDto contigencyPurchaseDto){
            //,String uploadCopyOfInvoiceFileName) {
        ContigencyPurchase existingCP = CPrepo.findById(contigencyId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_VALIDATION,
                                " ContigencyPurchase not found for the provided contigency purchase ID.")
                ));
        existingCP.setVendorsName(contigencyPurchaseDto.getVendorsName());
        existingCP.setVendorsInvoiceNo(contigencyPurchaseDto.getVendorsInvoiceNo());
        String date = contigencyPurchaseDto.getDate();
        existingCP.setDate(CommonUtils.convertStringToDateObject(date));
        existingCP.setMaterialCode(contigencyPurchaseDto.getMaterialCode());
        existingCP.setMaterialDescription(contigencyPurchaseDto.getMaterialDescription());
        existingCP.setQuantity(contigencyPurchaseDto.getQuantity());
        existingCP.setUnitPrice(contigencyPurchaseDto.getUnitPrice());
        existingCP.setRemarksForPurchase(contigencyPurchaseDto.getRemarksForPurchase());
        existingCP.setAmountToBePaid(contigencyPurchaseDto.getAmountToBePaid());
        existingCP.setUploadCopyOfInvoiceFileName(contigencyPurchaseDto.getUploadCopyOfInvoice());
        existingCP.setFileType(contigencyPurchaseDto.getFileType());
        existingCP.setProjectName(contigencyPurchaseDto.getProjectName());
       // handleFileUpload(existingCP, contigencyPurchaseDto.getUploadCopyOfInvoice(),
              //  existingCP::setUploadCopyOfInvoice);
        existingCP.setPredifinedPurchaseStatement(contigencyPurchaseDto.getPredifinedPurchaseStatement());
        existingCP.setProjectDetail(contigencyPurchaseDto.getProjectDetail());
        existingCP.setUpdatedBy(contigencyPurchaseDto.getUpdatedBy());
        existingCP.setCreatedBy(contigencyPurchaseDto.getCreatedBy());
   CPrepo.save(existingCP);

        return mapToResponseDTO(existingCP);
    }*/

    @Override
    public ContigencyPurchaseResponseDto getContigencyPurchaseById(String contigencyId) {
        ContigencyPurchase contigencyPurchase = CPrepo.findById(contigencyId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "Contigency Purchase not found for the provided contigency purchase ID.")
                ));
        return mapToResponseDTO(contigencyPurchase);
    }

    @Override
    public List<ContigencyPurchaseResponseDto> getAllContigencyPurchase() {
        List<ContigencyPurchase> contigencyPurchases = CPrepo.findAll();
        return contigencyPurchases.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteContigencyPurchase(String contigencyId) {

      ContigencyPurchase contigencyPurchase=CPrepo.findById(contigencyId)
                .orElseThrow(() -> new BusinessException(
                        new ErrorDetails(
                                AppConstant.ERROR_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_CODE_RESOURCE,
                                AppConstant.ERROR_TYPE_RESOURCE,
                                "ContigencyPurchase not found for the provided ID."
                        )
                ));
        try {
            CPrepo.delete(contigencyPurchase);
        } catch (Exception ex) {
            throw new BusinessException(
                    new ErrorDetails(
                            AppConstant.INTER_SERVER_ERROR,
                            AppConstant.ERROR_TYPE_CODE_INTERNAL,
                            AppConstant.ERROR_TYPE_ERROR,
                            "An error occurred while deleting the  Contigency purchase."
                    ),
                    ex
            );
        }
    }

    @Override
    public List<ContigencyPurchaseReportDto> getContigencyPurchaseReport(String startDate, String endDate) {
        List<Object[]> results = CPrepo.findContigencyPurchaseReport(CommonUtils.convertStringToDateObject(startDate), CommonUtils.convertStringToDateObject(endDate));

        return results.stream().map(row -> {
            ContigencyPurchaseReportDto dto = new ContigencyPurchaseReportDto();
            dto.setId((String) row[0]);
            dto.setMaterial((String) row[1]);
            dto.setMaterialCategory((String) row[2]);
            dto.setMaterialSubCategory((String) row[3]);
            dto.setEndUser((String) row[4]);
            dto.setValue((BigDecimal) row[5]);
            dto.setPaidTo((String) row[6]);
            dto.setVendorName((String) row[7]);
            dto.setProject((String) row[8]);
            return dto;
        }).collect(Collectors.toList());

    }

    private ContigencyPurchaseResponseDto mapToResponseDTO(ContigencyPurchase contigencyPurchase) {
        ContigencyPurchaseResponseDto dto = new ContigencyPurchaseResponseDto();

        dto.setContigencyId(contigencyPurchase.getContigencyId());
        //  dto.setCpNumber(contigencyPurchase.getCpNumber());
        dto.setVendorName(contigencyPurchase.getVendorsName());
        dto.setVendorInvoiceNo(contigencyPurchase.getVendorsInvoiceNo());

        LocalDate date = contigencyPurchase.getDate();
        dto.setDate(CommonUtils.convertDateToString(date));

        dto.setRemarksForPurchase(contigencyPurchase.getRemarksForPurchase());
        //   dto.setAmountToBePaid(contigencyPurchase.getAmountToBePaid());
        dto.setUploadCopyOfInvoice(contigencyPurchase.getUploadCopyOfInvoiceFileName());
        dto.setFileType(contigencyPurchase.getFileType());
        dto.setPredifinedPurchaseStatement(contigencyPurchase.getPredifinedPurchaseStatement());
        dto.setProjectDetail(contigencyPurchase.getProjectDetail());
        dto.setProjectName(contigencyPurchase.getProjectName());
        dto.setUpdatedBy(contigencyPurchase.getUpdatedBy());
        dto.setCreatedBy(contigencyPurchase.getCreatedBy());
        dto.setUpdatedDate(contigencyPurchase.getUpdatedDate());
        dto.setCreatedDate(contigencyPurchase.getCreatedDate());

        dto.setPaymentTo(contigencyPurchase.getPaymentTo());
        dto.setPaymentToVendor(contigencyPurchase.getPaymentToVendor());
        dto.setPaymentToEmployee(contigencyPurchase.getPaymentToEmployee());
        // Map list of CpMaterials to CpMaterialsResponseDto
        List<CpMaterialResponseDto> materialsDtoList = contigencyPurchase.getCpMaterials().stream()
                .map(material -> {
                    CpMaterialResponseDto mDto = new CpMaterialResponseDto();
                    mDto.setMaterialCode(material.getMaterialCode());
                    mDto.setMaterialDescription(material.getMaterialDescription());
                    mDto.setQuantity(material.getQuantity());
                    mDto.setUnitPrice(material.getUnitPrice());
                    mDto.setUom(material.getUom());
                    mDto.setTotalPrice(material.getTotalPrice());
                    mDto.setBudgetCode(material.getBudgetCode());
                    mDto.setMaterialCategory(material.getMaterialCategory());
                    mDto.setMaterialSubCategory(material.getMaterialSubCategory());
                    mDto.setCurrency(material.getCurrency());
                    mDto.setGst(material.getGst());
                    return mDto;
                }).collect(Collectors.toList());

        dto.setCpMaterials(materialsDtoList);

        return dto;

    }

    public void handleFileUpload(ContigencyPurchase contigencyPurchase, MultipartFile file, Consumer<byte[]> fileSetter) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileSetter.accept(fileBytes);
            } catch (IOException e) {
                throw new InvalidInputException(new ErrorDetails(500, 3, "File Processing Error",
                        "Error while processing the uploaded file. Please try again."));
            }
        } else {
            fileSetter.accept(null);  // Handle gracefully if no file is uploaded
        }
    }
}
