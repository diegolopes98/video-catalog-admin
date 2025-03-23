package com.codeflix.admin.video.catalog.application.category.retrieve.get;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface GetCategoryByIdUseCase
        extends UseCase<String, CategoryOutput>
        permits DefaultGetCategoryByIdUseCase { }
