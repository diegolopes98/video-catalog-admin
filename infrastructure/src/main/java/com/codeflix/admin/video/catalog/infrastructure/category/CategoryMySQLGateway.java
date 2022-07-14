package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.category.CategorySearchQuery;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

	private final CategoryRepository repository;

	public CategoryMySQLGateway(final CategoryRepository repository) {
		this.repository = repository;
	}

	@Override
	public Category create(Category aCategory) {
		return null;
	}

	@Override
	public void deleteById(CategoryID anId) {

	}

	@Override
	public Optional<Category> findById(CategoryID anId) {
		return Optional.empty();
	}

	@Override
	public Category update(Category aCategory) {
		return null;
	}

	@Override
	public Pagination<Category> findAll(CategorySearchQuery aQuery) {
		return null;
	}
}
