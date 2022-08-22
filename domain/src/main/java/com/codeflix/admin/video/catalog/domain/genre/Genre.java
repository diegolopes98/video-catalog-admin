package com.codeflix.admin.video.catalog.domain.genre;

import com.codeflix.admin.video.catalog.domain.AggregateRoot;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.domain.utils.InstantUtils;
import com.codeflix.admin.video.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;

import java.time.Instant;
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

        selfValidate();
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

    public static Genre from(
            final Genre aGenre
    ) {
        return Genre.with(
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
        final var now = InstantUtils.now();
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

    public Genre activate() {
        this.deletedAt = null;
        this.updatedAt = InstantUtils.now();
        this.active = true;
        return this;
    }

    public Genre deactivate() {
        final var now = InstantUtils.now();
        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }
        this.updatedAt = now;
        this.active = false;
        return this;
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {
        this.name = aName;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
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

    private void selfValidate() {
        final var notificationHandler = NotificationValidationHandler.create();

        validate(notificationHandler);

        if (notificationHandler.hasErrors()) {
            throw new NotificationException("Failed to validate Aggregate Genre", notificationHandler);
        }
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;
        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryID> aCategoryIDList) {
        if (aCategoryIDList == null || aCategoryIDList.isEmpty()) return this;
        this.categories.addAll(aCategoryIDList);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;
        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }
}
