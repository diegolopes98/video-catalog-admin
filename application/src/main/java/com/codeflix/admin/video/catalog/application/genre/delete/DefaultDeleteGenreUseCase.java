package com.codeflix.admin.video.catalog.application.genre.delete;

import com.codeflix.admin.video.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;

import java.util.Objects;

public non-sealed class DefaultDeleteGenreUseCase implements DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(String anId) {
        this.genreGateway.deleteById(GenreID.from(anId));
    }
}
