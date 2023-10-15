package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EnemyAlreadyExistsException extends ResponseStatusException {

    public EnemyAlreadyExistsException() {
        super(HttpStatus.CONFLICT, ErrorCodes.ENEMY_ALREADY_EXISTS.toString());
    }
}
