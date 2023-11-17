package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SkillCharacterClassMismatchException extends ResponseStatusException {

    public SkillCharacterClassMismatchException() {
        super(HttpStatus.CONFLICT, ErrorCodes.SKILL_CHARACTER_CLASS_MISMATCH.toString());
    }
}
