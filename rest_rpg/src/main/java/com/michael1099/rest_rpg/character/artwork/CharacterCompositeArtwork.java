package com.michael1099.rest_rpg.character.artwork;

import com.michael1099.rest_rpg.exceptions.ImageDoesNotExistException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

// Tydzień 3, Composite
// Klasa composite, która posiada tablicę CharacterArtworkComponent
// Pozwala na pobranie obrazka. Liście zawierają różne sposoby na ich pobieranie, dzięki czemu
// jeżeli dany obrazek nie jest dostępny z jednego liścia, automatycznie następuje próba
// jego pobrania z następnego liścia.
public class CharacterCompositeArtwork implements CharacterArtworkComponent {

    private final CharacterArtworkComponent[] components;

    public CharacterCompositeArtwork(CharacterArtworkComponent... components) {
        this.components = components;
    }

    @Override
    public ResponseEntity<Resource> getArtwork(String characterArtwork) {
        for (CharacterArtworkComponent component : components) {
            ResponseEntity<Resource> response = component.getArtwork(characterArtwork);
            if (response != null) {
                return response;
            }
        }
        throw new ImageDoesNotExistException();
    }
}
// Koniec Tydzień 3, Composite
