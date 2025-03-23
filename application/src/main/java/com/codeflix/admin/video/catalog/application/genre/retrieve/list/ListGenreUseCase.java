package com.codeflix.admin.video.catalog.application.genre.retrieve.list;

import com.codeflix.admin.video.catalog.application.UseCase;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;

public sealed interface ListGenreUseCase
        extends UseCase<SearchQuery, Pagination<GenreListOutput>>
        permits DefaultListGenreUseCase {}
