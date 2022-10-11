package com.codeflix.admin.video.catalog.infrastructure.api.controllers;

import com.codeflix.admin.video.catalog.application.genre.create.CreateGenreCommand;
import com.codeflix.admin.video.catalog.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.video.catalog.application.genre.delete.DeleteGenreUseCase;
import com.codeflix.admin.video.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.codeflix.admin.video.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.codeflix.admin.video.catalog.application.genre.update.UpdateGenreCommand;
import com.codeflix.admin.video.catalog.application.genre.update.UpdateGenreUseCase;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.video.catalog.infrastructure.api.GenreAPI;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.GenreListResponse;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.video.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.codeflix.admin.video.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final ListGenreUseCase listGenreUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var aQuery = new SearchQuery(
                page,
                perPage,
                search,
                sort,
                direction
        );
        return this.listGenreUseCase.execute(aQuery).map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}
