package com.michael1099.rest_rpg.interaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Chest extends Interactable {

    private final int gold;

    @Override
    public void interact() {

        log.info("Opened chest with {} gold!", gold);
        character.getEquipment().earnGold(gold);
    }
}
