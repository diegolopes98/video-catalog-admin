package com.codeflix.admin.video.catalog.infrastructure.api.controllers;

import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryCommand;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.admin.video.catalog.application.category.update.UpdateCategoryCommand;
import com.codeflix.admin.video.catalog.application.category.update.UpdateCategoryUseCase;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.infrastructure.api.CategoryAPI;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CategoryListResponse;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CategoryController implements CategoryAPI {

	private final CreateCategoryUseCase createCategoryUseCase;
	private final GetCategoryByIdUseCase getCategoryByIdUseCase;
	private final UpdateCategoryUseCase updateCategoryUseCase;
	private final DeleteCategoryUseCase deleteCategoryUseCase;
	private final ListCategoriesUseCase listCategoriesUseCase;

	public CategoryController(
			final CreateCategoryUseCase createCategoryUseCase,
			final GetCategoryByIdUseCase getCategoryByIdUseCase,
			final UpdateCategoryUseCase updateCategoryUseCase,
			final DeleteCategoryUseCase deleteCategoryUseCase,
			final ListCategoriesUseCase listCategoriesUseCase
	) {
		this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
		this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
		this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
		this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
		this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
	}

	@Override
	public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
		final var aCommand = CreateCategoryCommand.with(
				input.name(),
				input.description(),
				input.active() != null ? input.active() : true
		);

		final var output = this.createCategoryUseCase.execute(aCommand);

		return ResponseEntity
				.created(URI.create("/categories/" + output.id())).body(output);
	}

	@Override
	public CategoryResponse getById(String id) {
		return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
	}

	@Override
	public ResponseEntity<?> updateById(String id, UpdateCategoryRequest input) {
		final var aCommand = UpdateCategoryCommand.with(
				id,
				input.name(),
				input.description(),
				input.active() != null ? input.active() : true
		);

		final var output = this.updateCategoryUseCase.execute(aCommand);

		return ResponseEntity.ok(output);
	}

	@Override
	public void deleteById(final String id) {
		this.deleteCategoryUseCase.execute(id);
	}

	@Override
	public Pagination<CategoryListResponse> listCategories(
			final String search,
			final int page,
			final int perPage,
			final String sort,
			final String dir
	) {
		return listCategoriesUseCase.execute(
				new SearchQuery(
						page,
						perPage,
						search,
						sort,
						dir
				)
		)
				.map(CategoryApiPresenter::present);
	}
}
