package com.astro.dto.workflow;

import com.astro.util.Base64ToByteArrayConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MaterialMasterRequestDto {

  //  private String materialCode;
    private String category;
    private String subCategory;
    private String description;
    private String uom;
    private String modeOfProcurement;
    private String endOfLife;
    private BigDecimal depreciationRate;
    private BigDecimal stockLevels;
   // private BigDecimal stockLevelsMax;
   // private BigDecimal reOrderLevel;
    private String conditionOfGoods;
    private String shelfLife;
    private BigDecimal estimatedPriceWithCcy;
    private String uploadImageFileName;
    private Boolean indigenousOrImported;
    private List<String> vendorNames;
    private Integer createdBy;
    private String updatedBy;

}
