package com.e4motion.challenge.common.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.e4motion.challenge.common.response.ResponseFail;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException exception) throws IOException {

		// InsufficientAuthenticationException : no token or invalid token.
		// BadCredentialsException : invalid password when login.
		// DisabledException : disabled user when login.

		ResponseFail responseFail;
		if (exception instanceof BadCredentialsException) {
			responseFail = new ResponseFail(UnauthorizedException.CODE, UnauthorizedException.INVALID_PASSWORD);
		} else if (exception instanceof DisabledException) {
			responseFail = new ResponseFail(UnauthorizedException.CODE, UnauthorizedException.DISABLED_USER);
		} else {
			responseFail = new ResponseFail(UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
		}

    	response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		PrintWriter out = response.getWriter(); 
		out.print(responseFail);
		out.flush();
		out.close();
   }
   
}
