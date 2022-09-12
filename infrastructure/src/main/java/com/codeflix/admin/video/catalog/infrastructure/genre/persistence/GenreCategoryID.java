package com.codeflix.admin.video.catalog.infrastructure.genre.persistence;

import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(name = "genre_id", nullable = false)
    private String genreId;


    @Column(name = "category_id", nullable = false)
    private String categoryId;

    public GenreCategoryID() {}

    private GenreCategoryID(
            final String aGenreId,
            final String aCategoryId
    ) {
        this.genreId = aGenreId;
        this.categoryId = aCategoryId;
    }

    public static GenreCategoryID from(
            final String aGenreId,
            final String aCategoryId
    ) {
        return new GenreCategoryID(aGenreId, aCategoryId);
    }

    public static GenreCategoryID from(
            final GenreID aGenreId,
            final CategoryID aCategoryId
    ) {
        return new GenreCategoryID(aGenreId.toString(), aCategoryId.toString());
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(genreId, that.genreId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, categoryId);
    }
}
