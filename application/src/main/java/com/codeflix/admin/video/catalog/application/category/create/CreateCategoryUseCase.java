package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, CreateCategoryOutput>
        permits DefaultCreateCategoryUseCase { }
