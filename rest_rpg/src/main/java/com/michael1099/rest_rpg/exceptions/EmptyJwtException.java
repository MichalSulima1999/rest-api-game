package com.michael1099.rest_rpg.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyJwtException extends ResponseStatusException {

    public EmptyJwtException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public EmptyJwtException() {
        this("JWT ");
    }
}
