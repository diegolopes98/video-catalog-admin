package com.codeflix.admin.video.catalog.infrastructure.api;

import com.codeflix.admin.video.catalog.ControllerTest;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private CreateCategoryUseCase createCategoryUseCase;

	@MockBean
	private GetCategoryByIdUseCase getCategoryByIdUseCase;

	@Test
	public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var anInput =
				new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

		when(createCategoryUseCase.execute(any()))
				.thenReturn(Right(CreateCategoryOutput.from(CategoryID.from("123").getValue())));

		final var request = MockMvcRequestBuilders
				.post("/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(anInput));

		this.mvc.perform(request)
				.andDo(print())
				.andExpectAll(
						status().isCreated(),
						header().string("Location", "/categories/123"),
						header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
						jsonPath("$.id", equalTo("123"))
				);

		verify(createCategoryUseCase, times(1)).execute(
				argThat(cmd ->
								Objects.equals(expectedName, cmd.name())
										&& Objects.equals(expectedDescription, cmd.description())
										&& Objects.equals(expectedIsActive, cmd.isActive())
				)
		);
	}

	@Test
	public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnErrors() throws Exception {
		final String expectedName = null;
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedMessage = "'name' should not be null";

		final var anInput =
				new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

		when(createCategoryUseCase.execute(any()))
				.thenReturn(Left(NotificationValidationHandler.create(new Error(expectedMessage))));

		final var request = MockMvcRequestBuilders
				.post("/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(anInput));

		this.mvc.perform(request)
				.andDo(print())
				.andExpectAll(
						status().isUnprocessableEntity(),
						header().string("Location", nullValue()),
						header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
						jsonPath("$.errors", hasSize(1)),
						jsonPath("$.errors[0].message", equalTo(expectedMessage))
				);

		verify(createCategoryUseCase, times(1)).execute(
				argThat(cmd ->
								Objects.equals(expectedName, cmd.name())
										&& Objects.equals(expectedDescription, cmd.description())
										&& Objects.equals(expectedIsActive, cmd.isActive())
				)
		);
	}

	@Test
	public void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
		final String expectedName = null;
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;
		final var expectedMessage = "'name' should not be null";

		final var anInput =
				new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

		when(createCategoryUseCase.execute(any()))
				.thenThrow(DomainException.with(new Error(expectedMessage)));

		final var request = MockMvcRequestBuilders
				.post("/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(anInput));

		this.mvc.perform(request)
				.andDo(print())
				.andExpectAll(
						status().isUnprocessableEntity(),
						header().string("Location", nullValue()),
						header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
						jsonPath("$.errors", hasSize(1)),
						jsonPath("$.errors[0].message", equalTo(expectedMessage))
				);

		verify(createCategoryUseCase, times(1)).execute(
				argThat(cmd ->
								Objects.equals(expectedName, cmd.name())
										&& Objects.equals(expectedDescription, cmd.description())
										&& Objects.equals(expectedIsActive, cmd.isActive())
				)
		);
	}

	@Test
	public void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() throws Exception {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory(expectedName, expectedDescription, expectedIsActive);

		final var expectedId = aCategory.getId().getValue();

		when(getCategoryByIdUseCase.execute(any()))
				.thenReturn(CategoryOutput.from(aCategory));

		final var request = MockMvcRequestBuilders
				.get("/categories/{id}", expectedId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isOk(),
				header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
				jsonPath("$.id", equalTo(expectedId)),
				jsonPath("$.name", equalTo(expectedName)),
				jsonPath("$.description", equalTo(expectedDescription)),
				jsonPath("$.is_active", equalTo(expectedIsActive)),
				jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())),
				jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())),
				jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt()))
		);
		verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
	}

	@Test
	public void givenAValid_whenGatewayThrowsError_thenShouldReturnNotFoundException() throws Exception {
		final var expectedId = CategoryID.from("123").getValue();
		final var expectedErrorMessage = "Category with ID 123 was not found";

		final var request = MockMvcRequestBuilders
				.get("/categories/{id}", expectedId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		when(getCategoryByIdUseCase.execute(any()))
				.thenThrow(NotFoundException.with(
						Category.class,
						CategoryID.from(expectedId)
				));

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isNotFound(),
				jsonPath("$.message", equalTo(expectedErrorMessage))
		);
		verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
	}
}
