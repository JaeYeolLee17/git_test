package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.TsiDto;
import com.e4motion.challenge.api.service.TsiSender;
import com.e4motion.challenge.api.service.TsiService;
import com.e4motion.challenge.common.domain.TsiFilterBy;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = " 7. 교통신호정보")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class TsiController {

    private final TsiService tsiService;
    private final TsiSender tsiSender;

    @Operation(summary = "교통신호정보", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	@GetMapping("/tsi")
    public Response get(@RequestParam(value = "filterBy", required = false) TsiFilterBy filterBy,
                        @RequestParam(value = "filterValue", required = false) String filterValue) {

        List<TsiDto> tsiDtos = tsiService.getList(filterBy, filterValue);

        return new Response("tsi", tsiDtos);
    }

    //@Operation(summary = "교통신호정보 수신 등록", description = "접근 권한 : 최고관리자, 운영자, 사용자")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @Operation(summary = "교통신호정보 수신 등록")
    @GetMapping(value = "/tsi/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam(value = "filterBy", required = false) TsiFilterBy filterBy,
                                @RequestParam(value = "filterValue", required = false) String filterValue) {

        return tsiSender.subscribe(filterBy, filterValue);
    }

    @Operation(summary = "교통신호정보 수신 등록 - 인증 토큰 필요")
    @GetMapping(value = "/tsi/subscribe-with-token")
    public SseEmitter subscribeWithToken(@RequestParam(value = "filterBy", required = false) TsiFilterBy filterBy,
                                       @RequestParam(value = "filterValue", required = false) String filterValue) {

        return tsiSender.subscribe(filterBy, filterValue);
    }
}
