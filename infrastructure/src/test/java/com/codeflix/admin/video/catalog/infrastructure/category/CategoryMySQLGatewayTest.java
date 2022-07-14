package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.infrastructure.MySQLGatewayTest;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

	@Autowired
	private CategoryMySQLGateway gateway;

	@Autowired
	private CategoryRepository repository;

	@Test
	public void testInjectedDependencies() {
		Assertions.assertNotNull(gateway);
		Assertions.assertNotNull(repository);
	}

	@Test
	public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		Assertions.assertEquals(0, repository.count());

		final var actualCategory = gateway.create(aCategory);

		Assertions.assertEquals(1, repository.count());

		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());

		final var actualEntity = repository.findById(actualCategory.getId().getValue()).get();

		Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
		Assertions.assertEquals(expectedName, actualEntity.getName());
		Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
		Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
		Assertions.assertNull(actualEntity.getDeletedAt());
	}

	@Test
	public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory("old movies", "old movies desc", expectedIsActive);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		final var anUpdatedCategory = Category.from(aCategory)
				.update(expectedName, expectedDescription, expectedIsActive);

		final var actualCategory = gateway.update(anUpdatedCategory);

		Assertions.assertEquals(1, repository.count());

		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());

		final var actualEntity = repository.findById(actualCategory.getId().getValue()).get();

		Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
		Assertions.assertEquals(expectedName, actualEntity.getName());
		Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
		Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
		Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
		Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
		Assertions.assertNull(actualEntity.getDeletedAt());
	}

	@Test
	public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_thenShouldDeleteCategory() {
		final var aCategory = Category.newCategory("Movies", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		gateway.deleteById(aCategory.getId());

		Assertions.assertEquals(0, repository.count());
	}

	@Test
	public void givenAnInvalidCategoryId_whenTryToDeleteIt_thenShouldDeleteCategory() {
		final var aCategory = Category.newCategory("Movies", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		gateway.deleteById(CategoryID.from("invalid"));

		Assertions.assertEquals(1, repository.count());
	}
}
