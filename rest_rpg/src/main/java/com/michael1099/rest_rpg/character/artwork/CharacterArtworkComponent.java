package com.michael1099.rest_rpg.character.artwork;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface CharacterArtworkComponent {

    ResponseEntity<Resource> getArtwork(String characterArtwork);
}
