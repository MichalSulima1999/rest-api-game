package com.michael1099.rest_rpg.interaction;

import com.michael1099.rest_rpg.character.model.Character;
import lombok.extern.slf4j.Slf4j;

// Tydzień 8 - Zastosuj zasadę Podstawienia Liskov
// Istnieją 3 abstrakcje: Interactable, Task i Weapon
@Slf4j
public abstract class Interactable {

    protected Character character;

    public void interact() {
        log.info("Interacting with something!");
    }
}
// Koniec Tydzień 8 - Zastosuj zasadę Podstawienia Liskov
