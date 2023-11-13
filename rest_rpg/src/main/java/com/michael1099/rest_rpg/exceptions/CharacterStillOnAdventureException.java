package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterStillOnAdventureException extends ResponseStatusException {

    public CharacterStillOnAdventureException() {
        super(HttpStatus.CONFLICT, ErrorCodes.CHARACTER_STILL_ON_ADVENTURE.toString());
    }
}
