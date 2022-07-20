package com.e4motion.challenge.api.controller;

import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.api.dto.LoginDto;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.security.CustomUser;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.security.JwtTokenProvider;
import com.e4motion.challenge.common.response.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Tag(name = "1. 사용자 인증")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v2")
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인", description = "접근 권한 : 전체")
    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginDto loginDto) throws Exception {

    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        Set<AuthorityName> authorities = userDetails.getAuthorities().stream()
        		.map(authority -> AuthorityName.valueOf(authority.getAuthority()))
                .collect(Collectors.toSet());

        UserDto userDto = UserDto.builder()
        		.username(userDetails.getUsername())
                .nickname(userDetails.getNickname())
        		.email(userDetails.getEmail())
        		.phone(userDetails.getPhone())
                .enabled(userDetails.isEnabled())
        		.authority(authorities.isEmpty() ? null : authorities.iterator().next())
        		.build();
        
        Response response = new Response("token", token);
        response.setData("user", userDto);
        
        return response;
    }
    
}
