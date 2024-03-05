package com.michael1099.rest_rpg.character.artwork;

import com.michael1099.rest_rpg.character.CharacterService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

// Tydzień 3, Composite
// Liść
public class CharacterFullArtwork implements CharacterArtworkComponent {

    private static final String ARTWORKS_PATH = "public/avatars/full/";
    private final CharacterService characterService;

    public CharacterFullArtwork(CharacterService artworkService) {
        this.characterService = artworkService;
    }

    @Override
    public ResponseEntity<Resource> getArtwork(String characterArtwork) {
        return characterService.getCharacterArtwork(characterArtwork, ARTWORKS_PATH);
    }
}
// Koniec Tydzień 3, Composite
