package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.application.UseCase;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<NotificationValidationHandler, CreateCategoryOutput>> {
}
