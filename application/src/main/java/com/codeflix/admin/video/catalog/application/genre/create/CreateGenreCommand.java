package com.codeflix.admin.video.catalog.application.genre.create;

import java.util.List;

public record CreateGenreCommand(
        String name,
        boolean isActive,
        List<String> categories
) {
    public static CreateGenreCommand with(
            final String aName,
            final boolean isActive,
            final List<String> someCategories
    ) {
        return new CreateGenreCommand(aName, isActive, someCategories);
    }
}
