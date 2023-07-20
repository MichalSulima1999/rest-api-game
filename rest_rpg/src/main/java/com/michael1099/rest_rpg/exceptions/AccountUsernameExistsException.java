package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountUsernameExistsException extends ResponseStatusException {

    public AccountUsernameExistsException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public AccountUsernameExistsException() {
        this(ErrorCodes.ACCOUNT_USERNAME_EXISTS.toString());
    }
}
