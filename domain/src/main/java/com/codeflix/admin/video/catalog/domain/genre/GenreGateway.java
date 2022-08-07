package com.codeflix.admin.video.catalog.domain.genre;

import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Genre create(final Genre aGenre);

    void deleteById(final GenreID anId);

    Optional<Genre> findById(final GenreID anId);

    Genre update(final Genre aGenre);

    Pagination<Genre> findAll(final SearchQuery aQuery);
}
