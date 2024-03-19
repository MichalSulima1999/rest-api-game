package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterIsAtWorkException extends ResponseStatusException {

    public CharacterIsAtWorkException() {
        super(HttpStatus.CONFLICT, ErrorCodes.CHARACTER_IS_AT_WORK.toString());
    }
}
