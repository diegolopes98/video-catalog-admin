package com.codeflix.admin.video.catalog.application.category.retrieve.list;

import com.codeflix.admin.video.catalog.IntegrationTest;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesUseCaseIT {

	@Autowired
	private ListCategoriesUseCase useCase;

	@Autowired
	private CategoryRepository repository;

	@BeforeEach
	void mockup() {
		final var categories = Stream.of(
				Category.newCategory("Movies", null, true),
				Category.newCategory("Netflix Originals", "Netflix proprietary productions", true),
				Category.newCategory("Amazon Originals", "Amazon Prime proprietary productions", true),
				Category.newCategory("Documentaries", null, true),
				Category.newCategory("Sports", null, true),
				Category.newCategory("Kids", "children", true),
				Category.newCategory("Sitcoms", null, true)
		).map(CategoryJpaEntity::from).toList();

		repository.saveAllAndFlush(categories);
	}

	@Test
	public void givenAValidTerm_whenTermDoesntMatchPrePersisted_thenShouldReturnEmptyPage() {
		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTerms = "non existent";
		final var expectedSort = "name";
		final var expectedDirection = "asc";
		final var expectedItemsCount = 0;
		final var expectedTotal = 0;

		final var aQuery = new SearchQuery(
				expectedPage,
				expectedPerPage,
				expectedTerms,
				expectedSort,
				expectedDirection
		);

		final var actualResult = useCase.execute(aQuery);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
	}

	@ParameterizedTest
	@CsvSource({
			"mov,0,10,1,1,Movies",
			"net,0,10,1,1,Netflix Originals",
			"ZON,0,10,1,1,Amazon Originals",
			"Ki,0,10,1,1,Kids",
			"child,0,10,1,1,Kids",
			"Prime proprietary,0,10,1,1,Amazon Originals",
			"proprietary,0,1,1,2,Amazon Originals",
	})
	public void givenAValidTerm_whenCallsListCategories_thenShouldReturnCategoriesFiltered(
			final String expectedTerms,
			final int expectedPage,
			final int expectedPerPage,
			final int expectedItemsCount,
			final long expectedTotal,
			final String expectedCategoryName
	) {

		final var expectedSort = "name";
		final var expectedDirection = "asc";

		final var aQuery = new SearchQuery(
				expectedPage,
				expectedPerPage,
				expectedTerms,
				expectedSort,
				expectedDirection
		);

		final var actualResult = useCase.execute(aQuery);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
		Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
	}

	@ParameterizedTest
	@CsvSource({
			"name,asc,0,10,7,7,Amazon Originals",
			"name,desc,0,10,7,7,Sports",
			"createdAt,asc,0,10,7,7,Movies",
			"createdAt,desc,0,10,7,7,Sitcoms",
	})
	public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnSorted(
			final String expectedSort,
			final String expectedDirection,
			final int expectedPage,
			final int expectedPerPage,
			final int expectedItemsCount,
			final long expectedTotal,
			final String expectedCategoryName
	) {
		final var expectedTerms = "";

		final var aQuery = new SearchQuery(
				expectedPage,
				expectedPerPage,
				expectedTerms,
				expectedSort,
				expectedDirection
		);

		final var actualResult = useCase.execute(aQuery);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
		Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
	}

	@ParameterizedTest
	@CsvSource({
			"0,2,2,7,Amazon Originals;Documentaries",
			"1,2,2,7,Kids;Movies",
			"2,2,2,7,Netflix Originals;Sitcoms",
			"3,2,1,7,Sports",
	})
	public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
			final int expectedPage,
			final int expectedPerPage,
			final int expectedItemsCount,
			final long expectedTotal,
			final String expectedCategoriesNames
	) {
		final var expectedTerms = "";
		final var expectedSort = "name";
		final var expectedDirection = "asc";

		final var aQuery = new SearchQuery(
				expectedPage,
				expectedPerPage,
				expectedTerms,
				expectedSort,
				expectedDirection
		);

		final var actualResult = useCase.execute(aQuery);

		Assertions.assertEquals(expectedPage, actualResult.currentPage());
		Assertions.assertEquals(expectedPerPage, actualResult.perPage());
		Assertions.assertEquals(expectedTotal, actualResult.total());
		Assertions.assertEquals(expectedItemsCount, actualResult.items().size());

		int index = 0;
		for (String expectedName : expectedCategoriesNames.split(";")) {
			final String actualName =  actualResult.items().get(index).name();
			Assertions.assertEquals(expectedName, actualName);
			index++;
		}

	}
}
