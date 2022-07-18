package com.codeflix.admin.video.catalog.e2e.category;

import com.codeflix.admin.video.catalog.E2ETest;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import com.codeflix.admin.video.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CategoryRepository repository;

	@Container
	private static final MySQLContainer MYSQL_CONTAINER =
			new MySQLContainer("mysql:8.0.29")
					.withPassword("123456")
					.withUsername("root")
					.withDatabaseName("videos_adm");

	@DynamicPropertySource
	public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
		final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
		registry.add("mysql.port", () -> mappedPort);
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

		final var actualCategory = retrieveACategory(actualId.getValue());

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.name());
		Assertions.assertEquals(expectedDescription, actualCategory.description());
		Assertions.assertEquals(expectedIsActive, actualCategory.active());
		Assertions.assertNotNull(actualCategory.createdAt());
		Assertions.assertNotNull(actualCategory.updatedAt());
		Assertions.assertNull(actualCategory.deletedAt());
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToNavigateThroughAllCategories() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		givenACategory("Movies", null, true);
		givenACategory("Documentaries", null, true);
		givenACategory("Sitcoms", null, true);

		listCategories(0, 1)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(0)))
				.andExpect(jsonPath("$.per_page", equalTo(1)))
				.andExpect(jsonPath("$.total", equalTo(3)))
				.andExpect(jsonPath("$.items", hasSize(1)))
				.andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")));


		listCategories(1, 1)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(1)))
				.andExpect(jsonPath("$.per_page", equalTo(1)))
				.andExpect(jsonPath("$.total", equalTo(3)))
				.andExpect(jsonPath("$.items", hasSize(1)))
				.andExpect(jsonPath("$.items[0].name", equalTo("Movies")));

		listCategories(2, 1)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(2)))
				.andExpect(jsonPath("$.per_page", equalTo(1)))
				.andExpect(jsonPath("$.total", equalTo(3)))
				.andExpect(jsonPath("$.items", hasSize(1)))
				.andExpect(jsonPath("$.items[0].name", equalTo("Sitcoms")));

		listCategories(3, 1)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(3)))
				.andExpect(jsonPath("$.per_page", equalTo(1)))
				.andExpect(jsonPath("$.total", equalTo(3)))
				.andExpect(jsonPath("$.items", hasSize(0)));
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToSearchThroughAllCategories() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		givenACategory("Movies", null, true);
		givenACategory("Documentaries", null, true);
		givenACategory("Sitcoms", null, true);

		listCategories(0, 1, "mOV")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(0)))
				.andExpect(jsonPath("$.per_page", equalTo(1)))
				.andExpect(jsonPath("$.total", equalTo(1)))
				.andExpect(jsonPath("$.items", hasSize(1)))
				.andExpect(jsonPath("$.items[0].name", equalTo("Movies")));


		listCategories(1, 1, "mOV")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(1)))
				.andExpect(jsonPath("$.per_page", equalTo(1)))
				.andExpect(jsonPath("$.total", equalTo(1)))
				.andExpect(jsonPath("$.items", hasSize(0)));
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		givenACategory("Movies", "C", true);
		givenACategory("Documentaries", "A", true);
		givenACategory("Sitcoms", "Z", true);

		listCategories(0, 3, "", "description", "desc")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.current_page", equalTo(0)))
				.andExpect(jsonPath("$.per_page", equalTo(3)))
				.andExpect(jsonPath("$.total", equalTo(3)))
				.andExpect(jsonPath("$.items", hasSize(3)))
				.andExpect(jsonPath("$.items[0].name", equalTo("Sitcoms")))
				.andExpect(jsonPath("$.items[1].name", equalTo("Movies")))
				.andExpect(jsonPath("$.items[2].name", equalTo("Documentaries")));
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

		final var actualCategory = repository.findById(actualId.getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToUnderstandTheErrorWhenGettingACategoryByItsIdentifier() throws Exception {
		final var expectedErrorMessage = "Category with ID 123 was not found";

		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		final var aRequest = get("/categories/" + CategoryID.from("123").getValue())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(aRequest)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = false;

		final var actualId = givenACategory("old movies", null, true);

		final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

		final var aRequest = MockMvcRequestBuilders.put("/categories/" + actualId.getValue())
				.contentType(MediaType.APPLICATION_JSON)
				.content(Json.writeValueAsString(aRequestBody));

		this.mvc.perform(aRequest)
				.andExpect(status().isOk());

		final var actualCategory = repository.findById(actualId.getValue()).get();

		Assertions.assertNotNull(actualCategory);
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNotNull(actualCategory.getDeletedAt());
	}

	@Test
	public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
		Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
		Assertions.assertEquals(0, this.repository.count());

		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

		this.mvc.perform(delete("/categories/" + actualId.getValue()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		Assertions.assertFalse(this.repository.existsById(actualId.getValue()));
	}

	private ResultActions listCategories(
			final int page,
			final int perPage,
			final String search
	) throws Exception {
		return listCategories(page, perPage, search, "", "");
	}

	private ResultActions listCategories(
			final int page,
			final int perPage
	) throws Exception {
		return listCategories(page, perPage, "", "", "");
	}

	private ResultActions listCategories(
			final int page,
			final int perPage,
			final String search,
			final String sort,
			final String direction
	) throws Exception {
		final var aRequest = get("/categories")
				.queryParam("page", String.valueOf(page))
				.queryParam("perPage", String.valueOf(perPage))
				.queryParam("search", search)
				.queryParam("sort", sort)
				.queryParam("dir", direction)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		return this.mvc.perform(aRequest);

	}

	private CategoryID givenACategory(
			final String aName,
			final String aDescription,
			final boolean isActive
	) throws Exception {
		final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

		final var aRequest = MockMvcRequestBuilders.post("/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(Json.writeValueAsString(aRequestBody));

		final var actualId = this.mvc.perform(aRequest)
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse().getHeader("Location")
				.replace("/categories/", "");

		return CategoryID.from(actualId);
	}

	private CategoryResponse retrieveACategory(final String anId) throws Exception {
		final var aRequest = get("/categories/" + anId)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		final var json = this.mvc.perform(aRequest)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();

		return Json.readValue(json, CategoryResponse.class);
	}
}
