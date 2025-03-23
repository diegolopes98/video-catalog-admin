package com.codeflix.admin.video.catalog.infrastructure.api;

import com.codeflix.admin.video.catalog.ControllerTest;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.admin.video.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.codeflix.admin.video.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.admin.video.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.codeflix.admin.video.catalog.application.category.update.UpdateCategoryOutput;
import com.codeflix.admin.video.catalog.application.category.update.UpdateCategoryUseCase;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

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
	private DefaultCreateCategoryUseCase createCategoryUseCase;

	@MockBean
	private DefaultGetCategoryByIdUseCase getCategoryByIdUseCase;

	@MockBean
	private DefaultUpdateCategoryUseCase updateCategoryUseCase;

	@MockBean
	private DefaultDeleteCategoryUseCase deleteCategoryUseCase;

	@MockBean
	private DefaultListCategoriesUseCase listCategoriesUseCase;

	@Test
	public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var anInput =
				new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

		when(createCategoryUseCase.execute(any()))
				.thenReturn(CreateCategoryOutput.from(CategoryID.from("123").getValue()));

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
				new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

		when(createCategoryUseCase.execute(any()))
				.thenThrow(new NotificationException("mock exception", NotificationValidationHandler.create(new Error(expectedMessage))));

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
				new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

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

	@Test
	public void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() throws Exception {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory("old movies", null, false);

		final var expectedId = aCategory.getId().getValue();

		when(updateCategoryUseCase.execute(any()))
				.thenReturn(UpdateCategoryOutput.from(expectedId));

		final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

		final var request = MockMvcRequestBuilders
				.put("/categories/{id}", expectedId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(input));

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isOk(),
				jsonPath("$.id", equalTo(expectedId)),
				header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)
		);
		verify(updateCategoryUseCase, times(1)).execute(
				argThat(cmd ->
								Objects.equals(expectedName, cmd.name())
										&& Objects.equals(expectedDescription, cmd.description())
										&& Objects.equals(expectedIsActive, cmd.isActive())
				)
		);
	}

	@Test
	public void givenAInvalidId_whenCallsUpdateCategory_thenShouldReturnNotFound() throws Exception {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var expectedId = CategoryID.from("123").getValue();
		final var expectedErrorMessage = "Category with ID 123 was not found";

		when(updateCategoryUseCase.execute(any()))
				.thenThrow(NotFoundException.with(
						Category.class,
						CategoryID.from(expectedId)
				));

		final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

		final var request = MockMvcRequestBuilders
				.put("/categories/{id}", expectedId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(input));

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isNotFound(),
				header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
				jsonPath("$.message", equalTo(expectedErrorMessage))
		);

		verify(updateCategoryUseCase, times(1)).execute(
				argThat(cmd ->
								Objects.equals(expectedName, cmd.name())
										&& Objects.equals(expectedDescription, cmd.description())
										&& Objects.equals(expectedIsActive, cmd.isActive())
				)
		);
	}

	@Test
	public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
		final String expectedName = null;
		final String expectedDescription = null;
		final var expectedIsActive = false;

		final var expectedId = CategoryID.from("123").getValue();
		final var expectedErrorMessage = "'name' should not be null";

		when(updateCategoryUseCase.execute(any()))
				.thenThrow(new NotificationException(
						"mock exception",
						NotificationValidationHandler.create(
								new Error(expectedErrorMessage)
						)
				));

		final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

		final var request = MockMvcRequestBuilders
				.put("/categories/{id}", expectedId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(input));

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isUnprocessableEntity(),
				header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
				jsonPath("$.errors", hasSize(1)),
				jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
		);

		verify(updateCategoryUseCase, times(1)).execute(
				argThat(cmd ->
								Objects.equals(expectedName, cmd.name())
										&& Objects.equals(expectedDescription, cmd.description())
										&& Objects.equals(expectedIsActive, cmd.isActive())
				)
		);
	}

	@Test
	public void givenAValidId_whenCallsDeleteCategory_thenShouldReturnNoContent() throws Exception {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies Category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory(expectedName, expectedDescription, expectedIsActive);

		final var expectedId = aCategory.getId().getValue();

		doNothing()
				.when(deleteCategoryUseCase)
				.execute(expectedId);

		final var request = MockMvcRequestBuilders
				.delete("/categories/{id}", expectedId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isNoContent()
		);
		verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
	}

	@Test
	public void givenAValidParam_whenCallsListCategories_thenShouldReturnCategories() throws Exception {
		final var aCategory = Category.newCategory("Movies", null, true);
		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTerms = "movies";
		final var expectedSort = "description";
		final var expectedDirection = "desc";
		final var expectedItemsCount = 1;
		final var expectedTotal = 1;

		final var expectedItems = List.of(
				CategoryListOutput.from(aCategory)
		);

		when(listCategoriesUseCase.execute(any()))
				.thenReturn(new Pagination<>(
						expectedPage,
						expectedPerPage,
						expectedTotal,
						expectedItems
				));

		final var request = MockMvcRequestBuilders
				.get("/categories")
				.queryParam("page", String.valueOf(expectedPage))
				.queryParam("perPage", String.valueOf(expectedPerPage))
				.queryParam("sort", expectedSort)
				.queryParam("dir", expectedDirection)
				.queryParam("search", expectedTerms)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		final var response = this.mvc.perform(request)
				.andDo(print());

		response.andExpectAll(
				status().isOk(),
				jsonPath("$.current_page", equalTo(expectedPage)),
				jsonPath("$.per_page", equalTo(expectedPerPage)),
				jsonPath("$.total", equalTo(expectedTotal)),
				jsonPath("$.items", hasSize(expectedItemsCount)),
				jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())),
				jsonPath("$.items[0].name", equalTo(aCategory.getName())),
				jsonPath("$.items[0].description", equalTo(aCategory.getDescription())),
				jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())),
				jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())),
				jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt()))
		);

		verify(listCategoriesUseCase, times(1)).execute(
				argThat(query ->
								Objects.equals(expectedPage, query.page())
										&& Objects.equals(expectedPerPage, query.perPage())
										&& Objects.equals(expectedDirection, query.direction())
										&& Objects.equals(expectedSort, query.sort())
										&& Objects.equals(expectedTerms, query.terms())
				)
		);
	}
}
