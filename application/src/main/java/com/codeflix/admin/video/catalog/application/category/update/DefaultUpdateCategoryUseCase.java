package com.codeflix.admin.video.catalog.application.category.update;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;


public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        Objects.requireNonNull(categoryGateway);
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<NotificationValidationHandler, UpdateCategoryOutput> execute(final UpdateCategoryCommand anIn) {
        final var anId = CategoryID.from(anIn.id());
        final var aName = anIn.name();
        final var aDescription = anIn.description();
        final var isActive = anIn.isActive();

        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notificationHandler = NotificationValidationHandler.create();
        aCategory
                .update(aName, aDescription, isActive)
                .validate(notificationHandler);

        return notificationHandler.hasErrors() ? Left(notificationHandler) : update(aCategory);
    }

    private Either<NotificationValidationHandler, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(
                        NotificationValidationHandler::create,
                        UpdateCategoryOutput::from
                );
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(anId.getValue()))
        );
    }
}
