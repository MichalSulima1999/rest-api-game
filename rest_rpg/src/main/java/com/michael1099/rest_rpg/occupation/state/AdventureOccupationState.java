package com.michael1099.rest_rpg.occupation.state;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.exceptions.CharacterIsOnAdventureException;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.work.model.Work;

public class AdventureOccupationState implements OccupationState {

    @Override
    public void startAdventure(Adventure adventure, Occupation occupation) {
        throw new CharacterIsOnAdventureException();
    }

    @Override
    public void endAdventure(Adventure adventure, Occupation occupation) {
        var enemy = adventure.getEnemy();
        occupation.getFight().setEnemy(enemy);
        occupation.getFight().setActive(true);
        occupation.getFight().setEnemyCurrentHp(enemy.getHp());
        occupation.getFight().setEnemyCurrentMana(enemy.getMana());
    }

    @Override
    public void startWork(Work work, Occupation occupation) {
        throw new CharacterIsOnAdventureException();
    }

    @Override
    public void endWork(Work work, Occupation occupation) {
        throw new CharacterIsOnAdventureException();
    }
}
