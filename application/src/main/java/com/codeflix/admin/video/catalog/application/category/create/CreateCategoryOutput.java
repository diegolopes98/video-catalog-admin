package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;

public record CreateCategoryOutput(
		CategoryID id
) {
	public static CreateCategoryOutput from(final Category aCategory) {
		return new CreateCategoryOutput(aCategory.getId());
	}
}
