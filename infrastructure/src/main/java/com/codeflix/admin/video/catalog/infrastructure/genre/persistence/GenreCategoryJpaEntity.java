package com.codeflix.admin.video.catalog.infrastructure.genre.persistence;

import com.codeflix.admin.video.catalog.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = GenreCategoryJpaEntity.TABLE_NAME)
public class GenreCategoryJpaEntity {
    public static final String TABLE_NAME = "genres_categories";

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {
    }

    private GenreCategoryJpaEntity(
            final GenreJpaEntity aGenre,
            final CategoryID aCategoryID
    ) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryID.getValue());
        this.genre = aGenre;
    }

    public static GenreCategoryJpaEntity from (
            final GenreJpaEntity aGenre,
            final CategoryID aCategoryID
    ) {
        return new GenreCategoryJpaEntity(aGenre, aCategoryID);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJpaEntity genre) {
        this.genre = genre;
    }
}
