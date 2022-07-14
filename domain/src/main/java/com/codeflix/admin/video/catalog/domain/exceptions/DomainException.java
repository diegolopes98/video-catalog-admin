package com.codeflix.admin.video.catalog.domain.exceptions;

import com.codeflix.admin.video.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

	private static final String MULTIPLE_ERRORS_MESSAGE = "the domain validation returned some errors";
	private final List<Error> errors;

	private DomainException(final String aMessage, final List<Error> anErrorList) {
		super(aMessage);
		this.errors = anErrorList;
	}

	public static DomainException with(final Error anError) {
		return new DomainException(anError.message(), List.of(anError));
	}

	public static DomainException with(final List<Error> anErrorList) {
		return new DomainException(MULTIPLE_ERRORS_MESSAGE, anErrorList);
	}

	public List<Error> getErrors() {
		return errors;
	}
}
