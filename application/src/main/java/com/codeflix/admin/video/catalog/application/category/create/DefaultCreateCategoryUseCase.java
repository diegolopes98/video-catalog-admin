package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;

import java.util.Objects;

public non-sealed class DefaultCreateCategoryUseCase implements CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        Objects.requireNonNull(categoryGateway);
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CreateCategoryOutput execute(CreateCategoryCommand anIn) {
        final var aName = anIn.name();
        final var aDescription = anIn.description();
        final var isActive = anIn.isActive();

        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
    }

}
