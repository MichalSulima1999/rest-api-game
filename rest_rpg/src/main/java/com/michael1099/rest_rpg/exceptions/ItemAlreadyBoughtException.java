package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemAlreadyBoughtException extends ResponseStatusException {

    public ItemAlreadyBoughtException() {
        super(HttpStatus.CONFLICT, ErrorCodes.ITEM_ALREADY_BOUGHT.toString());
    }
}
