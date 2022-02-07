package com.e4motion.challenge.api.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.dto.LoginDto;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "¿Œ¡ı")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1/")
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        Set<Authority> authorities = userDetails.getAuthorities().stream()
        		.map(authority -> new Authority(authority.getAuthority()))
                .collect(Collectors.toSet());
        
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(new UserDto(userDetails.getUsername(), null, "aaa", "emailbbb", authorities));
    }
    
}
