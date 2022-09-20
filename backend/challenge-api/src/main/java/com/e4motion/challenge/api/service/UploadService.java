package com.e4motion.challenge.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

public interface UploadService {

    void uploadCamera(MultipartFile file) throws IOException, ParseException;

    void uploadDataStats(MultipartFile[] files) throws IOException, ParseException;

    void uploadRegion(MultipartFile file) throws IOException, ParseException;

    void uploadIntersection(MultipartFile file) throws IOException, ParseException;

    void uploadLink(MultipartFile file) throws IOException, ParseException;
}
