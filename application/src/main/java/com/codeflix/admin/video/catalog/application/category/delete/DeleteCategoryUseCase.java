package com.codeflix.admin.video.catalog.application.category.delete;

import com.codeflix.admin.video.catalog.application.UnitUseCase;

public sealed interface DeleteCategoryUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCategoryUseCase { }
