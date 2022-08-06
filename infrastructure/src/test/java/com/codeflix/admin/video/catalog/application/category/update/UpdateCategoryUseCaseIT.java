package com.codeflix.admin.video.catalog.application.category.update;

import com.codeflix.admin.video.catalog.IntegrationTest;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

	@Autowired
	private UpdateCategoryUseCase useCase;

	@Autowired
	private CategoryRepository repository;

	@SpyBean
	private CategoryGateway categoryGateway;

	@BeforeEach
	public void cleanup() {
		Mockito.reset(categoryGateway);
	}

	@Test
	public void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() {
		final var aCategory = Category.newCategory("Old movie", null, true);

		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedId = aCategory.getId();

		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		saveAllAndFlush(aCategory);

		Assertions.assertEquals(1, repository.count());

		final var actualOutput = useCase.execute(aCommand);

		Assertions.assertNotNull(actualOutput);
		Assertions.assertNotNull(actualOutput.id());

		final var actualCategory = repository.findById(expectedId.getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
		Assertions.assertNull(actualCategory.getDeletedAt());
	}

	@Test
	public void givenAValidCommandWithInactiveCategory_whenCallsUpdateCategory_thenShouldReturnCategoryId() {
		final var aCategory = Category.newCategory("Old movie", null, true);

		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = false;
		final var expectedId = aCategory.getId();

		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		saveAllAndFlush(aCategory);

		final var actualOutput = useCase.execute(aCommand);

		Assertions.assertNotNull(actualOutput);
		Assertions.assertNotNull(actualOutput.id());
		Assertions.assertNull(aCategory.getDeletedAt());

		final var actualCategory = repository.findById(expectedId.getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
		Assertions.assertNotNull(actualCategory.getDeletedAt());
	}

	@Test
	public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
		final var aCategory = Category.newCategory("Old movie", null, true);

		final String expectedName = null;
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedId = aCategory.getId();
		final var expectedErrorMessage = "'name' should not be null";
		final var expectedErrorCount = 1;

		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		saveAllAndFlush(aCategory);

		final var actualException = Assertions.assertThrows(
				NotificationException.class,
				() -> useCase.execute(aCommand)
		);

		Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

		Mockito.verify(categoryGateway, Mockito.times(0)).create(any());
	}

	@Test
	public void givenAValidCommand_whenGatewayThrowsException_thenShouldReturnException() {
		final var aCategory = Category.newCategory("Old movie", null, true);

		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedId = aCategory.getId();
		final var expectedErrorMessage = "gateway mock exception";

		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		saveAllAndFlush(aCategory);

		doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).update(any());

		final var actualException = Assertions.assertThrows(
				IllegalStateException.class,
				() -> useCase.execute(aCommand)
		);

		Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

		final var actualCategory = repository.findById(expectedId.getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
		Assertions.assertEquals(actualCategory.getDescription(), actualCategory.getDescription());
		Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
	}

	@Test
	public void givenACommandWithInvalidID_whenCallsUpdateCategory_thenShouldNotFoundException() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = false;
		final var expectedId = "123";
		final var expectedErrorMessage = "Category with ID 123 was not found";

		final var aCommand = UpdateCategoryCommand.with(
				expectedId,
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		final var actualException = Assertions.assertThrows(
				NotFoundException.class,
				() -> useCase.execute(aCommand)
		);

		Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

	}

	private void saveAllAndFlush(final Category... aCategory) {
		repository.saveAllAndFlush(Arrays.stream(aCategory).map(CategoryJpaEntity::from).toList());
	}
}
