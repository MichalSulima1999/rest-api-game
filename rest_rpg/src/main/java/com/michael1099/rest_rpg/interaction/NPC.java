package com.michael1099.rest_rpg.interaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NPC extends Interactable {

    private final String dialogue;

    @Override
    public void interact() {
        log.info("Talking with NPC!");
        log.info("NPC says: {}", dialogue);
    }
}
