package com.e4motion.challenge.api.controller;

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

import com.e4motion.challenge.api.dto.LoginDto;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.security.CustomUser;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.security.JwtTokenProvider;
import com.e4motion.challenge.common.response.Response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Tag(name = "1. Auth")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "v1")
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginDto loginDto) throws Exception {
    	UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        Set<AuthorityName> authorities = userDetails.getAuthorities().stream()
        		.map(authority -> AuthorityName.valueOf(authority.getAuthority()))
                .collect(Collectors.toSet());

        UserDto userDto = UserDto.builder()
        		.userId(userDetails.getUsername())
        		.username(userDetails.getCustomUsername())
        		.email(userDetails.getEmail())
        		.phone(userDetails.getPhone())
        		.authority(authorities.isEmpty() ? null : authorities.iterator().next())
        		.build();
        
        Response response = new Response("token", token);
        response.setData("user", userDto);
        
        return response;
    }
    
}
