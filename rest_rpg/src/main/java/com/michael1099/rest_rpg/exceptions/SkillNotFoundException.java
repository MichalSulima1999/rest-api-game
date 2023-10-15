package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SkillNotFoundException extends ResponseStatusException {

    public SkillNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.SKILL_NOT_FOUND.toString());
    }
}
