package com.codeflix.admin.video.catalog.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
		int currentPage,
		int perPage,
		long total,
		List<T> items
) {

	public <R> Pagination<R> map(final Function<T, R> mapper) {
		final List<R> aMappedList = this.items
				.stream()
				.map(mapper)
				.toList();
		return new Pagination<R>(currentPage(), perPage(), total(), aMappedList);
	}
}
