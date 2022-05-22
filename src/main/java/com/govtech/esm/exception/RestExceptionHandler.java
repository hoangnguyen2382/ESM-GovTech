package com.govtech.esm.exception;

import com.govtech.esm.model.UserResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Handle Exception the for the request.
 * 
 * @author vanhoang.nguyen
 *
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

	/**
	 * Handle Business Exception
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ ServiceException.class })
	public ResponseEntity<Object> handleServiceException(HttpServletRequest request, HttpServletResponse response,
			Exception exception) {

		log.debug(exception.getMessage(), exception);

		UserResponse userResponse = new UserResponse();
		userResponse.setMessage(exception.getMessage());

		return ResponseEntity.badRequest().body(userResponse);
	}

	/**
	 * Handle Parameter Validation exception
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
	public ResponseEntity<Object> handleMethodArgument(HttpServletRequest request, HttpServletResponse response,
			Exception exception) {

		log.debug(exception.getMessage(), exception);

		UserResponse userResponse = new UserResponse();
		userResponse.setMessage("The input does not comply with API spec. Detail: " + exception.getMessage());

		return ResponseEntity.badRequest().body(userResponse);
	}

	/**
	 * Handle unexpected exception
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleException(HttpServletRequest request, HttpServletResponse response,
			Exception exception) {

		log.error(exception.getMessage(), exception);

		return ResponseEntity.internalServerError().body("Unexpected error in processing the request.");
	}
}
