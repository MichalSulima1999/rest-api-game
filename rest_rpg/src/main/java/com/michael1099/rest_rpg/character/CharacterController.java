package com.michael1099.rest_rpg.character;

import com.michael1099.rest_rpg.auth.auth.IAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.CharacterApi;
import org.openapitools.model.CharacterBasic;
import org.openapitools.model.CharacterBasics;
import org.openapitools.model.CharacterCreateRequest;
import org.openapitools.model.CharacterLite;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CharacterController implements CharacterApi {

    private final CharacterService characterService;
    private final IAuthenticationFacade authenticationFacade;

    @Override
    public ResponseEntity<CharacterLite> createCharacter(CharacterCreateRequest characterCreateRequest) {
        var username = authenticationFacade.getAuthentication().getName();
        return ResponseEntity.ok(characterService.createCharacter(characterCreateRequest, username));
    }

    @Override
    public ResponseEntity<Resource> getCharacterArtwork(String characterArtwork) {
        return characterService.getCharacterFullArtwork(characterArtwork);
    }

    @Override
    public ResponseEntity<List<String>> getCharacterArtworkEnum() {
        return ResponseEntity.ok(characterService.getCharacterArtworkEnum());
    }

    @Override
    public ResponseEntity<Resource> getCharacterArtworkThumbnail(String characterArtwork) {
        return characterService.getCharacterThumbnailArtwork(characterArtwork);
    }

    @Override
    public ResponseEntity<CharacterBasics> getUserCharacters() {
        return ResponseEntity.ok(characterService.getUserCharacters());
    }

    @Override
    public ResponseEntity<CharacterBasic> getUserCharacter(Long characterId) {
        return ResponseEntity.ok(characterService.getUserCharacter(characterId));
    }
}
