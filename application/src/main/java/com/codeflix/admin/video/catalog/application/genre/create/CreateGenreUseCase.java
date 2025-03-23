package com.codeflix.admin.video.catalog.application.genre.create;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface CreateGenreUseCase
        extends UseCase<CreateGenreCommand, CreateGenreOutput>
        permits DefaultCreateGenreUseCase { }
