package com.codeflix.admin.video.catalog.application.genre.update;

import java.util.List;

public record UpdateGenreCommand (
        String id,
        String name,
        boolean isActive,
        List<String> categories
){
    public static UpdateGenreCommand with (
            final String anId,
            final String aName,
            final boolean isActive,
            final List<String> someCategories
    ) {
        return new UpdateGenreCommand(anId, aName, isActive, someCategories);
    }
}
