package com.codeflix.admin.video.catalog.domain.genre;

import com.codeflix.admin.video.catalog.domain.AggregateRoot;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> someCategories,
            final Instant aCreateDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = someCategories;
        this.createdAt = aCreateDate;
        this.updatedAt = anUpdateDate;
        this.deletedAt = aDeleteDate;

        final var notificationHandler = NotificationValidationHandler.create();

        validate(notificationHandler);

        if (notificationHandler.hasErrors()) {
            throw new NotificationException("Failed to create Aggregate Genre", notificationHandler);
        }
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> someCategories,
            final Instant aCreateDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate
    ) {
        return new Genre(
                anId,
                aName,
                isActive,
                someCategories,
                aCreateDate,
                anUpdateDate,
                aDeleteDate
        );
    }

    public static Genre with(
            final Genre aGenre
    ) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        final Instant anDeleteDate = isActive ? null : now;
        return Genre.with(
                anId,
                aName,
                isActive,
                new ArrayList<>(),
                now,
                now,
                anDeleteDate
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
