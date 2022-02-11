package com.e4motion.challenge.api.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.e4motion.common.ResponseFail;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, 
			HttpServletResponse response, 
			AuthenticationException authenticationException) throws IOException {

		// BadCredentialsException : invalid password when login.
		// InsufficientAuthenticationException : no token or invalid token.
		
		ResponseFail fail = new ResponseFail(UnauthorizedException.CODE, "Unauthorized token");
		if (authenticationException instanceof BadCredentialsException) {
			fail = new ResponseFail(UnauthorizedException.CODE, "Invalid password");
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(fail); 
		logger.debug("AuthenticationEntryPoint : response {}", json);
		
    	response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8"); 
		PrintWriter out = response.getWriter(); 
		out.print(json); 
		out.flush();
		out.close();
   }
   
}
