package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.service.UploadService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "8. 데이터 업로드")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class UploadController {

    private final UploadService uploadService;

    @Operation(summary = "데이터 통계(15분 단위) 업로드", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/data/stats/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadDataStats(@RequestPart("file") MultipartFile file) throws Exception {

        uploadService.uploadDataStats(file);

    	return new Response();
    }
}
