package com.michael1099.rest_rpg.fight.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoseFightCommand implements EndFightCommand {

    private final EndFight endFight;

    @Override
    public void execute() {
        endFight.loseFight();
    }
}
