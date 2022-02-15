package com.e4motion.challenge.data.collector.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.common.security.JwtTokenProvider;
import com.e4motion.challenge.data.collector.domain.dto.CameraLoginDto;
import com.e4motion.challenge.data.collector.security.CustomUser;
import com.e4motion.common.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. Auth")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1")
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/camera/login")
    public Response login(@RequestBody CameraLoginDto loginDto) {
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
