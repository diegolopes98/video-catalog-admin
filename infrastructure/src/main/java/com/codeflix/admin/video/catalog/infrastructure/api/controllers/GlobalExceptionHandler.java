package com.codeflix.admin.video.catalog.infrastructure.api.controllers;

import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<?> handleNotFound(final NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.from(e));
	}

	@ExceptionHandler(value = DomainException.class)
	public ResponseEntity<?> handleDomainException(final DomainException e) {
		return ResponseEntity.unprocessableEntity().body(ApiErrorResponse.from(e));
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleException(final Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.from(e));
	}
}
