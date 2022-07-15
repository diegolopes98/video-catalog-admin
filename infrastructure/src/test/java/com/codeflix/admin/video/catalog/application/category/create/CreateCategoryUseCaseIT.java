package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.IntegrationTest;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class CreateCategoryUseCaseIT {

	@Autowired
	private DefaultCreateCategoryUseCase useCase;

	@Autowired
	private CategoryRepository repository;

	@SpyBean
	private CategoryGateway categoryGateway;

	@BeforeEach
	public void cleanup() {
		Mockito.reset(categoryGateway);
	}

	@Test
	public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		Assertions.assertEquals(0, repository.count());
		final var aCommand =
				CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		final var actualOutput = useCase.execute(aCommand).get();


		Assertions.assertEquals(1, repository.count());

		Assertions.assertNotNull(actualOutput);
		Assertions.assertNotNull(actualOutput.id());

		final var actualCategory = repository.findById(actualOutput.id().getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());
	}

	@Test
	public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenShouldReturnCategoryId() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = false;

		Assertions.assertEquals(0, repository.count());
		final var aCommand =
				CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		final var actualOutput = useCase.execute(aCommand).get();


		Assertions.assertEquals(1, repository.count());

		Assertions.assertNotNull(actualOutput);
		Assertions.assertNotNull(actualOutput.id());

		final var actualCategory = repository.findById(actualOutput.id().getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNotNull(actualCategory.getDeletedAt());
	}

	@Test
	public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
		final String expectedName = null;
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedErrorMessage = "'name' should not be null";
		final var expectedErrorCount = 1;

		Assertions.assertEquals(0, repository.count());

		final var aCommand =
				CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		final var notificationHandler = useCase.execute(aCommand).getLeft();

		Assertions.assertEquals(0, repository.count());

		Assertions.assertEquals(expectedErrorCount, notificationHandler.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, notificationHandler.firstError().message());

		Mockito.verify(categoryGateway, Mockito.times(0)).create(any());
	}

	@Test
	public void givenAValidCommand_whenGatewayThrowsException_thenShouldReturnException() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedErrorMessage = "gateway mock exception";
		final var expectedErrorCount = 1;

		final var aCommand =
				CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		doThrow(new IllegalStateException(expectedErrorMessage))
				.when(categoryGateway).create(any());

		final var notificationHandler = useCase.execute(aCommand).getLeft();

		Assertions.assertEquals(expectedErrorCount, notificationHandler.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, notificationHandler.firstError().message());
	}
}
