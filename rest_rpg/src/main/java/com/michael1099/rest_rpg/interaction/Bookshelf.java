package com.michael1099.rest_rpg.interaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Bookshelf extends Interactable {

    private final int intelligencePoints;

    @Override
    public void interact() {
        log.info("Reading book! Intelligence + {}", intelligencePoints);
        character.getStatistics().trainIntelligence(intelligencePoints);
    }
}
