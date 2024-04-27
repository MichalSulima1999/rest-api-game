package com.michael1099.rest_rpg.interaction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InteractionService {

    private final Interactable interactable;

    public void interact() {
        interactable.interact();
    }
}
