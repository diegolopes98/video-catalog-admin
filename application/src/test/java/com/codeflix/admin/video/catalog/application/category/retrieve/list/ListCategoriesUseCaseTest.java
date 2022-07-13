package com.codeflix.admin.video.catalog.application.category.retrieve.list;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategorySearchQuery;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanup() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        final var aCategoryList = List.of(
                Category.newCategory("Movies", null, true),
                Category.newCategory("Sitcoms", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectDirection);

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                aCategoryList.size(),
                aCategoryList
        );

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(aCategoryList.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var aCategoryList = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectDirection);

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                aCategoryList.size(),
                aCategoryList
        );

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(aCategoryList.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsError_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectDirection = "asc";
        final var expectedErrorMessage = "gateway mock exception";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectDirection);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
