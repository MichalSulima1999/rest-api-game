package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.CharacterApi;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterLite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CharacterController implements CharacterApi {

    private final CharacterService characterService;
    private final IAuthenticationFacade authenticationFacade;

    @Override
    public ResponseEntity<CharacterLite> createCharacter(CharacterCreateRequest characterCreateRequest) {
        var username = authenticationFacade.getAuthentication().getName();
        return ResponseEntity.ok(characterService.createCharacter(characterCreateRequest, username));
    }
}
