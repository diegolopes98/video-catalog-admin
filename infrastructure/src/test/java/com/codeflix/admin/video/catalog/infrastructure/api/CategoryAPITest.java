package com.codeflix.admin.video.catalog.infrastructure.api;

import com.codeflix.admin.video.catalog.ControllerTest;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
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
}
