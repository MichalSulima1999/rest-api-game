package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WorkNotFoundException extends ResponseStatusException {

    public WorkNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.WORK_NOT_FOUND.toString());
    }
}
