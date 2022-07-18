package com.codeflix.admin.video.catalog.infrastructure.category.models;

import com.codeflix.admin.video.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
class CategoryListResponseTest {

	@Autowired
	private JacksonTester<CategoryListResponse> json;

	@Test
	public void testMarshall() throws Exception {
		final var expectedId = "123";
		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = false;
		final var expectedCreatedAt = Instant.now();
		final var expectedDeletedAt = Instant.now();

		final var response = new CategoryListResponse(
				expectedId,
				expectedName,
				expectedDescription,
				expectedIsActive,
				expectedCreatedAt,
				expectedDeletedAt
		);

		final var actualJson = this.json.write(response);

		Assertions.assertThat(actualJson)
				.hasJsonPathValue("$.id", expectedId)
				.hasJsonPathValue("$.name", expectedName)
				.hasJsonPathValue("$.description", expectedDescription)
				.hasJsonPathValue("$.is_active", expectedIsActive)
				.hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
				.hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
	}
}