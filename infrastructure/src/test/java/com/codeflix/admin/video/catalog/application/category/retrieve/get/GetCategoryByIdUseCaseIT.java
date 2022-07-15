package com.codeflix.admin.video.catalog.application.category.retrieve.get;

import com.codeflix.admin.video.catalog.IntegrationTest;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

	@Autowired
	private GetCategoryByIdUseCase useCase;

	@Autowired
	private CategoryRepository repository;

	@SpyBean
	private CategoryGateway categoryGateway;

	@BeforeEach
	public void cleanup() {
		Mockito.reset(categoryGateway);
	}

	@Test
	public void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory(expectedName, expectedDescription, expectedIsActive);
		final var expectedId = aCategory.getId();

		saveAllAndFlush(aCategory);

		final var actualCategory = useCase.execute(expectedId.getValue());

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedId, actualCategory.id());
		Assertions.assertEquals(expectedName, actualCategory.name());
		Assertions.assertEquals(expectedDescription, actualCategory.description());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
		Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
	}

	@Test
	public void givenAnInvalidId_whenCallsGetCategory_thenShouldReturnNotFound() {
		final var expectedErrorMessage = "Category with ID 123 was not found";
		final var expectedId = CategoryID.from("123");

		final var actualException = Assertions.assertThrows(
				DomainException.class,
				() -> useCase.execute(expectedId.getValue())
		);

		Assertions.assertNotNull(actualException);
		Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
		Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
	}

	@Test
	public void givenAValid_whenGatewayThrowsError_thenShouldReturnException() {
		final var aCategory = Category
				.newCategory("Movies", "Movies description", true);
		final var expectedId = aCategory.getId();
		final var expectedErrorMessage = "Gateway error";

		doThrow(new IllegalStateException(expectedErrorMessage))
				.when(categoryGateway)
				.findById(eq(expectedId));

		final var actualException = Assertions.assertThrows(
				IllegalStateException.class,
				() -> useCase.execute(expectedId.getValue())
		);
		Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
		Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
	}

	private void saveAllAndFlush(final Category... aCategory) {
		repository.saveAllAndFlush(
				Arrays
						.stream(aCategory)
						.map(CategoryJpaEntity::from)
						.toList()
		);
	}

}
