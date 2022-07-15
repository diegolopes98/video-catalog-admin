package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.MySQLGatewayTest;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.category.CategorySearchQuery;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

	@Autowired
	private CategoryMySQLGateway gateway;

	@Autowired
	private CategoryRepository repository;

	@Test
	public void testInjectedDependencies() {
		Assertions.assertNotNull(gateway);
		Assertions.assertNotNull(repository);
	}

	@Test
	public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		Assertions.assertEquals(0, repository.count());

		final var actualCategory = gateway.create(aCategory);

		Assertions.assertEquals(1, repository.count());

		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());

		final var actualEntity = repository.findById(actualCategory.getId().getValue()).get();

		Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
		Assertions.assertEquals(expectedName, actualEntity.getName());
		Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
		Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
		Assertions.assertNull(actualEntity.getDeletedAt());
	}

	@Test
	public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory("old movies", "old movies desc", expectedIsActive);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		final var anUpdatedCategory = Category.from(aCategory)
				.update(expectedName, expectedDescription, expectedIsActive);

		final var actualCategory = gateway.update(anUpdatedCategory);

		Assertions.assertEquals(1, repository.count());

		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());

		final var actualEntity = repository.findById(actualCategory.getId().getValue()).get();

		Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
		Assertions.assertEquals(expectedName, actualEntity.getName());
		Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
		Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
		Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
		Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
		Assertions.assertNull(actualEntity.getDeletedAt());
	}

	@Test
	public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_thenShouldDeleteCategory() {
		final var aCategory = Category.newCategory("Movies", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		gateway.deleteById(aCategory.getId());

		Assertions.assertEquals(0, repository.count());
	}

	@Test
	public void givenAnInvalidCategoryId_whenTryToDeleteIt_thenShouldDeleteCategory() {
		final var aCategory = Category.newCategory("Movies", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		gateway.deleteById(CategoryID.from("invalid"));

		Assertions.assertEquals(1, repository.count());
	}

	@Test
	public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
		final var expectedName = "Movies";
		final var expectedDescription = "Movies category";
		final var expectedIsActive = true;

		final var aCategory = Category
				.newCategory(expectedName, expectedDescription, expectedIsActive);

		Assertions.assertEquals(0, repository.count());

		repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

		Assertions.assertEquals(1, repository.count());

		final var actualCategory = gateway.findById(aCategory.getId()).get();

		Assertions.assertEquals(1, repository.count());

		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
		Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
		Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
		Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());
	}

	@Test
	public void givenAValidCategoryIdNotPersisted_whenCallsFindById_shouldReturnEmpty() {
		Assertions.assertEquals(0, repository.count());

		final var actualCategory = gateway.findById(CategoryID.from("empty"));

		Assertions.assertTrue(actualCategory.isEmpty());
	}

	@Test
	public void givenPrePersistedCategories_whenCallsFindAll_thenShouldReturnPaginated() {
		final var expectedPage = 0;
		final var expectedPerPage = 1;
		final var expectedTotal = 3;
		final var expectedItemsCount = expectedPerPage;

		final var movies = Category.newCategory("Movies", null, true);
		final var sitcoms = Category.newCategory("Sitcoms", null, true);
		final var documentaries = Category.newCategory("Documentaries", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAll(
				List.of(movies, sitcoms, documentaries)
						.stream()
						.map(CategoryJpaEntity::from)
						.toList()
		);

		Assertions.assertEquals(3, repository.count());

		final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
		final var actualResult = gateway.findAll(query);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
		Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
	}

	@Test
	public void givenEmptyCategoriesTable_whenCallsFindAll_thenShouldReturnEmptyPage() {
		final var expectedPage = 0;
		final var expectedPerPage = 1;
		final var expectedTotal = 0;
		final var expectedItemsCount = 0;

		Assertions.assertEquals(0, repository.count());

		final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
		final var actualResult = gateway.findAll(query);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
	}

	@Test
	public void givenPrePersistedCategories_whenCallsFindAllWithPage1_thenShouldReturnPaginated() {
		var expectedPage = 0;
		final var expectedPerPage = 1;
		final var expectedTotal = 3;
		final var expectedItemsCount = expectedPerPage;

		final var movies = Category.newCategory("Movies", null, true);
		final var sitcoms = Category.newCategory("Sitcoms", null, true);
		final var documentaries = Category.newCategory("Documentaries", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAll(
				List.of(movies, sitcoms, documentaries)
						.stream()
						.map(CategoryJpaEntity::from)
						.toList()
		);

		Assertions.assertEquals(3, repository.count());

		final var firstQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
		final var firstResult = gateway.findAll(firstQuery);

		Assertions.assertEquals(expectedPage, firstResult.currentPage());
		Assertions.assertEquals(expectedPerPage, firstResult.perPage());
		Assertions.assertEquals(expectedTotal, firstResult.total());
		Assertions.assertEquals(expectedItemsCount, firstResult.items().size());
		Assertions.assertEquals(documentaries.getId(), firstResult.items().get(0).getId());

		final var secondQuery = new CategorySearchQuery(1, 1, "", "name", "asc");
		final var secondResult = gateway.findAll(secondQuery);
		expectedPage = 1;

		Assertions.assertEquals(expectedPage, secondResult.currentPage());
		Assertions.assertEquals(expectedPerPage, secondResult.perPage());
		Assertions.assertEquals(expectedTotal, secondResult.total());
		Assertions.assertEquals(expectedItemsCount, secondResult.items().size());
		Assertions.assertEquals(movies.getId(), secondResult.items().get(0).getId());

		final var thirdQuery = new CategorySearchQuery(2, 1, "", "name", "asc");
		final var thirdResult = gateway.findAll(thirdQuery);
		expectedPage = 2;

		Assertions.assertEquals(expectedPage, thirdResult.currentPage());
		Assertions.assertEquals(expectedPerPage, thirdResult.perPage());
		Assertions.assertEquals(expectedTotal, thirdResult.total());
		Assertions.assertEquals(expectedItemsCount, thirdResult.items().size());
		Assertions.assertEquals(sitcoms.getId(), thirdResult.items().get(0).getId());
	}

	@Test
	public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchName_thenShouldReturnPaginated() {
		final var expectedPage = 0;
		final var expectedPerPage = 1;
		final var expectedTotal = 1;
		final var expectedItemsCount = expectedPerPage;

		final var movies = Category.newCategory("Movies", null, true);
		final var sitcoms = Category.newCategory("Sitcoms", null, true);
		final var documentaries = Category.newCategory("Documentaries", null, true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAll(
				List.of(movies, sitcoms, documentaries)
						.stream()
						.map(CategoryJpaEntity::from)
						.toList()
		);

		Assertions.assertEquals(3, repository.count());

		final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
		final var actualResult = gateway.findAll(query);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
		Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
	}

	@Test
	public void givenPrePersistedCategoriesAndMovAsTerms_whenCallsFindAllAndTermsMatchDesc_thenShouldReturnPaginated() {
		final var expectedPage = 0;
		final var expectedPerPage = 1;
		final var expectedTotal = 1;
		final var expectedItemsCount = expectedPerPage;

		final var movies = Category.newCategory("Movies", "movies", true);
		final var sitcoms = Category.newCategory("Sitcoms", "sitcoms", true);
		final var documentaries = Category.newCategory("Documentaries", "docs", true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAll(
				List.of(movies, sitcoms, documentaries)
						.stream()
						.map(CategoryJpaEntity::from)
						.toList()
		);

		Assertions.assertEquals(3, repository.count());

		final var query = new CategorySearchQuery(0, 1, "mov", "name", "asc");
		final var actualResult = gateway.findAll(query);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
		Assertions.assertEquals(movies.getId(), actualResult.items().get(0).getId());
	}

	@Test
	public void givenPrePersistedCategoriesAndMOVAsTerms_whenCallsFindAllAndTermsMatchDesc_thenShouldReturnPaginated() {
		final var expectedPage = 0;
		final var expectedPerPage = 1;
		final var expectedTotal = 1;
		final var expectedItemsCount = expectedPerPage;

		final var movies = Category.newCategory("Movies", "movies", true);
		final var sitcoms = Category.newCategory("Sitcoms", "sitcoms", true);
		final var documentaries = Category.newCategory("Documentaries", "docs", true);

		Assertions.assertEquals(0, repository.count());

		repository.saveAll(
				List.of(movies, sitcoms, documentaries)
						.stream()
						.map(CategoryJpaEntity::from)
						.toList()
		);

		Assertions.assertEquals(3, repository.count());

		final var query = new CategorySearchQuery(0, 1, "MOV", "name", "asc");
		final var actualResult = gateway.findAll(query);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
		Assertions.assertEquals(movies.getId(), actualResult.items().get(0).getId());
	}
}
