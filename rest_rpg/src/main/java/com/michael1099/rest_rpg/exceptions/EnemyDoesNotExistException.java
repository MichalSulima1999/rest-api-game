package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EnemyDoesNotExistException extends ResponseStatusException {

    public EnemyDoesNotExistException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.ENEMY_DOES_NOT_EXIST.toString());
    }
}
