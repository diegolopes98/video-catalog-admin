package com.codeflix.admin.video.catalog.application.genre.delete;

import com.codeflix.admin.video.catalog.application.UnitUseCase;

public sealed interface DeleteGenreUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteGenreUseCase { }
