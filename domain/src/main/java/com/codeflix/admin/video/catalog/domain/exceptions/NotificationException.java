package com.codeflix.admin.video.catalog.domain.exceptions;

import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;

public class NotificationException extends DomainException {
    public NotificationException(final String aMessage, final NotificationValidationHandler notificationValidationHandler) {
        super(aMessage, notificationValidationHandler.getErrors());
    }
}
