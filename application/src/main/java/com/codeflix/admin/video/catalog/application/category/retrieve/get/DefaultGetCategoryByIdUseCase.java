package com.codeflix.admin.video.catalog.application.category.retrieve.get;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.video.catalog.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultGetCategoryByIdUseCase implements GetCategoryByIdUseCase {

	private final CategoryGateway categoryGateway;

	public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
		Objects.requireNonNull(categoryGateway);
		this.categoryGateway = categoryGateway;
	}

	@Override
	public CategoryOutput execute(String anId) {
		final var anCategoryId = CategoryID.from(anId);
		return this.categoryGateway
				.findById(anCategoryId)
				.map(CategoryOutput::from)
				.orElseThrow(notFound(anCategoryId));
	}

	private Supplier<NotFoundException> notFound(final CategoryID anId) {
		return () -> NotFoundException.with(
				Category.class,
				anId
		);
	}
}
