package com.e4motion.challenge.data.collector.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.common.security.JwtTokenProvider;
import com.e4motion.challenge.data.collector.domain.dto.LoginDto;
import com.e4motion.challenge.data.collector.domain.dto.UserDto;
import com.e4motion.challenge.data.collector.domain.entity.Authority;
import com.e4motion.challenge.data.collector.security.CustomUser;
import com.e4motion.common.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. Authentication")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1")
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public Response login(@RequestBody LoginDto loginDto) {
    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        Set<Authority> authorities = userDetails.getAuthorities().stream()
        		.map(authority -> new Authority(authority.getAuthority()))
                .collect(Collectors.toSet());

        UserDto userDto = UserDto.builder()
        		.userId(userDetails.getUsername())
        		.username(userDetails.getCustomUsername())
        		.email(userDetails.getEmail())
        		.phone(userDetails.getPhone())
        		.authority(authorities.isEmpty() ? null : authorities.iterator().next().getAuthorityName())
        		.build();
        
        Response response = new Response("token", token);
        response.setData("user", userDto);
        
        return response;
    }
    
}
