package com.e4motion.challenge.common.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.e4motion.challenge.common.response.ResponseFail;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	
    @Override
    public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException {

		// AccessDeniedException : no access authority by role.

		ResponseFail fail = new ResponseFail(InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		PrintWriter out = response.getWriter();
		out.print(fail);
		out.flush();
		out.close();
    }
}
