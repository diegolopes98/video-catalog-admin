package com.codeflix.admin.video.catalog.application.category.delete;

import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;

import java.util.Objects;

public non-sealed class DefaultDeleteCategoryUseCase implements DeleteCategoryUseCase {

	private final CategoryGateway categoryGateway;

	public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
		Objects.requireNonNull(categoryGateway);
		this.categoryGateway = categoryGateway;
	}

	@Override
	public void execute(String anId) {
		this.categoryGateway.deleteById(CategoryID.from(anId));
	}
}
