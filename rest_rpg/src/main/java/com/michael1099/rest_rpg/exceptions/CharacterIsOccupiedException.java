package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterIsOccupiedException extends ResponseStatusException {

    public CharacterIsOccupiedException() {
        super(HttpStatus.CONFLICT, ErrorCodes.CHARACTER_IS_OCCUPIED.toString());
    }
}
