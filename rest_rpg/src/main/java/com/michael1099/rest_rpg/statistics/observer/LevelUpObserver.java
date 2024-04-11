package com.michael1099.rest_rpg.statistics.observer;

import com.michael1099.rest_rpg.character.model.Character;

public interface LevelUpObserver {

    void onLevelUp(Character character);
}

