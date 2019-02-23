package com.msh.restdemo.exception;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.msh.restdemo.controller.PackageController;

@ControllerAdvice
public class RestDemoExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(RestDemoExceptionHandler.class);
	
	@Value("${server.context}")
	String serverContext;

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		String correlationId = UUID.randomUUID().toString();
		log.error("APP_CORRLATION_ID: {}", correlationId, ex);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
				String.format("Error correlation Id: %s", correlationId));
	}

	@ExceptionHandler(ResponseStatusException.class)
	public final void handleUserNotFoundException(ResponseStatusException ex, WebRequest request) {
		throw ex;
	}

	@ExceptionHandler(FailureAfterPersistException.class)
	public final ResponseEntity<String> handleFailureAfterPersistException(FailureAfterPersistException ex,
			WebRequest request) {
		log.error("Failure after persistance: ", ex);
		String location = String.format("%s1/%s2/%d", serverContext, PackageController.REQUEST_PATH_API_PACKAGES, ex.getIdentifier());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", location);

		return ResponseEntity.accepted().headers(responseHeaders).body("");
	}
}
