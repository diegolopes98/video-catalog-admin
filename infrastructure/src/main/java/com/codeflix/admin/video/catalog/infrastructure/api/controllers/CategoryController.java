package com.codeflix.admin.video.catalog.infrastructure.api.controllers;

import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryCommand;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import com.codeflix.admin.video.catalog.infrastructure.api.CategoryAPI;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

	private CreateCategoryUseCase createCategoryUseCase;

	public CategoryController(CreateCategoryUseCase createCategoryUseCase) {
		this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
	}

	@Override
	public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
		final var aCommand = CreateCategoryCommand.with(
				input.name(),
				input.description(),
				input.active() != null ? input.active() : true
		);

		final Function<NotificationValidationHandler, ResponseEntity<?>> onError = notification ->
				ResponseEntity.unprocessableEntity().body(notification);

		final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity
				.created(URI.create("/categories/" + output.id())).body(output);

		return this.createCategoryUseCase.execute(aCommand)
				.fold(onError, onSuccess);
	}

	@Override
	public Pagination<?> listCategories(
			String search,
			int page,
			int perPage,
			String sort,
			String dir
	) {
		return null;
	}
}
