package com.codeflix.admin.video.catalog.application.category.delete;

import com.codeflix.admin.video.catalog.IntegrationTest;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
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
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

	@Autowired
	private DeleteCategoryUseCase useCase;

	@Autowired
	private CategoryRepository repository;

	@SpyBean
	private CategoryGateway categoryGateway;

	@BeforeEach
	public void cleanup() {
		Mockito.reset(categoryGateway);
	}

	@Test
	public void givenAValidId_whenCallsDeleteCategory_thenShouldBeOk() {
		final var aCategory = Category.newCategory("Movies", "Movies description", true);
		final var expectedId = aCategory.getId();

		saveAllAndFlush(aCategory);

		Assertions.assertEquals(1, repository.count());

		Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

		Assertions.assertEquals(0, repository.count());
	}

	@Test
	public void givenAnInvalidId_whenCallsDeleteCategory_thenShouldBeOk() {
		final var expectedId = CategoryID.from("123");

		Assertions.assertEquals(0, repository.count());

		Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

		Assertions.assertEquals(0, repository.count());
	}

	@Test
	public void givenAValid_whenGatewayThrowsError_thenShouldReturnException() {
		final var aCategory = Category.newCategory("Movies", "Movies description", true);
		final var expectedId = aCategory.getId();

		doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(eq(expectedId));

		Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

		Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
	}

	private void saveAllAndFlush(final Category... aCategory) {
		repository.saveAllAndFlush(Arrays.stream(aCategory).map(CategoryJpaEntity::from).toList());
	}

}
