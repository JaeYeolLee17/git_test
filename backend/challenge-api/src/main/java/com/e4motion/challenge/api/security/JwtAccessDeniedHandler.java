package com.e4motion.challenge.api.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.e4motion.common.ResponseFail;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	private final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);
	
	private static final String CODE = "INACCCESSIBLE_DATA";
	
    @Override
    public void handle(HttpServletRequest request, 
    		HttpServletResponse response, 
    		AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ResponseFail fail = new ResponseFail(CODE, "Access denied");

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(fail); 
		logger.debug("JwtAccessDeniedHandler : response {}", json);
		
    	response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8"); 
		PrintWriter out = response.getWriter(); 
		out.print(json); 
		out.flush();
		out.close();
    }
}
