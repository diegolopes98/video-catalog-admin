package com.codeflix.admin.video.catalog.infrastructure.category.persistence;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repository;

	@Test
	public void givenAnInvalidNullName_whenCallsSave_thenShouldReturnError() {
		final var expectedPropertyName = "name";
		final var aCategory = Category.newCategory("Movies", null, true);
		final var anEntity = CategoryJpaEntity.from(aCategory);
		anEntity.setName(null);

		final var actualException = Assertions.assertThrows(
				DataIntegrityViolationException.class,
				() -> repository.save(anEntity)
		);

		final var actualCause = Assertions
				.assertInstanceOf(PropertyValueException.class, actualException.getCause());

		Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
		Assertions.assertTrue(actualCause.getMessage()
									  .contains("not-null property references a null or transient value"));
	}

	@Test
	public void givenAnInvalidNullCreatedAt_whenCallsSave_thenShouldReturnError() {
		final var expectedPropertyName = "createdAt";
		final var aCategory = Category.newCategory("Movies", null, true);
		final var anEntity = CategoryJpaEntity.from(aCategory);
		anEntity.setCreatedAt(null);

		final var actualException = Assertions.assertThrows(
				DataIntegrityViolationException.class,
				() -> repository.save(anEntity)
		);

		final var actualCause = Assertions
				.assertInstanceOf(PropertyValueException.class, actualException.getCause());

		Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
		Assertions.assertTrue(actualCause.getMessage()
									  .contains("not-null property references a null or transient value"));
	}

	@Test
	public void givenAnInvalidNullUpdatedAt_whenCallsSave_thenShouldReturnError() {
		final var expectedPropertyName = "updatedAt";
		final var aCategory = Category.newCategory("Movies", null, true);
		final var anEntity = CategoryJpaEntity.from(aCategory);
		anEntity.setUpdatedAt(null);

		final var actualException = Assertions.assertThrows(
				DataIntegrityViolationException.class,
				() -> repository.save(anEntity)
		);

		final var actualCause = Assertions
				.assertInstanceOf(PropertyValueException.class, actualException.getCause());

		Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
		Assertions.assertTrue(actualCause.getMessage()
									  .contains("not-null property references a null or transient value"));
	}
}
