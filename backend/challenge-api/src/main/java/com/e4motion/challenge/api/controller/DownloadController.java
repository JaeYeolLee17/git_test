package com.e4motion.challenge.api.controller;


import com.e4motion.challenge.api.service.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Tag(name = "10. 데이터 다운로드")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v2")
public class DownloadController {

    private final DownloadService downloadService;

    @Operation(summary = "구역 정보 다운로드", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping(value = "/region/file", produces = "text/csv")
    public ResponseEntity<byte[]> downloadRegion(HttpServletResponse response) throws Exception {

        return downloadService.downloadRegion(response);
    }

    @Operation(summary = "교차로 정보 다운로드", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping(value = "/intersection/file", produces = "text/csv")
    public ResponseEntity<byte[]> downloadIntersection(HttpServletResponse response) throws Exception {

        return downloadService.downloadIntersection(response);
    }

    @Operation(summary = "링크 정보 다운로드", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping(value = "/link/file", produces = "text/csv")
    public ResponseEntity<byte[]> downloadLink(HttpServletResponse response) throws Exception {

        return downloadService.downloadLink(response);
    }

    @Operation(summary = "링크 정보 생성", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping(value = "/link/file/generate", produces = "text/csv")
    public ResponseEntity<byte[]> generateLinkByIntersection(HttpServletResponse response) throws Exception {

        return downloadService.generateLinkByIntersection(response);
    }

    @Operation(summary = "카메라 정보 다운로드", description = "접근 권한 : 최고관리자, 운영자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping(value = "/camera/file", produces = "text/csv")
    public ResponseEntity<byte[]> downloadCamera(HttpServletResponse response) throws Exception {

        return downloadService.downloadCamera(response);
    }
}
