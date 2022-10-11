package com.codeflix.admin.video.catalog.application.genre.retrieve.list;


import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
		String id,
		String name,
		boolean isActive,
		List<String> categories,
		Instant createdAt,
		Instant deletedAt
) {
	public static GenreListOutput from(final Genre aGenre) {
		return new GenreListOutput(
				aGenre.getId().getValue(),
				aGenre.getName(),
				aGenre.isActive(),
				asString(aGenre.getCategories()),
				aGenre.getCreatedAt(),
				aGenre.getDeletedAt()
		);
	}

	private static List<String> asString(final List<CategoryID> ids) {
		return ids.stream()
				.map(CategoryID::getValue)
				.toList();
	}
}
