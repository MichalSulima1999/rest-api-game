package com.michael1099.rest_rpg.task;

import com.michael1099.rest_rpg.character.model.Character;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FightMonsterTask implements Task {

    private final Character character;
    private final String monsterType;
    private final int monstersNumber;

    @Override
    public void startTask() {
        log.info("Starting quest for fighting {} of {}", monstersNumber, monsterType);
        character.acceptMonsterTask(monsterType, monstersNumber);
    }

    @Override
    public void endTask() {
        log.info("Ending quest for fighting {} of {}", monstersNumber, monsterType);
        character.endMonsterTask();
    }
}
