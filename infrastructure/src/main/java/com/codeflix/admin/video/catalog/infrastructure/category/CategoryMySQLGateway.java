package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.codeflix.admin.video.catalog.infrastructure.utils.SpecificationUtils.like;

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
	public void deleteById(final CategoryID anId) {
		final var anIdValue = anId.getValue();
		if (this.repository.existsById(anIdValue)) {
			this.repository.deleteById(anIdValue);
		}
	}

	@Override
	public Optional<Category> findById(final CategoryID anId) {
		return this.repository.findById(anId.getValue()).map(CategoryJpaEntity::toAggregate);
	}

	@Override
	public Category update(final Category aCategory) {
		return save(aCategory);
	}

	@Override
	public Pagination<Category> findAll(final SearchQuery aQuery) {
		final var page = PageRequest.of(
				aQuery.page(),
				aQuery.perPage(),
				Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
		);

		final var specifications = Optional.ofNullable(aQuery.terms())
				.filter(str -> !str.isBlank())
				.map(this::assembleSpecification)
				.orElse(null);

		final var pageResult =
				this.repository.findAll(Specification.where(specifications), page);

		return new Pagination<>(
				pageResult.getNumber(),
				pageResult.getSize(),
				pageResult.getTotalElements(),
				pageResult.map(CategoryJpaEntity::toAggregate).stream().toList()
		);
	}

	@Override
	public List<CategoryID> existsByIds(Iterable<CategoryID> ids) {
		// TODO: implement when working in genre infrastructure
		return Collections.emptyList();
	}

	private Specification<CategoryJpaEntity> assembleSpecification(final String str) {
		final Specification<CategoryJpaEntity> nameLike = like("name", str);
		final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
		return nameLike.or(descriptionLike);
	}

	private Category save(final Category aCategory) {
		return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
	}
}
