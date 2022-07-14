package com.codeflix.admin.video.catalog.infrastructure.configuration.usecases;

import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.admin.video.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.update.UpdateCategoryUseCase;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

	private final CategoryGateway gateway;

	public CategoryUseCaseConfig(final CategoryGateway gateway) {
		this.gateway = gateway;
	}

	@Bean
	public CreateCategoryUseCase createCategoryUseCase() {
		return new DefaultCreateCategoryUseCase(gateway);
	}

	@Bean
	public UpdateCategoryUseCase updateCategoryUseCase() {
		return new DefaultUpdateCategoryUseCase(gateway);
	}

	@Bean
	public GetCategoryByIdUseCase getCategoryByIdUseCase() {
		return new DefaultGetCategoryByIdUseCase(gateway);
	}

	@Bean
	public ListCategoriesUseCase listCategoriesUseCase() {
		return new DefaultListCategoriesUseCase(gateway);
	}

	@Bean
	public DeleteCategoryUseCase deleteCategoryUseCase() {
		return new DefaultDeleteCategoryUseCase(gateway);
	}
}
