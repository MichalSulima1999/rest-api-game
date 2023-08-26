package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GetImageException extends ResponseStatusException {

    public GetImageException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.GET_IMAGE.toString());
    }
}
