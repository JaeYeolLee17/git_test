package com.e4motion.challenge.common.exception;

import com.e4motion.challenge.common.exception.customexception.*;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.response.ResponseFail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = UnauthorizedException.class)
	public Response handleUnauthorizedException(UnauthorizedException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(value = InaccessibleException.class)
	public Response handleInaccessibleException(InaccessibleException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = UserNotFoundException.class)
	public Response handleUserNotFoundException(UserNotFoundException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = CameraNotFoundException.class)
	public Response handleCameraNotFoundException(CameraNotFoundException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(value = UserDuplicateException.class)
	public Response handleUserDuplicateException(UserDuplicateException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(value = CameraDuplicateException.class)
	public Response handleCameraDuplicateException(CameraDuplicateException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = InvalidParamException.class)
	public Response handleInvalidParamException(InvalidParamException ex) {

		return new ResponseFail(ex.getCode(), ex.getMessage());
	}

}
