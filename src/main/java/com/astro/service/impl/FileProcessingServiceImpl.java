package com.astro.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.astro.constant.AppConstant;
import com.astro.exception.ErrorDetails;
import com.astro.exception.FilesNotFoundException;
import com.astro.service.FileProcessingService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    @Value("${filePath}")
    private String basePath;

    final List<String> FILE_TYPE_LIST = Arrays.asList("Indent", "Tender", "CP");

    @Override
    public List<String> fileList() {
        File dir = new File(basePath);
        File[] files = dir.listFiles();
    
        return files != null ? Arrays.stream(files).map(i -> i.getName()).collect(Collectors.toList()) : null;
    }

    @Override
    public String uploadFile(String fileType, MultipartFile multipartFile) {
        if(!FILE_TYPE_LIST.contains(fileType)){
            throw new FilesNotFoundException(new ErrorDetails(AppConstant.INVALID_FILE_TYPE, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid File type."));
        }

        String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

        //multipartFile.getOriginalFilename().replace(multipartFile.getOriginalFilename(), String.valueOf(System.currentTimeMillis()).concat("_" + multipartFile.getOriginalFilename()));
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FilesNotFoundException(new ErrorDetails(AppConstant.INVALID_FILE_TYPE, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                        AppConstant.ERROR_TYPE_VALIDATION, "Invalid File type."));
            }

             Path path = Path.of(basePath+ fileType + "//" + fileName);
             Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){
            throw new FilesNotFoundException(new ErrorDetails(AppConstant.FILE_UPLOAD_ERROR, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "File upload error."));
        }
        return fileName;
    }   

    @Override
    public Resource downloadFile(String fileType, String fileName) {

        if(!FILE_TYPE_LIST.contains(fileType)){
            throw new FilesNotFoundException(new ErrorDetails(AppConstant.INVALID_FILE_TYPE, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "Invalid File type."));
        }

        File dir = new File(basePath+ fileType + "//" +fileName);
        try{
        if(dir.exists()){
            Resource resource = new UrlResource(dir.toURI());
            return resource;
        }else{
            throw new FilesNotFoundException(new ErrorDetails(AppConstant.FILE_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "File not found."));
        }
        }catch (Exception e){
            throw new FilesNotFoundException(new ErrorDetails(AppConstant.FILE_NOT_FOUND, AppConstant.ERROR_TYPE_CODE_VALIDATION,
                    AppConstant.ERROR_TYPE_VALIDATION, "File not found."));
        }
    }
    
}