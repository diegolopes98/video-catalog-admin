package com.codeflix.admin.video.catalog.infrastructure.genre.models;

import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ApiErrorResponse(
        @JsonProperty("message") String message,
        @JsonProperty("errors") List<Error> errors
) {
    public static ApiErrorResponse from(final DomainException e) {
        return new ApiErrorResponse(e.getMessage(), e.getErrors());
    }

    public static ApiErrorResponse from(final Exception e) {
        return new ApiErrorResponse(e.getMessage(), List.of());
    }
}
