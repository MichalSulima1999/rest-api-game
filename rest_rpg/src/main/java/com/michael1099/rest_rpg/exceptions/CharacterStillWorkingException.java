package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterStillWorkingException extends ResponseStatusException {

    public CharacterStillWorkingException() {
        super(HttpStatus.CONFLICT, ErrorCodes.CHARACTER_STILL_WORKING.toString());
    }
}
