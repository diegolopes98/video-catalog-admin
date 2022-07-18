package com.codeflix.admin.video.catalog.application.category.update;

import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;

public record UpdateCategoryOutput(
		String id
) {

	public static UpdateCategoryOutput from(String anId) {
		return new UpdateCategoryOutput(anId);
	}
	public static UpdateCategoryOutput from(Category aCategory) {
		return new UpdateCategoryOutput(aCategory.getId().getValue());
	}
}
