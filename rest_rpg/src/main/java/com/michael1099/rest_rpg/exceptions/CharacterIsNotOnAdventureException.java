package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharacterIsNotOnAdventureException extends ResponseStatusException {

    public CharacterIsNotOnAdventureException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.CHARACTER_IS_NOT_ON_ADVENTURE.toString());
    }
}
