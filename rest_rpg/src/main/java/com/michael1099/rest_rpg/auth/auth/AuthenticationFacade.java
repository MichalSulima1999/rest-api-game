package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.exceptions.CharacterNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void checkIfCharacterBelongsToUser(@NotNull Character character) {
        if (!Objects.equals(character.getUser().getUsername(), getAuthentication().getName())) {
            throw new CharacterNotFoundException();
        }
    }
}
