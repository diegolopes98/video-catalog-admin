package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.infrastructure.MySQLGatewayTest;
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
}
