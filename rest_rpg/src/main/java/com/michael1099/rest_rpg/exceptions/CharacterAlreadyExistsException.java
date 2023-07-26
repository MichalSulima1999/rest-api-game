package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterAlreadyExistsException extends ResponseStatusException {

    public CharacterAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public CharacterAlreadyExistsException() {
        this(ErrorCodes.CHARACTER_ALREADY_EXISTS.toString());
    }
}
