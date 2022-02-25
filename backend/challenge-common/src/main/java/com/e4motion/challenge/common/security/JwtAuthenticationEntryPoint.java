package com.e4motion.challenge.common.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.e4motion.common.ResponseFail;
import com.e4motion.common.exception.customexception.UnauthorizedException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence(HttpServletRequest request, 
			HttpServletResponse response, 
			AuthenticationException authenticationException) throws IOException {

		// BadCredentialsException : invalid password when login.
		// InsufficientAuthenticationException : no token or invalid token.
		
		ResponseFail responseFail = new ResponseFail(UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
		if (authenticationException instanceof BadCredentialsException) {
			responseFail = new ResponseFail(UnauthorizedException.CODE, UnauthorizedException.INVALID_PASSWORD);
		}

    	response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8"); 
		
		PrintWriter out = response.getWriter(); 
		out.print(responseFail);
		out.flush();
		out.close();
   }
   
}
