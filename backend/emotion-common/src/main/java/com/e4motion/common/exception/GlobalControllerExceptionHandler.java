package com.e4motion.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.e4motion.common.Response;
import com.e4motion.common.ResponseFail;
import com.e4motion.common.exception.customexception.InaccessibleException;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.e4motion.common.exception.customexception.UserNotFoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value=UnauthorizedException.class)
	public Response handleUnauthorizedException(UnauthorizedException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(value=InaccessibleException.class)
	public Response handleInaccessessibleException(InaccessibleException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value=UserNotFoundException.class)
	public Response handleUserNotFoundException(UserNotFoundException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
}
