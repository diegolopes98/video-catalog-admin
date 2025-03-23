package com.codeflix.admin.video.catalog.application.genre.update;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface UpdateGenreUseCase
        extends UseCase<UpdateGenreCommand, UpdateGenreOutput>
        permits DefaultUpdateGenreUseCase { }
