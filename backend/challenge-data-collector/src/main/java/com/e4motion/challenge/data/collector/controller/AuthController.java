package com.e4motion.challenge.data.collector.controller;

import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.security.JwtTokenProvider;
import com.e4motion.challenge.data.collector.dto.CameraLoginDto;
import com.e4motion.challenge.data.collector.security.CustomUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "1. 카메라 인증")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인", description = "접근 권한 : 전체")
    @PostMapping("/camera/login")
    public Response login(@Valid @RequestBody CameraLoginDto loginDto) throws Exception {

    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getCameraId(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        Response response = new Response("token", token);
        response.setData("settingsUpdated", userDetails.isSettingsUpdated());
        
        return response;
    }

}
