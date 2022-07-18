package com.codeflix.admin.video.catalog.domain.exceptions;

import com.codeflix.admin.video.catalog.domain.AggregateRoot;
import com.codeflix.admin.video.catalog.domain.Identifier;
import com.codeflix.admin.video.catalog.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {
	protected NotFoundException(
			String aMessage,
			List<Error> anErrorList
	) {
		super(aMessage, anErrorList);
	}

	public static NotFoundException with(
			final Class<? extends AggregateRoot<?>> anAggregate,
			final Identifier id
	) {
		final var anErrorMsg = "%s with ID %s was not found".formatted(
				anAggregate.getSimpleName(),
				id.getValue()
		);

		return new NotFoundException(anErrorMsg, Collections.emptyList());
	}
}
