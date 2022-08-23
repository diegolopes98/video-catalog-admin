package com.codeflix.admin.video.catalog.domain.validation.handler;

import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
	@Override
	public ValidationHandler append(final Error anError) {
		throw DomainException.with(anError);
	}

	@Override
	public ValidationHandler append(final ValidationHandler aHandler) {
		throw DomainException.with(aHandler.getErrors());
	}

	@Override
	public <T> T validate(Validation<T> aValidation) {
		try {
			return aValidation.validate();
		} catch (final Exception e) {
			throw DomainException.with(new Error(e.getMessage()));
		}
	}

	@Override
	public List<Error> getErrors() {
		return null;
	}
}
