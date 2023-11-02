package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FightIsOngoingException extends ResponseStatusException {

    public FightIsOngoingException() {
        super(HttpStatus.CONFLICT, ErrorCodes.FIGHT_IS_ONGOING.toString());
    }
}
