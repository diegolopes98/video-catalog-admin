package com.codeflix.admin.video.catalog.application.category.update;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;


public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        Objects.requireNonNull(categoryGateway);
        this.categoryGateway = categoryGateway;
    }

    @Override
    public UpdateCategoryOutput execute(final UpdateCategoryCommand anIn) {
        final var anId = CategoryID.from(anIn.id());
        final var aName = anIn.name();
        final var aDescription = anIn.description();
        final var isActive = anIn.isActive();

        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));

        aCategory.update(aName, aDescription, isActive);

        return UpdateCategoryOutput.from(this.categoryGateway.update(aCategory));
    }

    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(
                Category.class,
                anId
        );
    }
}
