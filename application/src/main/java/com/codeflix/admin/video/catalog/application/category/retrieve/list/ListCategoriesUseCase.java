package com.codeflix.admin.video.catalog.application.category.retrieve.list;

import com.codeflix.admin.video.catalog.application.UseCase;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
