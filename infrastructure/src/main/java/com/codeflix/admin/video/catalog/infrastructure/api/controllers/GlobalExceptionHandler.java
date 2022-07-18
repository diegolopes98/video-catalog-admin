package com.codeflix.admin.video.catalog.infrastructure.api.controllers;

import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = DomainException.class)
	public ResponseEntity<?> handleDomainException(final DomainException e) {
		return ResponseEntity.unprocessableEntity().body(ApiError.from(e));
	}

	record ApiError(String message, List<Error> errors) {
		static ApiError from(final DomainException e) {
			return new ApiError(e.getMessage(), e.getErrors());
		}
	}
}
