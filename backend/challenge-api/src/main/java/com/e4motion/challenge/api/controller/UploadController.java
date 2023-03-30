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

@Tag(name = " 9. 데이터 업로드")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class UploadController {

    private final UploadService uploadService;

    @Operation(summary = "구역 정보", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/region/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadRegion(@RequestPart("file") MultipartFile file) throws Exception {

        uploadService.uploadRegion(file);

        return new Response();
    }

    @Operation(summary = "교차로 정보", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/intersection/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadIntersection(@RequestPart("file") MultipartFile file) throws Exception {

        uploadService.uploadIntersection(file);

        return new Response();
    }

    @Operation(summary = "링크 정보", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/link/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadLink(@RequestPart("file") MultipartFile file) throws Exception {

        uploadService.uploadLink(file);

        return new Response();
    }

    @Operation(summary = "카메라 정보", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/camera/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadCamera(@RequestPart("file") MultipartFile file) throws Exception {

        uploadService.uploadCamera(file);

        return new Response();
    }

    @Operation(summary = "데이터 통계(15분 단위)", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/data/stats/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadDataStats(@RequestPart("files") MultipartFile[] files) throws Exception {

        uploadService.uploadDataStats(files);

        return new Response();
    }

    @Operation(summary = "교차로, 카메라 등록", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(value = "/data/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadDataStats(@RequestPart("file") MultipartFile file) throws Exception {

        uploadService.uploadData(file);

        return new Response();
    }
}
