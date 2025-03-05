package com.astro.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileProcessingService {

    public List<String> fileList();
    public String uploadFile(String fileType, MultipartFile multipartFile);
    public Resource downloadFile(String fileType, String fileName);


}
