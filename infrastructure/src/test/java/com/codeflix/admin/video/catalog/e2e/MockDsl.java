package com.codeflix.admin.video.catalog.e2e;

import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.video.catalog.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.video.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.video.catalog.infrastructure.configuration.json.Json;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.GenreResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    default CategoryID givenACategory(
            final String aName,
            final String aDescription,
            final boolean isActive
    ) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var output = this.given("/categories", aRequestBody, CreateCategoryOutput.class);

        return CategoryID.from(output.id());
    }

    default GenreID givenAGenre(
            final String aName,
            final List<CategoryID> categories,
            final boolean isActive
    ) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);

        final var output = this.given("/genres", aRequestBody, CreateGenreOutput.class);

        return GenreID.from(output.id());
    }

    default GenreResponse retrieveAGenre(final String anId) throws Exception {
        final var url = "/genres/" + anId;
        return retrieve(url, GenreResponse.class);
    }

    default CategoryResponse retrieveACategory(final String anId) throws Exception {
        final var url = "/genres/" + anId;
        return retrieve(url, CategoryResponse.class);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private <T> T given(final String url, final Object body, final Class<T> clazz) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var response = this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(response, clazz);
    }

    private <T> T retrieve(final String url, final Class<T> clazz) throws Exception {
        final var aRequest = get(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(response, clazz);
    }
}
