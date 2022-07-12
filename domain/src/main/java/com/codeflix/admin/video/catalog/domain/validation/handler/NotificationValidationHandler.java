package com.codeflix.admin.video.catalog.domain.validation.handler;

import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.video.catalog.domain.validation.Error;
import com.codeflix.admin.video.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class NotificationValidationHandler implements ValidationHandler {

    private final List<Error> errors;

    private NotificationValidationHandler(final List<Error> errors) {
        this.errors = errors;
    }

    public static NotificationValidationHandler create() {
        return new NotificationValidationHandler(new ArrayList<>());
    }

    public static NotificationValidationHandler create(final Error anError) {
        return new NotificationValidationHandler(new ArrayList<>()).append(anError);
    }

    public static NotificationValidationHandler create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    @Override
    public NotificationValidationHandler append(Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public NotificationValidationHandler append(ValidationHandler aHandler) {
        this.errors.addAll(aHandler.getErrors());
        return this;
    }

    @Override
    public NotificationValidationHandler validate(Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

    @Override
    public boolean hasErrors() {
        return ValidationHandler.super.hasErrors();
    }
}
