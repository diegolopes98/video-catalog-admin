package com.codeflix.admin.video.catalog.application.genre.create;

import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.domain.genre.Genre;
import com.codeflix.admin.video.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public non-sealed class DefaultCreateGenreUseCase implements CreateGenreUseCase {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var someCategories = toCategoryId(aCommand.categories());

        final var notificationHandler = NotificationValidationHandler.create();
        notificationHandler.append(validateCategories(someCategories));

        final var aGenre =
                notificationHandler.validate(() -> Genre.newGenre(aName, isActive));

        if (notificationHandler.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Genre", notificationHandler);
        }

        aGenre.addCategories(someCategories);

        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private NotificationValidationHandler validateCategories(List<CategoryID> ids) {
        final var validationHandler = NotificationValidationHandler.create();
        if (ids == null || ids.isEmpty()) {
            return validationHandler;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (retrievedIds.size() != ids.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);
            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            validationHandler.append(
                    new Error("Some categories could not be found: %s".formatted(missingIdsMessage))
            );
        }

        return validationHandler;
    }

    private List<CategoryID> toCategoryId(List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
