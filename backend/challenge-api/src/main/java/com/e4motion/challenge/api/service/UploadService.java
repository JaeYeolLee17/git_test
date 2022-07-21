package com.e4motion.challenge.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

public interface UploadService {

    void uploadDataStats(MultipartFile file) throws IOException, ParseException;
    
}
