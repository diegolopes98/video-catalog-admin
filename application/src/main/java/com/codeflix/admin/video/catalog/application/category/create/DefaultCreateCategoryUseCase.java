package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

	private final CategoryGateway categoryGateway;

	public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
		Objects.requireNonNull(categoryGateway);
		this.categoryGateway = categoryGateway;
	}

	@Override
	public Either<NotificationValidationHandler, CreateCategoryOutput> execute(CreateCategoryCommand anIn) {
		final var aName = anIn.name();
		final var aDescription = anIn.description();
		final var isActive = anIn.isActive();

		final var aCategory = Category.newCategory(aName, aDescription, isActive);
		final var notification = NotificationValidationHandler.create();
		aCategory.validate(notification);

		return notification.hasErrors() ? Left(notification) : create(aCategory);
	}

	private Either<NotificationValidationHandler, CreateCategoryOutput> create(final Category aCategory) {
		return Try(() -> this.categoryGateway.create(aCategory))
				.toEither()
				.bimap(
						NotificationValidationHandler::create,
						CreateCategoryOutput::from
				);
	}
}
