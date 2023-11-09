package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.character.model.Character;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

    void checkIfCharacterBelongsToUser(@NotNull Character character);
}
