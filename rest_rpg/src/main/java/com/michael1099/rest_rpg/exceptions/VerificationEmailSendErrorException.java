package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class VerificationEmailSendErrorException extends ResponseStatusException {

    public VerificationEmailSendErrorException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public VerificationEmailSendErrorException() {
        this(ErrorCodes.VERIFICATION_EMAIL_SEND_ERROR.toString());
    }
}
