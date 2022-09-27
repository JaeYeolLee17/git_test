package com.e4motion.challenge.api.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DownloadService {

    ResponseEntity<byte[]> downloadRegion(HttpServletResponse response) throws IOException;

    ResponseEntity<byte[]> downloadIntersection(HttpServletResponse response) throws IOException;

    ResponseEntity<byte[]> downloadLink(HttpServletResponse response) throws IOException;

    ResponseEntity<byte[]> downloadCamera(HttpServletResponse response) throws IOException;
}
