package com.codeflix.admin.video.catalog.e2e;

import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.video.catalog.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.video.catalog.domain.Identifier;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.configuration.json.Json;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    /**
     * Category
     */
    default CategoryID givenACategory(
            final String aName,
            final String aDescription,
            final boolean isActive
    ) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var output = this.performPost("/categories", aRequestBody, CreateCategoryOutput.class);

        return CategoryID.from(output.id());
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        final var url = "/categories/" + anId.getValue();

        final var result =  performGet(url);

        final var json = result
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }

    default ResultActions updateACategory(
            final CategoryID anId,
            final UpdateCategoryRequest updateCategoryRequest
    ) throws Exception {
        return this.performPut("/categories/" + anId.getValue(), updateCategoryRequest);
    }

    default ResultActions deleteACategory(final CategoryID anId) throws Exception {
        return this.performDelete("/categories/" + anId.getValue());
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search
    ) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage
    ) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return this.list(
                "/categories",
                page,
                perPage,
                search,
                sort,
                direction
        );
    }

    /**
     * Genre
     */
    default GenreID givenAGenre(
            final String aName,
            final List<CategoryID> categories,
            final boolean isActive
    ) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);

        final var output = this.performPost("/genres", aRequestBody, CreateGenreOutput.class);

        return GenreID.from(output.id());
    }

    default GenreResponse retrieveAGenre(final Identifier anId) throws Exception {
        final var url = "/genres/" + anId.getValue();

        final var result =  this.performGet(url);

        final var json = result
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, GenreResponse.class);
    }

    default ResultActions updateAGenre(
            final GenreID anId,
            final UpdateGenreRequest updateGenreRequest
    ) throws Exception {
        return this.performPut("/genres/" + anId.getValue(), updateGenreRequest);
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search
    ) throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default ResultActions listGenres(
            final int page,
            final int perPage
    ) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return this.list(
                "/genres",
                page,
                perPage,
                search,
                sort,
                direction
        );
    }

    /**
     * Utils
     */
    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var aRequest = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);

    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private <T> T performPost(final String url, final Object body, final Class<T> clazz) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var response = this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(response, clazz);
    }

    private ResultActions performGet(final String url) throws Exception {
        final var aRequest = get(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions performDelete(final String url) throws Exception {
        final var aRequest = delete(url)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions performPut(final String url, final Object aRequestBody) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }
}
