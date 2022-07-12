package com.codeflix.admin.video.catalog.domain.exceptions;

import com.codeflix.admin.video.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException {
    private final List<Error> errors;

    private DomainException(final List<Error> anErrorList) {
        super("", null, true, false);
        this.errors = anErrorList;
    }

    public static DomainException with(final List<Error> anErrorList) {
        return new DomainException(anErrorList);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
