package com.codeflix.admin.video.catalog.application.genre.retrieve.get;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface GetGenreByIdUseCase
        extends UseCase<String, GenreOutput>
        permits DefaultGetGenreByIdUseCase { }
