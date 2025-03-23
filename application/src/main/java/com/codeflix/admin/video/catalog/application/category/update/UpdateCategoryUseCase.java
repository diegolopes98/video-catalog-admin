package com.codeflix.admin.video.catalog.application.category.update;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface UpdateCategoryUseCase
		extends UseCase<UpdateCategoryCommand, UpdateCategoryOutput>
		permits DefaultUpdateCategoryUseCase { }
