package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AdventureNameExistsException extends ResponseStatusException {

    public AdventureNameExistsException() {
        super(HttpStatus.CONFLICT, ErrorCodes.ADVENTURE_NAME_EXISTS.toString());
    }
}
