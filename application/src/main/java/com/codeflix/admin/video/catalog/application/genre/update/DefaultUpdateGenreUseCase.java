package com.codeflix.admin.video.catalog.application.genre.update;

import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.category.CategoryID;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.domain.genre.Genre;
import com.codeflix.admin.video.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public non-sealed class DefaultUpdateGenreUseCase implements UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(UpdateGenreCommand aCommand) {
        final var anId = GenreID.from(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryId(aCommand.categories());

        final var aGenre = this.genreGateway.findById(anId)
                .orElseThrow(notFound(anId));

        NotificationValidationHandler validationHandler = NotificationValidationHandler.create();
        validationHandler.append(validateCategories(categories));
        validationHandler.validate(() -> aGenre.update(
                aName,
                isActive,
                categories
        ));

        if (validationHandler.hasErrors()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre with ID %s".formatted(aCommand.id()),
                    validationHandler
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private List<CategoryID> toCategoryId(List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }

    private Supplier<NotFoundException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(
                Genre.class,
                anId
        );
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
}
