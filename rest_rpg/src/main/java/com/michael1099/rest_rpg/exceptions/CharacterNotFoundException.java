package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterNotFoundException extends ResponseStatusException {

    public CharacterNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.CHARACTER_NOT_FOUND.toString());
    }
}
