package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.category.CategorySearchQuery;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
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
	public Category create(final Category aCategory) {
		return save(aCategory);
	}

	@Override
	public void deleteById(CategoryID anId) {
		final var anIdValue = anId.getValue();
		if (this.repository.existsById(anIdValue)) {
			this.repository.deleteById(anIdValue);
		}
	}

	@Override
	public Optional<Category> findById(CategoryID anId) {
		return Optional.empty();
	}

	@Override
	public Category update(final Category aCategory) {
		return save(aCategory);
	}

	@Override
	public Pagination<Category> findAll(CategorySearchQuery aQuery) {
		return null;
	}

	private Category save(Category aCategory) {
		return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
	}
}
