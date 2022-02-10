package com.e4motion.challenge.data.collector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.data.collector.domain.User;
import com.e4motion.challenge.data.collector.domain.UserRepository;
import com.e4motion.challenge.data.collector.dto.AuthRequest;
import com.e4motion.challenge.data.collector.dto.UserView;
import com.e4motion.challenge.data.collector.dto.UserViewMapper;
import com.e4motion.challenge.data.collector.security.JwtTokenUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth")
@RequiredArgsConstructor
@RestController 
@RequestMapping(path = "api/public")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserViewMapper userViewMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody AuthRequest request) {
        try {
        	logger.error("authenticate " + request.getUsername());
        	logger.error("authenticate " + request.getPassword());
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            logger.error("authenticate");
            User user = (User) authenticate.getPrincipal();
            logger.error("user");
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
                    .body(userViewMapper.toUserView(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("register")
    public UserView register(@RequestBody AuthRequest request) {
    	User user = new User(request.getUsername(), request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
        return userViewMapper.toUserView(user);
    }
    
}
