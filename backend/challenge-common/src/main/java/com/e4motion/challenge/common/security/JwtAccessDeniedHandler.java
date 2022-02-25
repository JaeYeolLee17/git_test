package com.e4motion.challenge.common.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.e4motion.common.exception.customexception.InaccessibleException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.e4motion.common.ResponseFail;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	
    @Override
    public void handle(HttpServletRequest request, 
    		HttpServletResponse response, 
    		AccessDeniedException accessDeniedException) throws IOException {

		ResponseFail fail = new ResponseFail(InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
		
    	response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8"); 
		
		PrintWriter out = response.getWriter(); 
		out.print(fail.toString()); 
		out.flush();
		out.close();
    }
}
