package com.codeflix.admin.video.catalog.application.category.update;

import com.codeflix.admin.video.catalog.application.UseCase;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<NotificationValidationHandler, UpdateCategoryOutput>> {
}
