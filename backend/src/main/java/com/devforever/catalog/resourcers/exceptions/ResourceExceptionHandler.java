package com.devforever.catalog.resourcers.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devforever.catalog.services.exceptions.DatabaseException;
import com.devforever.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice	//ESSA CLASSE INTERCEPTA EXCEÇÕES NA CAMADA DE RESOURCE
public class ResourceExceptionHandler {
	
	//EXCEÇÕES DO TIPO EntityNotFound QUANDO CAPTURADA, ESSE MÉTODO É EXECUTADO
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(ResourceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}