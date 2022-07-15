package com.codeflix.admin.video.catalog.infrastructure.api.controllers;

import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {

	@Override
	public ResponseEntity<?> createCategory() {
		return null;
	}

	@Override
	public Pagination<?> listCategories(
			String search,
			int page,
			int perPage,
			String sort,
			String dir
	) {
		return null;
	}
}
